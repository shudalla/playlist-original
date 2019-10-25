package br.com.shu.playlist.controller;

import java.lang.reflect.Type;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.shu.playlist.component.Dictionary;
import br.com.shu.playlist.controller.advice.ControllerAdvice;

@RunWith(MockitoJUnitRunner.Silent.class)
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    protected MockHttpServletResponse response;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:internationalization/messages_en");
        messageSource.setDefaultEncoding("UTF-8");
        ReflectionTestUtils.setField(Dictionary.class, "messageSource", messageSource);
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controllerClass())
                .setControllerAdvice(new ControllerAdvice())
                .build();
    }

    protected <T> T stringJsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    protected <T> List<T> stringJsonToList(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, new TypeReference<List<T>>() {
                @Override
                public Type getType() {
                    return mapper.getTypeFactory().constructCollectionType(List.class, clazz);
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    protected String objectToStringJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    /**
     * @return the controller to be tested (with @InjectMocks annotation)
     */
    protected abstract Object controllerClass();

}