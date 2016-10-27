package com.easemob.kefu.video.protocol;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

import com.easemob.kefu.video.protocol.get.jid.Response;

/**
 * 测试用例
 *
 * Created by wangchunye on 10/27/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GetJIDTest.Config.class)
@WebAppConfiguration
@ActiveProfiles("GetJIDTest")
public class GetJIDTest extends AbstractRestTest {
    @Test
    public void getJid() throws Exception {
        String endpoint = "/v1/video/{agentName}/jid";
        String docName = "get_jid_get";
        final AbstractRestTest.ConstrainedFields resFields =
                new AbstractRestTest.ConstrainedFields(Response.class);
        mockMvc.perform(
                get(endpoint, "kefu@easemob.com"))
                .andExpect(status().isOk())
                .andDo(document(docName, preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("agentName").description("坐席登陆名称, 一般是电子邮件地址")),
                        responseFields(resFields.withPath("orgName").description("JID 里面的组织名"),
                                resFields.withPath("appName").description("JID 里面的应用名"),
                                resFields.withPath("userName").description("JID 里面的用户名"))));
    }

    @Configuration
    @EnableWebMvc
    @ComponentScan("com.easemob.kefu.video")
    @Profile("GetJIDTest")
    public static class Config {
    }
}
