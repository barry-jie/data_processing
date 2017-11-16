package com.changhong.db.domain.arango;

import java.util.Collection;

/**
 * Created by tangjuan on 2017/11/10.
 */
public class BaseEntity {

    private String label;

    private String name;

    private String formatName;

    private Collection<String> alias;

    private Collection<String> formatNames;

    private Collection<String> originFormatNames;

    private Collection<String> dataSource;

    private String status;

    private Long updateAt;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public Collection<String> getAlias() {
        return alias;
    }

    public void setAlias(Collection<String> alias) {
        this.alias = alias;
    }

    public Collection<String> getFormatNames() {
        return formatNames;
    }

    public void setFormatNames(Collection<String> formatNames) {
        this.formatNames = formatNames;
    }

    public Collection<String> getOriginFormatNames() {
        return originFormatNames;
    }

    public void setOriginFormatNames(Collection<String> originFormatNames) {
        this.originFormatNames = originFormatNames;
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

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Long updateAt) {
        this.updateAt = updateAt;
    }
}
