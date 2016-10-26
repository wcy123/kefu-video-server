package com.easemob.kefu.video.protocol;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.util.JsonExpectationsHelper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractRestTest implements BeanFactoryAware {
  @Rule
  public JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("target/generated-snippets");
  protected MockMvc mockMvc;
  @Autowired
  protected WebApplicationContext wac;

  // 这个是调试的时候用的
  @SuppressWarnings("unused")
  private BeanFactory beanFactory;

  public static <T> void assertJson(T object, String json) throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    assertJson(object, json, mapper);
  }

  public static <T> void assertJson(T object, String json, ObjectMapper mapper) throws Exception {
    String expectedJson = mapper.writeValueAsString(object);
    new JsonExpectationsHelper().assertJsonEqual(json, expectedJson, true);
    @SuppressWarnings("unchecked")
    T expectedObject = mapper.readValue(json, (Class<T>) object.getClass());
    Assert.assertEquals(object, expectedObject);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(this.wac)
        .apply(documentationConfiguration(this.restDocumentation)).build();
  }

  /**
   * 参考 https://www.tothepoint.company/blog/spring-rest-doc/
   */
  protected static class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    ConstrainedFields(Class<?> input) {
      this.constraintDescriptions = new ConstraintDescriptions(input);
    }

    public FieldDescriptor withPath(String path) {
      return fieldWithPath(path)
          .attributes(key("constraints").value(StringUtils.collectionToDelimitedString(
              this.constraintDescriptions.descriptionsForProperty(path), ". ")));
    }
  }
}
