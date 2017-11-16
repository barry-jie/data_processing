package com.changhong.db.arango;

import java.util.ArrayList;

/**
 * Created by redred on 2017/9/8.
 * email:zhyx2014@yeah.net
 */
public class ArangoParamList extends ArrayList {

    public ArangoParamList eq(String field, Object value) {
        ArangoQueryParam param=new ArangoQueryParam(field,value,ArangoQueryType.eq);
        param.setFilterString( " and o."+param.getField()+"==@"+param.getField());
        this.add(param);
        return this;
    }
    public ArangoParamList gt(String field, long value) {
        ArangoQueryParam param=new ArangoQueryParam(field,value,ArangoQueryType.eq);
        param.setFilterString(" and o." + param.getField()+">@"+param.getField());
        this.add(param);
        return this;
    }
    public ArangoParamList contains(String field, Object value) {
        ArangoQueryParam param = new ArangoQueryParam(field, value, ArangoQueryType.contains);
        param.setFilterString( " and contains( o."+param.getField()+",@"+param.getField()+") ");
        this.add(param);
        return this;
    }
    public ArangoParamList anyIn(String field,String[] fields, Object value) {
        ArangoQueryParam param = new ArangoQueryParam(field,fields, value, ArangoQueryType.anyIn);
        StringBuilder query = new StringBuilder();
        query.append(" and [");
        int index=0;
        for (String key:param.getInFieldArray()){
            if (index++==0){
                query.append(" o.").append(key);
            }else {
                query.append(" ,o.").append(key);
            }
        }
        query.append("] any in @").append(param.getField());
        param.setFilterString(query.toString());
        this.add(param);
        return this;
    }
}
