package com.easemob.weichat.integration.conf;

import org.springframework.cassandra.core.keyspace.CreateTableSpecification;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraPersistentEntity;
import org.springframework.data.cassandra.mapping.CassandraPersistentProperty;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.util.Assert;

import static org.springframework.cassandra.core.keyspace.CreateTableSpecification.createTable;

/**
 * @author stliu @ apache.org
 */
public class BasicCassandraMappingContextWithClusterKeyOrderingFix extends BasicCassandraMappingContext {
    @Override
    public CreateTableSpecification getCreateTableSpecificationFor(CassandraPersistentEntity<?> entity) {

        Assert.notNull(entity);
        final CreateTableSpecification spec = createTable().name(entity.getTableName());

        entity.doWithProperties(new MyPropertyHandler(spec));

        if (spec.getPartitionKeyColumns().isEmpty()) {
            throw new MappingException("no partition key columns found in the entity " + entity.getType());
        }

        return spec;
    }
    
    class MyPropertyHandler implements PropertyHandler<CassandraPersistentProperty> {
      
      private final CreateTableSpecification spec;
      
      MyPropertyHandler(CreateTableSpecification spec){
        this.spec=spec;
      }
      
      @Override
      public void doWithPersistentProperty(CassandraPersistentProperty prop) {
          if (prop.isCompositePrimaryKey()) {
              CassandraPersistentEntity<?> pkEntity = getPersistentEntity(prop.getRawType());
              pkEntity.doWithProperties(new PropertyHandler<CassandraPersistentProperty>() {
                  @Override
                  public void doWithPersistentProperty(CassandraPersistentProperty pkProp) {
                      if (pkProp.isPartitionKeyColumn()) {
                          spec.partitionKeyColumn(pkProp.getColumnName(), pkProp.getDataType());
                      } else { // it's a cluster column
                          spec.clusteredKeyColumn(pkProp.getColumnName(), pkProp.getDataType(), pkProp.getPrimaryKeyOrdering());
                      }
                  }
              });
          } else {
              if (prop.isIdProperty() || prop.isPartitionKeyColumn()) {
                  spec.partitionKeyColumn(prop.getColumnName(), prop.getDataType());
              } else if (prop.isClusterKeyColumn()) {
                  spec.clusteredKeyColumn(prop.getColumnName(), prop.getDataType(), prop.getPrimaryKeyOrdering());
              } else {
                  spec.column(prop.getColumnName(), prop.getDataType());
              }
          }
      }
  }

}
