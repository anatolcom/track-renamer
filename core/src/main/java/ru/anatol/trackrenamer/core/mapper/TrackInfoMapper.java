package ru.anatol.trackrenamer.core.mapper;

import org.apache.ibatis.annotations.*;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;
import ru.anatol.trackrenamer.core.entities.TrackInfo;

import java.util.List;

/**
 * Created by Anatol on 24.04.2017.
 */
public interface TrackInfoMapper {

    @Insert("INSERT INTO track_info (type, track_md5, info_id) "
            + "VALUES (#{type}, #{track.md5}, #{info.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addTrackInfo(TrackInfo trackInfo);

    @Select("SELECT ti.* " +
            "FROM track_info ti " +
            "WHERE ti.id = #{id}")
    TrackInfo getTrackInfo(@Param("id") Long id);

    @Results({
            @Result(property = "track", column = "track_md5", javaType = Track.class,
                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.TrackMapper.getTrack")),
            @Result(property = "info", column = "info_id", javaType = Info.class,
                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))
    })
    @Select("SELECT  ti.* " +
            "FROM track_info ti " +
            "WHERE ti.track_md5 = #{md5}")
    List<TrackInfo> getTrackInfosByMd5(@Param("md5") String md5);

    @Results({
            @Result(property = "track", column = "track_md5", javaType = Track.class,
                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.TrackMapper.getTrack")),
            @Result(property = "info", column = "info_id", javaType = Info.class,
                    one = @One(select = "ru.anatol.trackrenamer.core.mapper.InfoMapper.getInfo"))
    })
    @Select("SELECT  ti.* " +
            "FROM track_info ti " +
            "WHERE ti.track_md5 = #{track.md5}")
    List<TrackInfo> getTrackInfosByTrack(@Param("track") Track track);
}
