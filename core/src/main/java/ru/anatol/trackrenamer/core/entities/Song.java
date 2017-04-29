package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;

/**
 * Created by Anatol on 29.04.2017.
 */
@Alias("song")
public class Song {
    private Long id;
    Artist artist;
    Title title;
}
