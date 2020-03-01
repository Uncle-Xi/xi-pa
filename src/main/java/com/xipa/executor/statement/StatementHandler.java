package com.xipa.executor.statement;

import com.xipa.mapping.MappedStatement;
import com.xipa.session.ResultHandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    int update(MappedStatement statement) throws SQLException;

    <E> List<E> query(MappedStatement statement, ResultHandler resultHandler) throws SQLException;
}
