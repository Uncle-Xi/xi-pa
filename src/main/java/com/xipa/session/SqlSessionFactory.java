package com.xipa.session;

import java.sql.Connection;

public interface SqlSessionFactory {

    SqlSession openSession();

    Configuration getConfiguration();
}