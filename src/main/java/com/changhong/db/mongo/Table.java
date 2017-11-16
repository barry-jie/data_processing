package com.changhong.db.mongo;

/**
 * leo.yan on 2014/8/20.
 */

@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Table {
    String name();
}
