package com.hesky.test;

import com.hesky.test.hibernate.HibernateInterceptorCustomizer;
import com.hesky.test.hibernate.HibernateStatisticsInterceptor;
import com.hesky.test.web.RequestStatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig {
    @Bean
    public HibernateStatisticsInterceptor hibernateStatisticsInterceptor() {
        return new HibernateStatisticsInterceptor();
    }

    @Bean
    public HibernateInterceptorCustomizer hibernateInterceptorCustomizer() {
        return new HibernateInterceptorCustomizer();
    }

    @Configuration
    public static class WebApplicationConfig extends WebMvcConfigurationSupport {

        private final RequestStatisticsInterceptor requestStatisticsInterceptor;

        @Autowired
        public WebApplicationConfig(RequestStatisticsInterceptor requestStatisticsInterceptor) {this.requestStatisticsInterceptor = requestStatisticsInterceptor;}

        @Bean
        public RequestStatisticsInterceptor requestStatisticsInterceptor() {
            return new RequestStatisticsInterceptor();
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(requestStatisticsInterceptor).addPathPatterns("/**");
        }

    }
}