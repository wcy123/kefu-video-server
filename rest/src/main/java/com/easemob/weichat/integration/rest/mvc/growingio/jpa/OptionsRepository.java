package com.easemob.weichat.integration.rest.mvc.growingio.jpa;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.OptionAction;

	

/**
 * @author likai
 * @date 2016年7月4日
 */
@Repository
public interface OptionsRepository extends JpaRepository<OptionAction,Long> {

	@Query(value = "SELECT o FROM OptionAction o WHERE o.tenantId = ?0 and o.optionName=?1")
	public OptionAction findByTenantId(int tenantId,String optionName);

	 
}
