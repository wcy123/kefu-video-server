package com.easemob.weichat.integration.rest.mvc.growingio.jpa;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

	

/**
 * @author likai
 * @date 2016年7月4日
 */
@Repository
public interface GrowingTenantRepository extends JpaRepository<GrowingTenantAction, Long> {

	@Query(value = "SELECT o FROM GrowingTenantAction o WHERE o.tenantId = ?1")
	public GrowingTenantAction findByTenantId(Long tenantId);

	@Transactional
    @Modifying
    @Query("update GrowingTenantAction o set o.refreshToken = ?1 WHERE o.tenantId = ?2")
    int updateGrowingRefreshTokenByTenanId(String refreshToken,Long tenantId);
	 
}
