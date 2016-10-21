package com.easemob.weichat.integration.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.easemob.weichat.integration.paging.PagingRequest;
import com.easemob.weichat.integration.paging.PagingResult;
import com.easemob.weichat.integration.paging.PagingStateHelper;
import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.UserTracks;
import rx.Observable;

@Repository
public class UserTracksCassandraTemplate {
  public static final String TABLE_NAME = "growing_visitor_trace";

  public static final String TENANT_ID = "tenant_id";
  public static final String VISITOR_ID = "visitor_id";
  public static final String GROWINGIO_ID = "growingio_id";
  public static final String TIMESTAMP = "timestamp";
  public static final String ATTRS = "attrs";
  public static final String TYPE = "type";

  @Autowired
  private CassandraOperations cassandraOperations;

  private final RowMapper<UserTracks> mapper = new RowMapper<UserTracks>(){
    @Override
    public UserTracks mapRow(Row row, int rowNum){
      UserTracks userTracks = new UserTracks();
      userTracks.setTenantId(row.getInt(TENANT_ID));
      userTracks.setVisitorId(row.getString(VISITOR_ID));
      userTracks.setGrowingioId(row.getString(GROWINGIO_ID));
      userTracks.setTimestamp(row.getDate(TIMESTAMP));
      userTracks
          .setAttributes(row.getMap(ATTRS, String.class, String.class));
      userTracks.setType(row.getString(TYPE));
      return userTracks;
    }
  };

  public Observable<ResultSet> insert(UserTracks userTracks) {
    Insert insert = QueryBuilder.insertInto(TABLE_NAME);
    insert.value(TENANT_ID, userTracks.getTenantId());
    insert.value(VISITOR_ID, userTracks.getVisitorId());
    insert.value(GROWINGIO_ID, userTracks.getGrowingioId());
    insert.value(TIMESTAMP, userTracks.getTimestamp());
    insert.value(ATTRS, userTracks.getAttributes());
    insert.value(TYPE, userTracks.getType());
    ResultSetFuture resultSetFuture = cassandraOperations.executeAsynchronously(insert);
    return RxHelper.to(resultSetFuture);
  }

  public Observable<ResultSet> delete(int tenantId, String visitorUserId) {
    Clause c1 = QueryBuilder.eq(TENANT_ID, tenantId);
    Clause c2 = QueryBuilder.eq(VISITOR_ID, visitorUserId);
    Statement statement = QueryBuilder.delete().from(TABLE_NAME).where(c1).and(c2);
    statement.setIdempotent(true);
    ResultSetFuture resultSetFuture = cassandraOperations.executeAsynchronously(statement);
    return RxHelper.to(resultSetFuture);
  }

  public PagingResult<UserTracks> findVisitorTracks(int tenantId,
      String visitorUserId, PagingRequest pagingRequest) {
    return findVisitorAllTracks(tenantId,visitorUserId, pagingRequest).toBlocking().first();
}
  
  private Observable<PagingResult<UserTracks>> findVisitorAllTracks(int tenantId,
      String visitorUserId, PagingRequest pagingRequest) {
    Integer limit = pagingRequest.getLimit();
    PagingState pagingState =
        PagingStateHelper.toPagingState(pagingRequest.getCursor()).orElse(null);
    ResultSetFuture resultSetFuture =
        findByTenantIdAndVisitorId(tenantId, visitorUserId, limit, pagingState);
    Observable<ResultSet> observable = RxHelper.to(resultSetFuture);
    return observable.flatMap(rs -> {
      int resultSize = rs.getAvailableWithoutFetching();
      PagingState nextPagingState = rs.getExecutionInfo().getPagingState();
      String nextCursor = PagingStateHelper.toBase64PagingState(nextPagingState).orElse(null);
      return Observable.from(rs).take(resultSize).map(row -> mapper.mapRow(row, 0)).toList()
          .map(entities -> new PagingResult<>(entities, nextCursor, pagingRequest.getCursor()));
    });
  }

  private ResultSetFuture findByTenantIdAndVisitorId(int tenantId, String visitorUserId,
      Integer limit, PagingState pagingState) {
    Clause c1 = QueryBuilder.eq(TENANT_ID, tenantId);
    Clause c2 = QueryBuilder.eq(VISITOR_ID, visitorUserId);
    Statement statement = QueryBuilder.select().all().from(TABLE_NAME).where(c1).and(c2).orderBy(QueryBuilder.desc(TIMESTAMP));
    if (limit != null && limit > 0) {
      statement.setFetchSize(limit);
    }
    if (pagingState != null) {
      statement.setPagingState(pagingState);
    }

    statement.setIdempotent(true);
    return cassandraOperations.executeAsynchronously(statement);
  }

}
