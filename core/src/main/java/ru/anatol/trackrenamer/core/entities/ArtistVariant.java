package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;

/**
 * Created by Anatol on 28.04.2017.
 */
@Alias("artist_variant")
public class ArtistVariant {
    private Long id;
    private Artist artist;
    private String value;
}
