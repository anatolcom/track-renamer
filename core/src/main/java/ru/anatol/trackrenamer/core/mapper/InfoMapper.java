package ru.anatol.trackrenamer.core.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import ru.anatol.trackrenamer.core.entities.Info;

/**
 * Created by Anatol on 24.04.2017.
 */
public interface InfoMapper {


    @Insert("INSERT INTO info (type, artist, title, comment) "
            + "VALUES (#{type}, #{artist}, #{title}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addInfo(Info info);
}
