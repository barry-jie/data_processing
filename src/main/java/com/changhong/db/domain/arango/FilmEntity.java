package com.changhong.db.domain.arango;

import java.util.Collection;

/**
 * Created by tangjuan on 2017/11/10.
 */
public class FilmEntity extends BaseEntity {

    private String officialSite;

    private String summary;

    private Boolean hot;

    private String publisher;

    private Double boxOffice;

    private Double ratingNum;

    private Integer ratingPeopleNum;

    private String director;

    private String cast;

    private String writer;

    private String issueDate;

    private Integer year;

    private String oralName;

    private Integer sourceFlag;

    private Integer seasonNum;

    private Integer maxSeason;

    private Collection<String> role;

    private String language;

    private String tags;

    private Integer duration;

    private String type;

    private Integer episodeCount;

    private Collection<String> categorys;

    private String imdbNum;

    public String getOfficialSite() {
        return officialSite;
    }

    public void setOfficialSite(String officialSite) {
        this.officialSite = officialSite;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Double getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(Double boxOffice) {
        this.boxOffice = boxOffice;
    }

    public Double getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(Double ratingNum) {
        this.ratingNum = ratingNum;
    }

    public Integer getRatingPeopleNum() {
        return ratingPeopleNum;
    }

    public void setRatingPeopleNum(Integer ratingPeopleNum) {
        this.ratingPeopleNum = ratingPeopleNum;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
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

    public String getOralName() {
        return oralName;
    }

    public void setOralName(String oralName) {
        this.oralName = oralName;
    }

    public Integer getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(Integer sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    public Integer getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(Integer seasonNum) {
        this.seasonNum = seasonNum;
    }

    public Integer getMaxSeason() {
        return maxSeason;
    }

    public void setMaxSeason(Integer maxSeason) {
        this.maxSeason = maxSeason;
    }

    public Collection<String> getRole() {
        return role;
    }

    public void setRole(Collection<String> role) {
        this.role = role;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(Integer episodeCount) {
        this.episodeCount = episodeCount;
    }

    public Collection<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(Collection<String> categorys) {
        this.categorys = categorys;
    }

    public String getImdbNum() {
        return imdbNum;
    }

    public void setImdbNum(String imdbNum) {
        this.imdbNum = imdbNum;
    }
}
