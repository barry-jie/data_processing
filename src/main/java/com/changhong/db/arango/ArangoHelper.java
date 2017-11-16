package com.changhong.db.arango;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.entity.BaseDocument;
import com.arangodb.util.MapBuilder;
import com.changhong.db.domain.arango.BaseEntity;
import com.changhong.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by redred on 2017/9/6.
 * email:zhyx2014@yeah.net
 */
public class ArangoHelper {
    protected ArangoDB arangoDB;
    protected ArangoConfig config;
    protected ArangoDatabase[] dbs;
    private static final Logger log = LoggerFactory.getLogger(ArangoHelper.class);

    public void shutDown() {
        arangoDB.shutdown();
    }

    protected void init() {
        //连接Arangodb服务器
        arangoDB = new ArangoDB.Builder().host(config.getHost(), config.getPort()).user(config.getUserName()).password(config.getPassword()).build();
        //判断database是否已经存在，不存在就新创建
        dbs = new ArangoDatabase[config.getDbNames().size()];
        int index = 0;
        for (String dbName : config.getDbNames()) {
            if (!arangoDB.getDatabases().contains(dbName)) {
                arangoDB.createDatabase(dbName);
            }
            dbs[index++] = arangoDB.db(dbName);
        }

    }

    protected void insert(int dbIndex, String doc, BaseDocument insertDoc) {
        String insertCmmd = "insert @insertDoc into @@doc";
        dbs[dbIndex].query(insertCmmd, new MapBuilder().put("insertDoc", insertDoc).put("@doc", doc).get(), null, null);
    }

    protected void update(int dbIndex, String doc, String key, BaseDocument updateDoc) {
        String updateCmmd = "update {_key:@key} with @updateDoc into @@doc";
        dbs[dbIndex].query(updateCmmd, new MapBuilder().put("key", key).put("updateDoc", updateDoc).put("@doc", doc).get(), null, null);
    }

    protected void upsert(int dbIndex, String doc, BaseDocument upsertDoc, BaseDocument insertDoc, BaseDocument updateDoc) {
        String upsertCmmd = "upsert @upsertDoc insert @insertDoc update @updateDoc in @doc  OPTIONS { keepNull: false }";
        dbs[dbIndex].query(upsertCmmd, new MapBuilder().put("upsertDoc", upsertDoc).put("insertDoc", insertDoc).put("updateDoc", updateDoc).put("doc", doc).get(), null, null);
    }

    protected ArangoCursor<BaseEntity> find(Class clazz, int dbIndex, String doc, List<ArangoQueryParam> arangoQueryParamList, int limit, int page) {
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
//        table : for o in table
        query.append(" for o in ").append(doc);
//        filter  o.field=@field and o.field1=@field1
        if (!CollectionUtils.isEmpty(arangoQueryParamList)) {
            query.append(" filter ");
            int index = 0;
            for (ArangoQueryParam arangoQueryParam : arangoQueryParamList) {
                if (index++ == 0) {
                    query.append(arangoQueryParam.getFilterString().replace("and", ""));
                } else {
                    query.append(arangoQueryParam.getFilterString());
                }
                params.put(arangoQueryParam.getField(), arangoQueryParam.getValue());

            }
        }
//        limit offset,count
        if (page > 0 && limit > 0) {
            query.append(" limit ").append((page - 1) * limit).append(",").append(limit);
        }
//        return o
        query.append(" return o ");
        LogUtil.debug(query.toString());
        return dbs[dbIndex].query(query.toString(), params, null, clazz);
    }

    public ArangoConfig getConfig() {
        return config;
    }

    public void setConfig(ArangoConfig config) {
        this.config = config;
    }

    public ArangoCursor<BaseDocument> query(int dbIndex, String query, Map<String, Object> params) {
        return dbs[dbIndex].query(query, params, null, BaseDocument.class);
    }
}
