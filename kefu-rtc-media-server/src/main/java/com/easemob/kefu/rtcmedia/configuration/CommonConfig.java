package com.easemob.kefu.rtcmedia.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * see
 * http://stackoverflow.com/questions/10650196/how-to-configure-mappingjacksonhttpmessageconverter-while-using-spring-annotatio
 * for more details
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.easemob.kefu.rtcmedia")
public class CommonConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }

    @Bean
    MappingJackson2HttpMessageConverter converter() {
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper().registerModule(new GuavaModule());
        return converter;
    }
}
