package com.easemob.weichat.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.easemob.weichat.entity.XdfVisitor;

public interface XdfVisitorRepository extends JpaRepository<XdfVisitor, String>,JpaSpecificationExecutor<XdfVisitor>{
	
}
