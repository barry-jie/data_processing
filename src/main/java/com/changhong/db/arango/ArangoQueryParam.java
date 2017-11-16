package com.changhong.db.arango;

public class ArangoQueryParam {
    private String field;
    private String[] inFieldArray;
    private Object value;
    private ArangoQueryType queryType;
    private String filterString;
    public ArangoQueryParam(String field, String[] inFieldArray, Object value, ArangoQueryType queryType) {
        this.field = field;
        this.inFieldArray = inFieldArray;
        this.value = value;
        this.queryType = queryType;
    }

    public ArangoQueryParam(String field, Object value, ArangoQueryType queryType) {
        this.field = field;
        this.value = value;
        this.queryType = queryType;
    }

    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public String[] getInFieldArray() {
        return inFieldArray;
    }

    public void setInFieldArray(String[] inFieldArray) {
        this.inFieldArray = inFieldArray;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ArangoQueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(ArangoQueryType queryType) {
        this.queryType = queryType;
    }
}