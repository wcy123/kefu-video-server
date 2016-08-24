package com.easemob.weichat.integration.rest.mvc.growingio.jpa;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.easemob.weichat.integration.rest.mvc.growingio.jpa.entity.GrowingIoCompanyAction;

	

/**
 * @author likai
 * @date 2016年7月4日
 */
@Repository
public interface GrowingIoCompanyRepository extends JpaRepository<GrowingIoCompanyAction,Long> {

	@Query(value = "SELECT o FROM GrowingIoCompanyAction o WHERE o.tenantId = ?1")
	public GrowingIoCompanyAction findByTenantId(int tenantId);

	@Transactional
    @Modifying
    @Query("update GrowingIoCompanyAction o set o.refreshToken = ?1 WHERE o.tenantId = ?2")
    int updateGrowingRefreshTokenByTenanId(String refreshToken, int tenantId);
	 
}
