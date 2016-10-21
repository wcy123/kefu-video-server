package com.easemob.weichat.service;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.easemob.weichat.conf.XdfVisitorCsvConfig;
import com.easemob.weichat.data.XdfVisitorQuery;
import com.easemob.weichat.entity.XdfVisitor;
import com.easemob.weichat.persistence.XdfVisitorRepository;
import com.easemob.weichat.persistence.XdfVisitorSpecification;
import com.easemob.weichat.service.storage.IExportFileDataSource;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class XdfVisitorFileDataSearcher implements IExportFileDataSource {

	private XdfVisitorQuery query;
	
    private XdfVisitorRepository repository;

	
	@Override
	public List<?> search(int page, int size) {
		XdfVisitorSpecification spc=new XdfVisitorSpecification(query);
        Pageable pageRequest = new PageRequest(page - 1, size);
        List<XdfVisitor> data=repository.findAll(spc,pageRequest).getContent();
		for(XdfVisitor visitor:data){
			visitor.setSexStr(XdfVisitorCsvConfig.getVisitorSex(visitor.getSex()));//设置性别
		}
		return data;
	}

	@Override
	public Object getQuery() {
		return query;
	}

	@Override
	public List<String> getCsvColumnNames() {
		return XdfVisitorCsvConfig.getColumnName();
	}

	@Override
	public List<String> getCsvFiledNames() {
		return XdfVisitorCsvConfig.getFieldName();
	}

	@Override
	public String getFileName() {
		return XdfVisitorCsvConfig.getCsvFileName(query.getBeginDate(),query.getEndDate());
	}

}
