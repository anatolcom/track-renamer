package ru.anatol.trackrenamer.core.mapper;

import org.apache.ibatis.annotations.*;
import ru.anatol.trackrenamer.core.entities.Artist;
import ru.anatol.trackrenamer.core.entities.ArtistVariant;

/**
 * Created by Anatol on 29.04.2017.
 */
public interface SongMapper {

    @Select("SELECT a.* FROM artist a WHERE a.id = #{id}")
    Artist getArtist(@Param("id") Long id);

    @Insert("INSERT INTO artist (value) "
            + "VALUES (#{value})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addArtist(Artist artist);




    @Results({
            @Result(property = "artist", column = "artist_id", javaType = Artist.class,
                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.SongMapper.getArtist")),
    })
    @Select("SELECT av.* "
            + "FROM artist_variant av "
            + "WHERE av.value = #{value}"
            + "LIMIT 1")
    ArtistVariant findArtistVariant(@Param("value") String value);

    @Insert("INSERT INTO artist_variant (artist_id, value) "
            + "VALUES (#{artist.id}, #{value})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addArtistVariant(ArtistVariant artistVariant);
}
