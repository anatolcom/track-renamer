/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;

/**
 * @author Anatol
 */
public interface TrackMapper {

    @Insert("INSERT INTO track (md5, filename, size, length, bitrate, bitrate_type) "
            + "VALUES (#{md5}, #{filename}, #{size}, #{length}, #{bitrate}, #{bitrateType})")
    void addTrack(Track track);

    @Results({
            @Result(property = "bitrateType", column = "bitrate_type"),
//            @Result(property = "fromName", column = "from_name_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
//            @Result(property = "id3v1", column = "id3v1_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
//            @Result(property = "id3v2", column = "id3v2_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))
    })
    @Select("SELECT t.* " +
            "FROM track t " +
            "WHERE t.md5 = #{md5}")
    Track getTrack(@Param("md5") String md5);

    @Results({
            @Result(property = "bitrateType", column = "bitrate_type"),
//            @Result(property = "fromName", column = "from_name_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
//            @Result(property = "id3v1", column = "id3v1_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
//            @Result(property = "id3v2", column = "id3v2_id", javaType = Info.class,
//                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))

            @Result(property = "trackInfos", column = "md5", javaType = List.class,
                    many = @Many(select = "ru.anatol.trackrenamer.core.mapper.TrackInfoMapper.getTrackInfoByTrack", fetchType = FetchType.EAGER))
    })
    @Select("SELECT  t.* " +
            "FROM track t " +
            " LEFT JOIN track_info ti ON t.md5 = ti.track_md5")
    List<Track> getTracks();

    @Select("SELECT md5 FROM track")
    Set<String> getMD5Set();

    @Select("SELECT exists(SELECT FROM track WHERE md5 = #{md5}) AS exists")
    boolean existsMD5(String md5);

    @Delete("DELETE FROM track WHERE md5 = #{md5} ")
    void removeTrack(String md5);

}
