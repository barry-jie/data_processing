package com.changhong.db;

import com.changhong.db.arango.ArangoEntityHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * User: lj
 */
public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
    private static final String REGISTER_MONGO_TEMPLATE_KEY = "mongoTemplate";
    private static final String REGISTER_REDIS_TEMPLATE_KEY = "redisTemplate";
    private static final String REGISTER_ES_DOMAIN_PARSE = "domainParse";
    private static final String REGISTER_ARANGO_KEY = "arangoEntity";
    public static ArangoEntityHelper getArangoDbEntity() {
        return getBean(REGISTER_ARANGO_KEY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        Object object = null;
        if (applicationContext != null) {
            object = applicationContext.getBean(name);
        }
        return (T) object;
    }

    public static <T extends Annotation> Map<String, Object> getBeansWithAnnotation(Class<T> t) {
        return applicationContext.getBeansWithAnnotation(t);
    }

    /**
     * 获取MongodbTemplate
     *
     * @return MongoOperations
     */
    public static MongoTemplate getMongoDBTemplate() {
        return getBean(REGISTER_MONGO_TEMPLATE_KEY);
    }


    public static RedisTemplate getRedisTemplate() {
        return getBean(REGISTER_REDIS_TEMPLATE_KEY);
    }

}
