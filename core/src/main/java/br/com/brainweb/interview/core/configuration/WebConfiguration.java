package br.com.brainweb.interview.core.configuration;

import br.com.brainweb.interview.core.toggle.FeatureInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.time.ZoneOffset;
import java.util.TimeZone;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new FeatureInterceptor(env));
    }
}