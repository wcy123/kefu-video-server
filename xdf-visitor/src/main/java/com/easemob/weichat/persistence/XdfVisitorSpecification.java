package com.easemob.weichat.persistence;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import com.easemob.weichat.data.XdfVisitorQuery;
import com.easemob.weichat.entity.XdfVisitor;

public class XdfVisitorSpecification implements Specification<XdfVisitor>{

	private XdfVisitorQuery visitorQuery;
	
	public XdfVisitorSpecification(XdfVisitorQuery visitorQuery){
		this.visitorQuery=visitorQuery;
	}
	
	@Override
	public Predicate toPredicate(Root<XdfVisitor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> andPredicates = new ArrayList<>();
		if(visitorQuery.getTenantId()!=null){
			Predicate pre=cb.equal(root.get("tenantId"),visitorQuery.getTenantId());
			andPredicates.add(pre);
		}
		if(visitorQuery.getBeginDate()!=null){
			Predicate pre=cb.greaterThanOrEqualTo(root.get("createDateTime"), visitorQuery.getBeginDate());
			andPredicates.add(pre);
		}
		if(visitorQuery.getEndDate()!=null){
			Predicate pre=cb.lessThanOrEqualTo(root.get("createDateTime"), visitorQuery.getEndDate());
			andPredicates.add(pre);
		}
		if(!StringUtils.isEmpty(visitorQuery.getVisitorName())){
			Predicate pre1=cb.like(root.get("trueName"),"%"+visitorQuery.getVisitorName()+"%");
			Predicate pre2=cb.like(root.get("nicename"),"%"+visitorQuery.getVisitorName()+"%");
			andPredicates.add(cb.or(pre1,pre2));
		}
		
		return cb.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
	}


}
