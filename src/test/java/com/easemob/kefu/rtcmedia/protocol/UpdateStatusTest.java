package com.easemob.kefu.rtcmedia.protocol;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.easemob.kefu.rtcmedia.AbstractRestTest;

/**
 * 测试用例
 *
 * Created by wangchunye on 10/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CreateConferenceTest.Config.class)
@WebAppConfiguration
@ActiveProfiles("CreateConferenceTest")
public class UpdateStatusTest extends AbstractRestTest {


    @Configuration
    @EnableWebMvc
    @ComponentScan("com.easemob.kefu.rtcmedia")
    @Profile("CreateConferenceTest")
    public static class Config {
    }
}
