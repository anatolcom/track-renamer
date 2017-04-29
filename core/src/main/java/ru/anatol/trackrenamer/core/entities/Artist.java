package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;

/**
 * Created by Anatol on 28.04.2017.
 */
@Alias("artist")
public class Artist {
    private Long id;
    private String value;
}
