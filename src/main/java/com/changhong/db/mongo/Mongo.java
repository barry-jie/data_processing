package com.changhong.db.mongo;


import com.changhong.db.SpringApplicationContext;
import com.changhong.util.RegexUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;


public class Mongo extends MongoQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mongo.class);

    private static final String ID = "uuid";

    protected MongoTemplate mongoOperations;

    private Mongo(String source) {
        super();
        mongoOperations = SpringApplicationContext.getBean(source);
    }

    public Mongo() {
        this.mongoOperations = SpringApplicationContext.getMongoDBTemplate();
    }

    public Mongo(MongoTemplate mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public List list(Class clazz) {
        return mongoOperations.find(query, clazz, collectionName(clazz));
    }

    public List<GeoResult> geoFind(Double lat, Double lng, Class clazz, String collectionName) {
        NearQuery nearQuery = NearQuery.near(lng, lat, Metrics.KILOMETERS);
        nearQuery.maxDistance(10);
        nearQuery.query(query);
        nearQuery.spherical(true);
        nearQuery.skip(query.getSkip());
        nearQuery.num(query.getSkip() + query.getLimit());
        GeoResults geoResults = mongoOperations.geoNear(nearQuery, clazz, collectionName);
        return geoResults.getContent();
    }

    public static Mongo buildMongo() {
        return new Mongo();
    }

    public static Mongo buildMongo(String source) {
        return new Mongo(source);
    }

    public Mongo arrayEqAll(String property, String... values) {
        criteria.and(property).all(values);
        return this;
    }

    public Mongo multiEq(String property, Object... values) {
        Criteria[] criterias = new Criteria[values.length];
        int index = 0;
        for (Object str : values) {
            criterias[index] = Criteria.where(property).is(str);
            index++;
        }
        criteria.andOperator(criterias);
        return this;
    }

    public Mongo hint(String indexName) {
        query.withHint(indexName);
        return this;
    }


    public Mongo eq(String key, Object value) {
        and(key).is(value);
        return this;
    }

    public Mongo fuzzy(String key, Object value) {
        String re = String.valueOf(value);
        re = RegexUtil.compile(re);
        and(key).regex(re, "i");
        return this;
    }


    public Mongo ne(String key, Object value) {
        and(key).ne(value);
        return this;
    }

    public Mongo or(String key, Object... value) {
        criteria.orOperator(genOrCriteria(key, value));
        return this;
    }

    public Mongo or(String[] key, Object... value) {
        criteria.orOperator(genOrCriteria(key, value));
        return this;
    }

    public Mongo or(String[] key, Object[] value, String[] operators) {
        criteria.orOperator(genOrCriteria(key, value, operators));
        return this;
    }

    public Mongo or(Criteria... criteria) {
        this.criteria.orOperator(criteria);
        return this;
    }

    private Criteria[] genOrCriteria(String key, Object[] value) {
        Criteria[] criterias = new Criteria[value.length];
        for (int i = 0; i < value.length; i++) {
            criterias[i] = Criteria.where(key).is(value[i]);
        }
        return criterias;
    }

    private Criteria[] genOrCriteria(String[] key, Object[] value) {
        Criteria[] criterias = new Criteria[value.length];
        for (int i = 0; i < value.length; i++) {
            criterias[i] = Criteria.where(key[i]).is(value[i]);
        }
        return criterias;
    }

    private Criteria[] genOrCriteria(String[] key, Object[] value, String[] operators) {
        Criteria[] criterias = new Criteria[value.length];
        for (int i = 0; i < value.length; i++) {
            if ("in".equals(operators[i])) {
                Object[] tempStr = (Object[]) value[i];
                criterias[i] = Criteria.where(key[i]).in(tempStr);
            }
            if ("f".equals(operators[i])) {
                String re = String.valueOf(value[i]);
                re = re.replace("(", "\\(").replace(")", "\\)");
                criterias[i] = Criteria.where(key[i]).regex(re, "i");
            }
            if ("eq".equals(operators[i])) {
                criterias[i] = Criteria.where(key[i]).is(value[i]);
            }
            if ("gte".equals(operators[i])) {
                criterias[i] = Criteria.where(key[i]).gte(value[i]);
            }
            if ("ne".equals(operators[i])) {
                criterias[i] = Criteria.where(key[i]).ne(value[i]);
            }
        }
        return criterias;
    }

    public Mongo gte(String key, Object value) {
        and(key).gte(value);
        return this;
    }

    public Mongo gt(String key, Object value) {
        and(key).gt(value);
        return this;
    }

    public Mongo lt(String key, Object value) {
        and(key).lt(value);
        return this;
    }

    public Mongo lte(String key, Object value) {
        and(key).lte(value);
        return this;
    }


    public Mongo in(String key, Object... value) {
        and(key).in(value);
        return this;
    }

    public Mongo nin(String key, Object... value) {
        and(key).nin(value);
        return this;
    }

    public Mongo size(String key, int size) {
        and(key).size(size);
        return this;
    }

    public Mongo exists(String key, boolean flag) {
        and(key).exists(flag);
        return this;
    }

    public Mongo limit(int limit, int page) {
        if (page < 1) {
            throw new RuntimeException("page is invalid ...");
        }
        query.limit(limit);
        query.skip((page - 1) * limit);
        return this;
    }

    public Mongo limitSkip(int limit, int skip) {
        if (skip < 0) {
            throw new RuntimeException("skip is invalid ...");
        }
        query.limit(limit);
        query.skip(skip);
        return this;
    }

    //查询返回指定字段 wq 2016.5.19, 必须在sort之前
    public Mongo fields(String... properties) {
        DBObject dbObject = new BasicDBObject();
        DBObject fieldObject = new BasicDBObject();
        for (String propertie : properties) {
            fieldObject.put(propertie, true);
        }
        Query _query = new BasicQuery(dbObject, fieldObject);
        _query.addCriteria(criteria);
        _query.limit(query.getLimit()).skip(query.getSkip());
        query = _query;
        return this;
    }

    public Mongo desc(String... properties) {
        query.with(new Sort(Sort.Direction.DESC, properties));
        return this;
    }

    public Mongo asc(String... properties) {
        query.with(new Sort(Sort.Direction.ASC, properties));
        return this;
    }

    public Mongo type(String key, int type) {
        and(key).type(type);
        return this;
    }

    public <T> T load(String uuid, Class<T> clazz, String collectionName) {
        return eq(ID, uuid).findOne(clazz, collectionName);
    }

    public enum MongoType {
        UPDATE, INSERT
    }

    private String collectionName(Object obj) {
        Class clazz = obj.getClass();
        return collectionName(clazz);
    }

    private String collectionName(Class clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = (Table) clazz.getAnnotation(Table.class);
            return table.name();
        } else {
            return clazz.getSimpleName();
        }
    }

    public MongoType insert(IMongoUpdate update, Object obj) {
        return insert(update, obj, collectionName(obj));
    }


    public interface Call {
        void callFind();

        void callNotFind();
    }

    public MongoType insert(IMongoUpdate update, Object obj, Call call) {
        String collectionName = collectionName(obj);
        if (count(collectionName) > 0) {
            if (update != null) {
                updateFirst(update, collectionName);
            } else {
                call.callFind();
            }
            return MongoType.UPDATE;
        } else {
            insert(obj, collectionName);
            call.callNotFind();
            return MongoType.INSERT;
        }
    }

    public MongoType insert(IMongoUpdate update, Object obj, String collectionName) {
        if (count(collectionName) > 0) {
            if (update != null) {
                updateFirst(update, collectionName);
            }
            return MongoType.UPDATE;
        } else {
            insert(obj, collectionName);
            return MongoType.INSERT;
        }
    }

    public WriteResult upsert(IMongoUpdate update, String collectionName) {
        Update update1 = new Update();
        update.update(update1);
        return mongoOperations.upsert(query, update1, collectionName);
    }

    public WriteResult updateMulti(IMongoUpdate update, String collectionName) {
        Update update1 = new Update();
        update.update(update1);
        return mongoOperations.updateMulti(query, update1, collectionName);
    }

    public void updateFirst(IMongoUpdate update, String collectionName) {
        Update update1 = new Update();
        update.update(update1);
        mongoOperations.updateFirst(query, update1, collectionName);
    }

    public void updateFirst(IMongoUpdate update, Object obj) {
        Update update1 = new Update();
        update.update(update1);
        mongoOperations.updateFirst(query, update1, collectionName(obj));
    }


    public void remove(String collectionName) {
        mongoOperations.remove(query, collectionName);
    }


    public long count(String collectionName) {
        return mongoOperations.count(query, collectionName);
    }

    public List find(Class clazz) {
        return mongoOperations.find(query, clazz, collectionName(clazz));
    }

    public <T> T findAndUpdate(Class clazz, String collectionName, IMongoUpdate update) {
        Update update1 = new Update();
        update.update(update1);
        return (T) mongoOperations.findAndModify(query, update1, clazz, collectionName);
    }


    public Long sum(String sum, Class clazz, String... group) {
        return new MyAggregation<Long>() {
            @Override
            public GroupOperation.GroupOperationBuilder nextGroupOperation(GroupOperation groupOperation, String key) {
                return groupOperation.sum(key);
            }

            @Override
            public Long valueOf(String value) {
                return Double.valueOf(value).longValue();
            }
        }.handler(sum, collectionName(clazz), group);
    }


