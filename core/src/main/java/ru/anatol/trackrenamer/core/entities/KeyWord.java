package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;

/**
 * Created by Anatol on 28.04.2017.
 */
@Alias("key_word")
public class KeyWord {
    private Long id;
    private String value;
}
