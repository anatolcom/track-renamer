package ru.anatol.trackrenamer.core.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ru.anatol.trackrenamer.core.entities.Info;
import ru.anatol.trackrenamer.core.entities.Track;

/**
 * Created by Anatol on 24.04.2017.
 */
public interface InfoMapper {

    @Select("SELECT i.* " +
            "FROM info i " +
            "WHERE i.id = #{id}")
    Info getInfo(@Param("id") Long id);

    @Insert("INSERT INTO info (type, artist, title, comment) "
            + "VALUES (#{type}, #{artist}, #{title}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addInfo(Info info);
}
