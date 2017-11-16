package com.changhong.db.arango;

import com.arangodb.ArangoCursor;
import com.arangodb.entity.BaseDocument;
import com.changhong.db.SpringApplicationContext;
import com.changhong.db.domain.arango.FilmFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redred on 2017/9/7.
 * email:zhyx2014@yeah.net
 */
public class ArangoEntityHelper extends ArangoHelper {
    private static int knowledgeGraphTest = 0;
    private static int knowledgeSystem = 1;

    public static ArangoEntityHelper build() {
        return SpringApplicationContext.getArangoDbEntity();
    }

    public void insert(Table table, BaseDocument insertDoc) {
        insert(table.dbIndex, table.tableName, insertDoc);
    }

    public void update(Table table, String key, BaseDocument updateDoc) {
        update(table.dbIndex, table.tableName, key, updateDoc);
    }

    public List find(Table table, ArangoParamList params, int limit, int page) {
        List list = new ArrayList<>();
        ArangoCursor arangoCursor = find(table.clazz, table.dbIndex, table.tableName, params, limit, page);
        while (arangoCursor.hasNext()) {
            Object obj = arangoCursor.next();
            list.add(obj);
        }
        return list;
    }

    public enum Table {
        entity_filmFeature(knowledgeGraphTest, "entity", FilmFeature.class);

        private int dbIndex;
        private String tableName;
        private Class clazz;

        Table(int dbIndex, String tableName, Class clazz) {
            this.dbIndex = dbIndex;
            this.tableName = tableName;
            this.clazz = clazz;
        }

        public int getDbIndex() {
            return dbIndex;
        }

        public void setDbIndex(int dbIndex) {
            this.dbIndex = dbIndex;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }
    }
}
