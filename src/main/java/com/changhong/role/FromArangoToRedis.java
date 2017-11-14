package com.changhong.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arangodb.ArangoCursor;
import com.arangodb.util.MapBuilder;
import com.changhong.util.ArangodbUtil;
import com.changhong.util.JedisUtil;

public class FromArangoToRedis {

	public static String KEY = "role";
	public static int INDEX = 4;
	
	public static void main(String[] args) {
		String queryFigure = "for doc in @@entityCollName filter doc.status == 'active' and doc.label == 'role' "
				+ "return {'key':doc._key,'profession':doc.profession,'formatName':doc.formatName,'formatNames':doc.formatNames,'label':doc.label,'name':doc.name, 'dataSource':doc.dataSource}";
        ArangoCursor<String> iter = ArangodbUtil.getDbDouban().query(queryFigure, new MapBuilder()
                .put("@entityCollName", "entity").get(), null, String.class);
    	List<JSONObject> list = new ArrayList<>();
        while(iter.hasNext()){
        	JSONObject json = JSONObject.parseObject(iter.next());
        	list.add(json);
        }
        for(JSONObject item : list){
        	JSONArray formatNames = item.getJSONArray("formatNames");
        	String name = item.getString("name");
        	String dataSource = item.getString("dataSource");
        	String formatName = item.getString("formatName");
        	String key = item.getString("key");
        	if(!formatNames.isEmpty()){
        		for(int i =0;i<formatNames.size();i++){
        			JSONObject json = new JSONObject();
        			String formatNamesTmp = formatNames.getString(i);
        			if(!StringUtils.isBlank(formatNamesTmp)){
        				formatNamesTmp = formatNamesTmp.toUpperCase();
        			}
        			json.put("name", name);
        			json.put("dataSource", dataSource);
        			json.put("key", key);
        			json.put("formatName", formatName);
        			if(JedisUtil.hexists(KEY, formatNamesTmp, INDEX)){
        				JSONArray tmp = JSONArray.parseArray(JedisUtil.hget(KEY, formatNamesTmp, INDEX));
        				tmp.add(json);
        				JedisUtil.hset(KEY, formatNamesTmp, tmp.toString(), INDEX);
        			}else{
        				JSONArray tmp = new JSONArray();
        				tmp.add(json);
        				JedisUtil.hset(KEY, formatNamesTmp, tmp.toString(), INDEX);
        			}
        		}
        	}
        }
	}
}
