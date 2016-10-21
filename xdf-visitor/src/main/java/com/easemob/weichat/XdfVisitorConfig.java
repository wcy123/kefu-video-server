package com.easemob.weichat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.easemob.weichat.client.IWebappReadWriteClient;
import com.easemob.weichat.models.entity.UserExportFile;
import com.easemob.weichat.service.util.AbstractUserFileExporter;

@Configuration
@EnableWebMvc
@ComponentScan
public class XdfVisitorConfig extends WebMvcConfigurerAdapter{

	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	        "classpath:/META-INF/resources/", "classpath:/resources/",
	        "classpath:/static/", "classpath:/public/" };

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
	
	@Autowired
    private IWebappReadWriteClient webappReadWriteClient;
    @Bean
    public AbstractUserFileExporter userFileExporter() {
       return new AbstractUserFileExporter() {
          @Override
          protected UserExportFile saveFileExportData(UserExportFile userExportFile) {
            return webappReadWriteClient.saveUserExportFile(String.valueOf(userExportFile.getTenantId()), 
                userExportFile.getAgentUser().getUserId(), userExportFile);
          }
       };
    }
	
}
