package com.changhong.film;

import com.changhong.db.Redis.RedisService;
import com.changhong.db.domain.arango.FilmFeature;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by tangjuan on 2017/11/14.
 */
public class FilmProcessTest {

    @Before
    public void init() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext_db.xml");
        System.out.println("");
    }

    @Test
    public void testImportData2Redis() {
        String label = "film";
        FilmProcess exportFilm2Redis = new FilmProcess();
        exportFilm2Redis.exportAllFilmFeature2Redis(label);
    }

    @Test
    public void testGetJsonArray() {
        List<FilmFeature> filmFeatureList = RedisService.getDupEntityInfoList("变形金刚fff","film", FilmFeature.class);
        System.out.println("");
    }
}
