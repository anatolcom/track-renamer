/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.*;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;

/**
 * @author Anatol
 */
public interface TrackMapper {

    @Results({
            @Result(property = "bitrateType", column = "bitrate_type"),
            @Result(property="fromName",  column="from_name_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
            @Result(property="id3v1",  column="id3v1_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
            @Result(property="id3v2",  column="id3v2_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))
    })
    @Select("SELECT t.* " +
            "FROM track t " +
            "WHERE t.md5 = #{md5}")
    Track getTrack(@Param("md5") String md5);


    @Results({
            @Result(property = "bitrateType", column = "bitrate_type"),
            @Result(property="fromName",  column="from_name_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
            @Result(property="id3v1",  column="id3v1_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo")),
            @Result(property="id3v2",  column="id3v2_id",  javaType=Info.class, one=@One(select="ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))
    })
    @Select("SELECT  t.* " +
            "FROM track t")
    List<Track> getTracks();

    @Select("SELECT md5 FROM track")
    Set<String> getMD5Set();

    @Select("SELECT exists(SELECT FROM track WHERE md5 = #{md5}) AS exists")
    boolean existsMD5(String md5);

    @Insert("INSERT INTO track (md5, filename, size, length, bitrate, bitrate_type, from_name_id, id3v1_id, id3v2_id) "
            + "VALUES (#{md5}, #{filename}, #{size}, #{length}, #{bitrate}, #{bitrateType}, #{fromName.id}, #{id3v1.id}, #{id3v2.id})")
        //@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addTrack(Track track);

    //    void updateTrack(Track track);
    @Delete("DELETE FROM track WHERE md5 = #{md5} ")
    void removeTrack(String md5);

}
