package com.easemob.weichat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.easemob.weichat.data.XdfVisitorQuery;
import com.easemob.weichat.entity.XdfVisitor;
import com.easemob.weichat.models.data.ApiResponse;
import com.easemob.weichat.persistence.XdfVisitorRepository;
import com.easemob.weichat.persistence.XdfVisitorSpecification;
import com.easemob.weichat.service.XdfVisitorFileDataSearcher;
import com.easemob.weichat.service.util.AbstractUserFileExporter;

@Controller
public class XdfVisitorController {
	
	@Autowired
	private XdfVisitorRepository repository;
	
	@Autowired
	private AbstractUserFileExporter userFileExporter;
	
    @RequestMapping(value = "/tenants/{tenantId}/xdfvisitorusers", method = RequestMethod.GET)
    public @ResponseBody Page<XdfVisitor> getVisitors(@PathVariable("tenantId") Integer tenantId,
        XdfVisitorQuery query, Pageable page) {
        query.setTenantId(tenantId);
        XdfVisitorSpecification spc = new XdfVisitorSpecification(query);
    
        return repository.findAll(spc, page);
    }
	
	@RequestMapping(value="/tenants/{tenantId}/agents/{agentUserId}/xdfvisitorusers",method=RequestMethod.POST)
    public @ResponseBody ApiResponse getVisitors(@PathVariable("tenantId") Integer tenantId,
            @PathVariable("agentUserId") String agentUserId,
            XdfVisitorQuery query){
        query.setTenantId(tenantId);
        
        XdfVisitorFileDataSearcher dataSearcher=new XdfVisitorFileDataSearcher(query,repository);
        userFileExporter.createExportFile(dataSearcher, tenantId, agentUserId, query.getFileEncoding());
        ApiResponse response=new ApiResponse();
        response.setStatus(ApiResponse.STATUS_OK);
        return response;    
    }

	/**
	 * 直接使用spring data rest在userId不存在时会报404，因此重写接口
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/xdfVisitors/{userId}",method=RequestMethod.GET)
	public @ResponseBody XdfVisitor getOne(@PathVariable("userId")String userId){
		return repository.findOne(userId);
	}
	
}
	

