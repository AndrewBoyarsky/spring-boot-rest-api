package com.hesky.test.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

import java.util.Map;

/**
 * A Hibernate properties customizer with purpose to add hibernate interceptor to hibernate properties
 */
public class HibernateInterceptorCustomizer implements HibernatePropertiesCustomizer {

    @Autowired
    private HibernateStatisticsInterceptor interceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", interceptor);
    }

}
