package com.easemob.weichat.integration.cassandra;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.jgroups.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.easemob.weichat.integration.IntegrationTestApplication;
import com.easemob.weichat.integration.modes.GrowingIoInfo;
import com.easemob.weichat.integration.modes.IntgerationGrowingInfo;
import com.easemob.weichat.integration.rest.mvc.growingio.service.IGrowingService;
import com.easemob.weichat.models.util.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationTestApplication.class)
@WebAppConfiguration
public class CassandraTest {

	@Autowired
	private IGrowingService growingService ; 
	
	@Test
	public void test() {
		// 将复杂的json结构体 转换为 带有Tree结构的数据库表结构
		String json = ReadFile("/Users/tianxiayouxue/Downloads/sample.json");
	    try {
	    	JsonNode node = JSONUtil.getObjectMapper().readTree(json);
	    	if(node!=null){
	    		
	    		IntgerationGrowingInfo info = new  IntgerationGrowingInfo();
	    		info.setTenantId(1);
	    		info.setGrowingioId("growing"+UUID.randomUUID().toString());
	    		info.setUserId("userid"+UUID.randomUUID().toString());
	    		List<GrowingIoInfo> array = JSONUtil.getObjectMapper().readValue(json, new TypeReference<List<GrowingIoInfo>>(){});
        		if(array!=null&&array.size() > 0){
        			growingService.processData(array,info);
        		}
	    		
	    	}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String ReadFile(String Path){
		BufferedReader reader = null;
		String laststr = "";
		try{
				FileInputStream fileInputStream = new FileInputStream(Path);
				InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String tempString = null;
				while((tempString = reader.readLine()) != null){
					laststr += tempString;
				}
				reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
			return laststr;
		}				

}
