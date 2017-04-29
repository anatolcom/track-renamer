/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core.entities;

import org.apache.ibatis.type.Alias;
import ru.anatol.Printable;
import ru.anatol.trackrenamer.core.enums.BitrateTypeEnum;

/**
 * @author Anatol
 */
@Alias("title_variant")
public class TitleVariant {
    private Long id;
    private Title title;
    private String value;
}
