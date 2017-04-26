/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.date;

import java.util.Date;

/**
 *
 * @author Anatol
 */
public interface DateFormater {

    String format(Date date) throws IllegalArgumentException;

}
