package com.hesky.test;

import com.hesky.test.hibernate.HibernateInterceptorCustomizer;
import com.hesky.test.hibernate.HibernateStatisticsInterceptor;
import com.hesky.test.web.RequestStatisticsInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@EnableAutoConfiguration
//@ComponentScan
//@Configuration
@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    //could be @Component, but for disabling failing of WebMvcTest  was annotated as @Bean here for simplicity
    @Bean
    public HibernateStatisticsInterceptor hibernateStatisticsInterceptor() {
        return new HibernateStatisticsInterceptor();
    }

    //could be @Component, but for disabling failing of WebMvcTest  was annotated as @Bean here for simplicity
    @Bean
    public HibernateInterceptorCustomizer hibernateInterceptorCustomizer() {
        return new HibernateInterceptorCustomizer();
    }
    //Adding interceptor for logging db calls count and request time
    @Configuration
    public static class WebApplicationConfig extends WebMvcConfigurationSupport {

        @Autowired
        private RequestStatisticsInterceptor requestStatisticsInterceptor;

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