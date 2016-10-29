package com.easemob.kefu.rtcmedia.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
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

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.easemob.kefu.rtcmedia.AbstractRestTest;
import com.easemob.kefu.rtcmedia.protocol.AgentCreateConference;
import com.easemob.kefu.rtcmedia.protocol.AgentJid;
import com.easemob.kefu.rtcmedia.protocol.GetStatus;
import com.easemob.kefu.rtcmedia.protocol.UpdateStatus;
import com.easemob.kefu.rtcmedia.data.TestSamples;

/**
 * 测试用例
 *
 * Created by wangchunye on 10/27/16. eclipse
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VideoServerControllerTest.Config.class)
@WebAppConfiguration
@ActiveProfiles("VideoServerControllerTest")
public class VideoServerControllerTest extends AbstractRestTest {

    @Test
    public void updateStatus() throws Exception {
        final AbstractRestTest.ConstrainedFields reqFields =
                new AbstractRestTest.ConstrainedFields(
                        UpdateStatus.Request.class);
        final AbstractRestTest.ConstrainedFields resFields =
                new AbstractRestTest.ConstrainedFields(
                        UpdateStatus.Response.class);
        String endpoint = "/v1/rtcmedia/conference/{sid}";
        String docName = "update_conference_post";
        final String content = TestSamples.updateStatusResponseJson();
        final String states =
                TestSamples.getStates();
        assertJson(TestSamples.updateStatusRequest(), content);
        mockMvc.perform(post(endpoint, "1234567890").contentType(APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andDo(document(docName, preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("sid").description("会议唯一标识")),
                        requestFields(
                                resFields.withPath("state").description(states),
                                reqFields.withPath("callbackArg")
                                        .description(
                                                "下一次回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来")),
                        responseFields(
                                resFields.withPath("callbackUrl")
                                        .description("media service 回调 kefu server 的回调地址"),
                                reqFields.withPath("callbackArg")
                                        .description("回调参数, media service 不用关心这个, 怎么送过去, 怎么还回来"),
                                resFields.withPath("callbackTimeoutMs").description(
                                        "回调时间, 在 callbackTimeoutMs 毫秒只能, 必须再次回调, 否则 kefu server 认为连接异常"))));
    }

    @Test
    public void getJid() throws Exception {
        String endpoint = "/v1/rtcmedia/{agentName}/jid";
        String docName = "get_jid_get";
        final AbstractRestTest.ConstrainedFields resFields =
                new AbstractRestTest.ConstrainedFields(
                        AgentJid.Response.class);
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint, "kefu@easemob.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document(docName, preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("agentName").description("坐席登陆名称, 一般是电子邮件地址")),
                        responseFields(
                                resFields.withPath("orgName").description("JID 里面的组织名"),
                                resFields.withPath("appName").description("JID 里面的应用名"),
                                resFields.withPath("userName").description("JID 里面的用户名"))));
    }

    @Test
    public void createConference() throws Exception {
        final ConstrainedFields reqFields = new ConstrainedFields(
                AgentCreateConference.Request.class);
        final ConstrainedFields resFields = new ConstrainedFields(
                AgentCreateConference.Response.class);

        String endpoint = "/v1/rtcmedia/conferences";
        String docName = "agent_create_conference";
        String content = TestSamples.agentCreateConferenceRequestJson();
        mockMvc.perform(
                RestDocumentationRequestBuilders.post(endpoint)
                        .content(content).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document(docName,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                reqFields.withPath("agentId").description("坐席 ID"),
                                reqFields.withPath("mediaType")
                                        .description("客服请求 服务类型：VIDEO，AUDIO"),
                                reqFields.withPath("msgId").description(" msg id 是哪一条消息触发的这次呼叫"),
                                reqFields.withPath("orgName").description("JID 里面的组织名"),
                                reqFields.withPath("appName").description("JID 里面的应用名"),
                                reqFields.withPath("visitorUserName").description("JID 中的访客用户名"),
                                reqFields.withPath("agentUserName").description(
                                        "JID 中的坐席名称, 如果坐席本地没有缓存 cookie ,那么利用 getJID 接口获取")),
                        responseFields(
                                resFields.withPath("sid")
                                        .description("call 的唯一标识, 目前前段应该不用这个字段, "))));
    }

    @Test
    public void getStatus() throws Exception {
        final ConstrainedFields resFields = new ConstrainedFields(
                GetStatus.Response.class);
        String endpoint = "/v1/rtcmedia/conference/{msgId}";
        String docName = "agent_get_status";
        String content = TestSamples.agentCreateConferenceRequestJson();
        final String states =
                TestSamples.getStates();
        mockMvc.perform(
                RestDocumentationRequestBuilders.get(endpoint,
                        UUID.fromString("3f9ce5b9-a979-4088-b47f-df23f37094e8")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentation.document(docName,
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("msgId").description(
                                "会议唯一标识,  一个会议既可以用 sid 表示(客服和media service 之间的协议), 也可以用 msgId 表示(客服和坐席前段之间协议)")),
                        responseFields(
                                resFields.withPath("state").description(states))));
    }

    @Configuration
    @EnableWebMvc
    @ComponentScan("com.easemob.kefu.rtcmedia")
    @Profile("VideoServerControllerTest")
    public static class Config {
    }
}
