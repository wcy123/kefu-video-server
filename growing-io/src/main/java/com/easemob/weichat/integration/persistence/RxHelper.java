package com.easemob.weichat.integration.persistence;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author stliu @ apache.org
 */
public class RxHelper {
  
    public static final Func1<ResultSet, Iterable<Row>> resultSetToRow = (ResultSet rs) -> rs;
    
    private RxHelper(){};
    
    public static Observable<Row> toObservable(ResultSetFuture resultSetFuture) {
        return to(resultSetFuture)
                .flatMapIterable(resultSetToRow);
    }

    public static Observable<ResultSet> to(ResultSetFuture resultSetFuture) {
        return Observable.from(resultSetFuture, Schedulers.io());
    }
    
}
