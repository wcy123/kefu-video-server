package com.easemob.kefu.rtcmedia;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.easemob.kefu.rtcmedia.data.TestSamples;
import com.easemob.kefu.rtcmedia.dep.MediaServiceApi;
import com.easemob.kefu.rtcmedia.dep.WebAppApi;
import com.easemob.kefu.rtcmedia.model.RtcMediaEntity;
import com.easemob.kefu.rtcmedia.model.RtcMediaRepository;
import com.easemob.kefu.rtcmedia.protocol.types.State;

/**
 * 测试用例
 *
 * Created by wangchunye on 10/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RtcServerControllerTest.Config.class)
@WebAppConfiguration
@ActiveProfiles("VideoServerControllerTest")
public class RtcServerControllerTest extends AbstractRestTest {
    @Autowired
    RtcMediaRepository rtcMediaRepository;

    @Test
    public void getJid() throws Exception {
        String endpoint = "/v1/rtcmedia/{agentName}/jid";
        // 首次运行, 创建数据库里面的值
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint, "kefu@easemob.com")
                        .param("orgName", TestSamples.ORG_NAME)
                        .param("appName", TestSamples.APP_NAME)
                        .param("imServiceNumber", TestSamples.AGENT_USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // 再次运行, 得到数据库里面的值
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint, "kefu@easemob.com")
                        .param("orgName", TestSamples.ORG_NAME)
                        .param("appName", TestSamples.APP_NAME)
                        .param("imServiceNumber", TestSamples.AGENT_USER_NAME))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createConference() throws Exception {
        String endpoint = "/v1/rtcmedia/conferences";
        String content = TestSamples.agentCreateConferenceRequestJson();
        mockMvc.perform(
                RestDocumentationRequestBuilders.post(endpoint)
                        .content(content).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateStatus() throws Exception {
        String endpoint = "/v1/rtcmedia/conference/{msgId}";
        final String content = TestSamples.updateStatusResponseJson();
        assertJson(TestSamples.updateStatusRequest(), content);
        final String msgId = "xxx";
        mockMvc.perform(post(endpoint, msgId).contentType(APPLICATION_JSON).content(content))
                .andExpect(status().isNotFound());

        rtcMediaRepository.save(RtcMediaEntity.builder()
                .msgId(msgId)
                .created(new Date(System.currentTimeMillis()))
                .state(State.INIT)
                .lastModified(new Date(System.currentTimeMillis()))
                .build());

        mockMvc.perform(post(endpoint, msgId).contentType(APPLICATION_JSON).content(content))
                .andExpect(status().isOk());

    }

    @Test
    public void getStatus() throws Exception {
        String endpoint = "/v1/rtcmedia/conference/{msgId}";
        final String msgId = "ab082532-c410-4a4d-93eb-2b92397b46e9";
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint,
                        msgId))
                .andExpect(status().isNotFound());

        rtcMediaRepository.save(RtcMediaEntity.builder()
                .msgId(msgId.toString())
                .created(new Date(System.currentTimeMillis()))
                .state(State.INIT)
                .lastModified(new Date(System.currentTimeMillis()))
                .build());

        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint,
                        msgId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Configuration
    @ComponentScan("com.easemob.kefu.rtcmedia")
    @Profile("VideoServerControllerTest")
    public static class Config {
        @Bean
        public MediaServiceApi mediaServiceApi() {
            final MediaServiceApi mock = Mockito.mock(MediaServiceApi.class);
            when(mock.createConference(any())).thenReturn(TestSamples.createConferenceResponse());
            return mock;
        }

        @Bean
        public WebAppApi webAppApi() {
            final WebAppApi mock = Mockito.mock(WebAppApi.class);
            when(mock.registerImUser(any(), any(), any())).thenReturn(
                    new WebAppApi.ResponseRegisterImUser("a2", "1234"));
            return mock;
        }
    }
}
