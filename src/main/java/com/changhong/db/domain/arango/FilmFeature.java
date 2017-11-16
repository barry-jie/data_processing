package com.changhong.db.domain.arango;

import java.util.Collection;

/**
 * Created by tangjuan on 2017/11/13.
 */
public class FilmFeature {
    private String _key;

    private String issueDate;

    private Integer year;

    private Collection<String> formatNames;

    private String cast;

    private String director;

    private String writer;

    private Collection<String> categorys;

    private Collection<String> dataSource;

    private String status;

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Collection<String> getFormatNames() {
        return formatNames;
    }

    public void setFormatNames(Collection<String> formatNames) {
        this.formatNames = formatNames;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public Collection<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(Collection<String> categorys) {
        this.categorys = categorys;
    }

    public Collection<String> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Collection<String> dataSource) {
        this.dataSource = dataSource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