/*    public List geo(int page, int limit, double lng, double lat, Class clazz) {
        try {
            System.out.println(query.getSkip());
            System.out.println(query.getLimit());
//            PageRequest pageRequest = new PageRequest(page, limit, Sort.Direction.DESC, "createAt");
            NearQuery nearQuery = NearQuery.near(lat, lng, Metrics.KILOMETERS).maxDistance(10).query(query).spherical(true).num(query.getLimit()).skip(query.getSkip()).with(pageRequest);
//            MyGeoNearOperation geoNearOperation = new MyGeoNearOperation(nearQuery);
//            AggregationResults aggregationResults = mongoOperations.aggregate(Aggregation.newAggregation(geoNearOperation), collectionName(clazz), HashMap.class);
            GeoResults geoResults = mongoOperations.geoNear(nearQuery, clazz, collectionName(clazz));
            return geoResults.;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }*/


    public GroupOperation fillFields(Class clazz, GroupOperation groupOperation) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            groupOperation = groupOperation.first(field.getName()).as(field.getName());
        }
        fields = clazz.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            groupOperation = groupOperation.first(field.getName()).as(field.getName());

        }
        return groupOperation;
    }

    public Double avg(String avg, Class clazz, String... group) {
        return new MyAggregation<Double>() {
            @Override
            public GroupOperation.GroupOperationBuilder nextGroupOperation(GroupOperation groupOperation, String key) {
                return groupOperation.avg(key);
            }

            @Override
            public Double valueOf(String value) {
                return Double.valueOf(value);
            }
        }.handler(avg, collectionName(clazz), group);

    }

    private abstract class MyAggregation<T extends Number> {
        public T handler(String key, String collectionName, String... group) {
            String valueKey = "_" + key;
            GroupOperation groupOperation = new GroupOperation(Fields.fields(group));
            AggregationResults aggregationResults = mongoOperations.aggregate(Aggregation.newAggregation(new MatchOperation(criteria), nextGroupOperation(groupOperation, key).as(valueKey)), collectionName, HashMap.class);
            List<Map> maps = aggregationResults.getMappedResults();
            Double total = 0d;
            for (Map map : maps) {
                total += Double.valueOf(String.valueOf(map.get(valueKey)));
            }
            return valueOf(String.valueOf(total));
        }

        public abstract T valueOf(String value);

        public abstract GroupOperation.GroupOperationBuilder nextGroupOperation(GroupOperation groupOperation, String key);
    }


    public List find(Class clazz, String collectionName) {
        LOGGER.debug(query.toString());
        return mongoOperations.find(query, clazz, collectionName);
    }

    public <T> T findOne(Class clazz) {
        return (T) mongoOperations.findOne(query, clazz, collectionName(clazz));
    }

    public <T> T findOne(Class clazz, String collectionName) {
        return (T) mongoOperations.findOne(query, clazz, collectionName);
    }

    public CommandResult executeCommand(String json) {
        return mongoOperations.executeCommand(json);
    }

    public void insert(Object obj) {
        insert(obj, collectionName(obj));
    }

    public void insert(Object obj, String collectionName) {
        mongoOperations.insert(obj, collectionName);
    }

    public List findAll(Class clazz, String collectionName) {
        return mongoOperations.findAll(clazz, collectionName);
    }

    public void ensureIndex(String name, Order order, String collectionName) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(name, order.getOrderValue());
        mongoOperations.getCollection(collectionName).createIndex(dbObject);
    }

    public void ensureIndex(String name, Order order, String collectionName, boolean unique) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(name, order.getOrderValue());
        mongoOperations.getCollection(collectionName).createIndex(dbObject, name + "_" + order.getOrderValue(), unique);
    }

    public void ensureIndex(Map<String, Order> keys, String collectionName, boolean unique) {
        DBObject dbObject = new BasicDBObject();
        StringBuilder name = new StringBuilder();
        for (Map.Entry<String, Order> en : keys.entrySet()) {
            dbObject.put(en.getKey(), en.getValue().getOrderValue());
            name.append(en.getKey()).append("_").append(en.getValue().getOrderValue()).append("_");
        }
        mongoOperations.getCollection(collectionName).createIndex(dbObject, name.substring(0, name.length() - 1), unique);
    }

    public void ensureIndex2D(String name, String collectionName) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put(name, "2d");
        mongoOperations.getCollection(collectionName).createIndex(dbObject);
    }


    public Mongo between(String key, Object begin, Object end, Between between) {
        between.between(and(key), begin, end);
        return this;
    }

    public enum Order {
        desc(-1), asc(1);

        private int orderValue;

        Order(int orderValue) {
            this.orderValue = orderValue;
        }

        public int getOrderValue() {
            return orderValue;
        }
    }

    public enum Between {
        EQ, NEQ, FEQ, EEQ;

        public void between(Criteria criteria, Object begin, Object end) {
            switch (this) {
                case EQ:
                    criteria.lte(end).gte(begin);
                    break;
                case NEQ:
                    criteria.lt(end).gt(begin);
                    break;
                case FEQ:
                    criteria.lt(end).gte(begin);
                    break;
                case EEQ:
                    criteria.lte(end).gt(begin);
                    break;
                default:
                    throw new QueryException("no Between enum");
            }
        }
    }
}