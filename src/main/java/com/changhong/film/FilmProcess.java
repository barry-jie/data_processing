package com.changhong.film;

import com.alibaba.fastjson.JSON;
import com.changhong.db.Redis.RedisService;
import com.changhong.db.arango.ArangoEntityHelper;
import com.changhong.db.arango.ArangoParamList;
import com.changhong.db.domain.arango.FilmEntity;
import com.changhong.db.domain.arango.FilmFeature;
import com.changhong.util.ChineseNum2mathNum3;
import com.changhong.util.MyStringUtils;
import com.changhong.util.RegexUtil;

import java.util.*;

/**
 * Created by tangjuan on 2017/11/10.
 */
public class FilmProcess {

    public void exportAllFilmFeature2Redis(String entityLabel) {
        Map<String, Map<String, String>> map = new HashMap<>();
        int pageSize = 50000;
        int pageNum = 1;
        while (true) {
            System.out.println("pageNum = " + pageNum);
            List<FilmFeature> filmFeatureList = ArangoEntityHelper.build().find(ArangoEntityHelper.Table.entity_filmFeature,
                    new ArangoParamList().eq("label", entityLabel), pageSize, pageNum);
            if (filmFeatureList.isEmpty()) break;
//            if(pageNum > 2) break;
            pageNum++;
            for (FilmFeature filmFeature : filmFeatureList) {
                String _key = filmFeature.get_key();
                Collection<String> formatNameColl = filmFeature.getFormatNames();
                if (formatNameColl == null) continue;

                filmFeature.setFormatNames(null);
                String jsonStr = JSON.toJSONString(filmFeature);

                for (String formatName : formatNameColl) {
                    String tempFormatName = formatName.replaceAll(RegexUtil.bracketAndPuncEx1, "");
                    tempFormatName = ChineseNum2mathNum3.change(tempFormatName);
                    if (map.containsKey(tempFormatName)) {
                        Map<String, String> tempMap = map.get(tempFormatName);
                        if (!tempMap.containsKey(_key)) {
                            tempMap.put(_key, jsonStr);
                            map.put(tempFormatName, tempMap);
                        }
                    } else {
                        Map<String, String> tempMap = new HashMap<>();
                        tempMap.put(_key, jsonStr);
                        map.put(tempFormatName, tempMap);
                    }
                }
            }
        }

        //写redis
        RedisService.putMap2Redis(entityLabel, map);
    }


    /**
     * 查询将要新增的film是否已经存在
     * @param filmEntity
     */
    public boolean isFilmExisted(FilmEntity filmEntity) {
        String label = "film";
        Collection<String> formatNames = filmEntity.getFormatNames();
        if (formatNames == null || formatNames.isEmpty()) {
            return true;
        }

        Collection<String> categorys1 = filmEntity.getCategorys();
        Integer year1 = filmEntity.getYear();
        String issue1 = filmEntity.getIssueDate();
        String director1 = filmEntity.getDirector();
        String removeRegEx = RegexUtil.bracketAndPuncEx1;
        Set<String> directorSet1 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx, true, false, director1);
        String writer1 = filmEntity.getWriter();
        Set<String> writerSet1 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx, true, false, writer1);
        String cast1 = filmEntity.getCast();
        Set<String> castSet1 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx, true, false, cast1);

        for (String formatName : formatNames) {
            List<FilmFeature> filmFeatureList = RedisService.getDupEntityInfoList(formatName,label, FilmFeature.class);
            if(filmFeatureList == null || filmFeatureList.isEmpty()) continue;
            for (FilmFeature filmFeature : filmFeatureList) {
                //上映日期是否相同
                String issue2 = filmFeature.getIssueDate();
                if (issue1 != null && !issue1.isEmpty() && issue2 != null && issue1.equals(issue2)) {
                    return true;
                }

                //上映年是否相同
                boolean isYearSame = false;
                Integer year2 = filmFeature.getYear();
                if (year1 != null && year2 != null) {
                    if (year1.equals(year2)) {
                        isYearSame = true;
                    }
                }

                //导演是否相同
                Set<String> direcotrSet2 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx,
                        true, false, filmFeature.getDirector());
                boolean haveCommonDirector = false;
                if (directorSet1 != null && direcotrSet2 != null && !directorSet1.isEmpty() &&!direcotrSet2.isEmpty()) {
                    if (MyStringUtils.haveCommonElement(directorSet1, direcotrSet2)) {
                        haveCommonDirector = true;
                    }
                }

                if (isYearSame && haveCommonDirector) {
                    return true;
                }

                //演员是否相同
                Set<String> castSet2 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx,
                        true, false, filmFeature.getCast());
                boolean haveCommonCast = false;
                if (castSet1 != null && castSet2 != null && !castSet1.isEmpty() && !castSet2.isEmpty()) {
                    if (MyStringUtils.haveCommonElement(castSet1, castSet2)) {
                        haveCommonCast = true;
                    }
                }
                if (isYearSame && haveCommonCast) {
                    return true;
                }

                //编剧是否相同
                Set<String> writerSet2 = MyStringUtils.addStrings2HashSet(" / ", removeRegEx,
                        true, false, filmFeature.getWriter());
                boolean haveComonWriter = false;
                if (writerSet1 != null && writerSet2 != null && !writerSet1.isEmpty() && !writerSet2.isEmpty()) {
                    if (MyStringUtils.haveCommonElement(writerSet1, writerSet2)) {
                        haveComonWriter = true;
                    }
                }
                if (isYearSame && haveComonWriter) {
                    return true;
                }


            }
        }

        return false;
    }


}
