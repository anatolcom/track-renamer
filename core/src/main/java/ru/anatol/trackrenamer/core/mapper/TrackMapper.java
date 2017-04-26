/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Options;
import ru.anatol.trackrenamer.core.entities.Track;

/**
 * @author Anatol
 */
public interface TrackMapper {

    Track getTrack(Long id);

//    @Select("SELECT "
//            + "  t.id, "
//            + "  t.filename, "
//            + "  a.name as artist, "
//            + "  t.name, "
//            + "  t.comment "
//            + "FROM track t "
//            + "  LEFT JOIN artist a ON t.artist_id = a.id")
//    List<Track> getTracks();

//    @Select("SELECT md5 FROM track")
//    Set<String> getMD5Set();

    @Select("SELECT exists(SELECT FROM track WHERE md5 = #{md5}) AS exists")
    boolean existsMD5(String md5);

    @Insert("INSERT INTO track (md5, filename, size, length, bitrate, bitrate_type, from_name_id, id3v1_id, id3v2_id) "
            + "VALUES (#{md5}, #{filename}, #{size}, #{length}, #{bitrate}, #{bitrateType}, #{fromName.id}, #{id3v1.id}, #{id3v2.id})")
        //@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addTrack(Track track);

    //    void updateTrack(Track track);
    void removeTrack(Long id);

}
