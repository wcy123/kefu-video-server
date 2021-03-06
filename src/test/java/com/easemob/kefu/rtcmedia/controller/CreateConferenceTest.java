package com.easemob.kefu.rtcmedia.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.easemob.kefu.rtcmedia.AbstractRestTest;
import com.easemob.kefu.rtcmedia.protocol.CreateConference;
import com.easemob.kefu.rtcmedia.data.TestSamples;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * 测试用例
 *
 * Created by wangchunye on 10/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CreateConferenceTest.Config.class)
@WebAppConfiguration
@ActiveProfiles("CreateConferenceTest")
public class CreateConferenceTest extends AbstractRestTest {
  @Test
  public void testCreateConference() throws Exception {
        final ConstrainedFields reqFields = new ConstrainedFields(CreateConference.Request.class);
        final ConstrainedFields resFields = new ConstrainedFields(CreateConference.Response.class);
    String endpoint = "/v1/webrtc/kefu/call";
    String docName = "create_conference_post";
    final String content = TestSamples.createConferenceRequestJson();
    assertJson(TestSamples.createConferenceRequest(), content);
    mockMvc.perform(post(endpoint).contentType(APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(document(docName, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),

            requestFields(
                reqFields.withPath("mediaType").description("客服请求 服务类型：VIDEO，AUDIO"),
                reqFields.withPath("callbackUrl").description("media service 回调 kefu server 的回调地址"),
                reqFields.withPath("callbackArg").description("media service 回调 kefu server 的回调参数"),
                                reqFields.withPath("callbackTimeoutMs").description(
                                        "回调时间, 在 callbackTimeoutMs 毫秒只能, 必须再次回调, 否则 kefu server 认为连接异常"),
                reqFields.withPath("users[]").description("邀请参加会议的人, 内容是Easemob JID, 第一个坐席人员，第二个访客"),
                reqFields.withPath("timestamp").description("时间戳"),
                reqFields.withPath("callExt").description("扩展信息"),
                reqFields.withPath("sign").description("请求参数签名")),
            responseFields(
                    resFields.withPath("sid").description("call 的唯一标识")
            )));
  }

    @Configuration
    @EnableWebMvc
    @ComponentScan("com.easemob.kefu.rtcmedia")
    @Profile("CreateConferenceTest")
    /**
     * see
     * http://stackoverflow.com/questions/10650196/how-to-configure-mappingjacksonhttpmessageconverter-while-using-spring-annotatio
     * for more details
     */
    public static class Config extends WebMvcConfigurerAdapter {
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
}
