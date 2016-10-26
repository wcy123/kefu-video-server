package com.easemob.kefu.video.protocol;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.easemob.kefu.video.SampleData.TestSamples;

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
    String endpoint = "/v1/video/conferences";
    String docName = "create_conference_post";
    final String content = TestSamples.createConferenceRequestJson();
    assertJson(TestSamples.createConferenceRequest(), content);
    mockMvc.perform(post(endpoint).contentType(APPLICATION_JSON).content(content))
        .andExpect(status().isOk())
        .andDo(document(docName, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            requestFields(
                reqFields.withPath("callbackUrl").description("media server 回调 kefu server 的回调地址"),
                reqFields.withPath("callbackArg").description("media server 回调 kefu server 的回调参数"),
                reqFields.withPath("users[]").description("邀请参加会议的人, 内容是Easemob JID"),
                reqFields.withPath("timestamp").description("时间戳"),
                reqFields.withPath("callExt").description("扩展信息")),
            responseFields(resFields.withPath("sid").description("video conference 的唯一标识"))));
  }


  @Configuration
  @EnableWebMvc
  @ComponentScan("com.easemob.kefu.video")
  @Profile("CreateConferenceTest")
  public static class Config {
  }
}
