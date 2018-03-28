package com.hesky.test.hibernate;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A hibernate interceptor that counts number of queries to database
 */
public class HibernateStatisticsInterceptor extends EmptyInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateStatisticsInterceptor.class);

    private ThreadLocal<Long> queryCount = new ThreadLocal<>();

    public void startCounter() {
        queryCount.set(0L);
    }

    public Long getQueryCount() {
        return queryCount.get();
    }

    public void clearCounter() {
        queryCount.remove();
    }

    @Override
    public String onPrepareStatement(String sql) {
        Long count = getQueryCount();
        if (count != null) {
            queryCount.set(count + 1);
        }
        LOG.info(sql);
        return super.onPrepareStatement(sql);
    }
}
