package com.easemob.kefu.video.protocol;

import com.easemob.kefu.video.SampleData.TestSamples;

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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  @Test
  public void testCreateConference() throws Exception {
    final AbstractRestTest.ConstrainedFields reqFields = new AbstractRestTest.ConstrainedFields(CreateConference.Request.class);
    final AbstractRestTest.ConstrainedFields resFields = new AbstractRestTest.ConstrainedFields(CreateConference.Response.class);
    String endpoint = "/callback/kefu/call/{sid}";
    String docName = "update_conference_post";
    final String content = TestSamples.updateStatusResponseJson();
    assertJson(TestSamples.updateStatusRequest(), content);
    mockMvc.perform(post(endpoint, "1234567890").contentType(APPLICATION_JSON).content(content))
            .andExpect(status().isOk())
            .andDo(document(docName, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                    pathParameters(parameterWithName("sid").description("会议唯一标识")),
                    requestFields(
                            reqFields.withPath("state").description("会议状态"),
                            reqFields.withPath("callbackArg").description("回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来")),
                    responseFields(resFields.withPath("status").description(String.join(",",
                            String.valueOf(UpdateStatus.State.INIT),
                            String.valueOf(UpdateStatus.State.CREATED),
                            String.valueOf(UpdateStatus.State.RINGING),
                            String.valueOf(UpdateStatus.State.TERMINATED),
                            String.valueOf(UpdateStatus.State.ABORTED))))));
  }


  @Configuration
  @EnableWebMvc
  @ComponentScan("com.easemob.kefu.video")
  @Profile("CreateConferenceTest")
  public static class Config {
  }
}
