package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;

/**
 * Created by Anatol on 28.04.2017.
 */
@Alias("artist")
public class Artist {
    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
