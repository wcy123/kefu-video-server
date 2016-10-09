## 测试环境可以使用下面这个创建keyspace
CREATE KEYSPACE weichat WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

## 生产环境使用下面这个创建keyspace
CREATE KEYSPACE weichat WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': '2'};

## 创建表

CREATE TABLE weichat.growing_visitor_trace (
    tenant_id bigint,
    visitor_id uuid,
    timestamp timestamp,
    attrs map<text, text>,
    growingio_id text,
    type text,
    PRIMARY KEY ((tenant_id, visitor_id), timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC);


CREATE TABLE weichat.service_session_track (
    servicesession_id uuid PRIMARY KEY,
    context text,
    growingio_id text,
    tenant_id bigint,
    visitor_id uuid
);