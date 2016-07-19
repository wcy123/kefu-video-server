package com.easemob.weichat.integration.rest.mvc.growingio.jpa;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

	

/**
 * @author likai
 * @date 2016年7月4日
 */
@Repository
public interface AgentUserRepository extends JpaRepository<AgentUserAction, String> {
	@Query("select a from AgentUserAction a where a.tenantId = ?1")
	List<AgentUserAction> findByTenantId(int tenantId);
}
