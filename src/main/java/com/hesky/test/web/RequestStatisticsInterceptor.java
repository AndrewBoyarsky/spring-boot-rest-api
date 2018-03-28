package com.hesky.test.web;

import com.hesky.test.hibernate.HibernateStatisticsInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Request interceptor that print logs with request time, url, method(GET,POST) and number of db queries
 */
public class RequestStatisticsInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestStatisticsInterceptor.class);
    private ThreadLocal<Long> time = new ThreadLocal<>();
    //Using a Hibernate interceptor for counting db queries
    @Autowired
    private HibernateStatisticsInterceptor statisticsInterceptor;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        time.set(System.currentTimeMillis());
        statisticsInterceptor.startCounter();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long duration = System.currentTimeMillis() - time.get();
        Long queryCount = statisticsInterceptor.getQueryCount();
        statisticsInterceptor.clearCounter();
        time.remove();
        log.info("Time: {} ms, Queries: {}, {} {}", duration, queryCount, request.getMethod(), request.getRequestURI());
    }

//    @Override
//    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        concurrent handling cannot be supported here
//        statisticsInterceptor.clearCounter();
//        time.remove();
//    }
}