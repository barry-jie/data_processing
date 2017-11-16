package com.changhong.db;

/**
 * Created by tangjuan on 2017/11/7_
 */
public interface DatabaseTable {

    //redis
    Integer redisDbIndex = 4;


    //mongo



    //arango

    String graph_name = "celebrity_film_graph";

    String douban_arango_entity="entity";
    String douban_arango_act = "celebrity_film_act";
    String douban_arango_work = "celebrity_film_work";

    String mtime_arango_entity = "entity_mtime";
    String mtime_arango_act = "celebrity_film_act_mtime";
    String mtime_arango_work = "celebrity_film_work_mtime";
}
