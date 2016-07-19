package com.easemob.weichat.integration.rest.mvc.growingio.jpa;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

	

/**
 * @author likai
 * @date 2016年7月4日
 */
@Repository
public interface GrowingUserRepository extends JpaRepository<GrowingUserAction, String> {
	@Query("select a from GrowingUserAction a where a.userName = ?1")
	GrowingUserAction findByUserName(String userName);
}
