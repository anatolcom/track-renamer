/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.anatol.trackrenamer.core;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author Anatol
 */
public class MyBatis {

    protected static final SqlSessionFactory FACTORY;

    static {
        try {
            //Читаем файл с настройками подключения и настройками MyBatis
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            FACTORY = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }

    public static SqlSession openSession() {
        return FACTORY.openSession();
    }
}
