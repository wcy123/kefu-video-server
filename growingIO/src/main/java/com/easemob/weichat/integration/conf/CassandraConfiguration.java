package com.easemob.weichat.integration.conf;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = {"com.easemob.weichat.integration"})
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${kf.cassandra.contactpoints}")
    private String contactpoints;

    @Value("${kf.cassandra.port}")
    private Integer port;

    @Value("${kf.cassandra.keyspace}")
    private String keyspace;

    @Value("${kf.cassandra.test}")
    private boolean test;

    @Override
    public String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public String getContactPoints() {
        return contactpoints;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        if (test) {
            return SchemaAction.RECREATE_DROP_UNUSED;
        } else {
            return SchemaAction.NONE;
        }
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.easemob.weichat.integration"};
    }

    @Override
    public List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification createKeyspace = CreateKeyspaceSpecification.createKeyspace(keyspace);
        createKeyspace.ifNotExists();
        return Arrays.asList(createKeyspace);
    }

}
