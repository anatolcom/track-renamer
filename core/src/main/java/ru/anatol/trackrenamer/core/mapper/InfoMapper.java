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

    @Insert("INSERT INTO info (artist, title, comment) "
            + "VALUES (#{artist}, #{title}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addInfo(Info info);

    @Select("SELECT i.* " +
            "FROM info i " +
            "WHERE i.id = #{id}")
    Info getInfo(@Param("id") Long id);

    @Select("SELECT i.* " +
            "FROM info i, " +
            "  (SELECT " +
            "     #{artist} :: VARCHAR(256)  AS artist,\n" +
            "     #{title} :: VARCHAR(256)   AS title,\n" +
            "     #{comment} :: VARCHAR(256) AS comment) e " +
            "WHERE (i.artist = e.artist  OR (i.artist IS NULL AND e.artist IS NULL)) " +
            "  AND (i.title = e.title OR (i.title IS NULL AND e.title IS NULL)) " +
            "  AND (i.comment = e.comment OR (i.comment IS NULL AND e.comment IS NULL))")
    Info getEqualsInfo(Info info);

}
