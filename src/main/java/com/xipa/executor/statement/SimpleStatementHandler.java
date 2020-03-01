package com.xipa.executor.statement;

import com.sun.org.apache.regexp.internal.RE;
import com.xipa.executor.Executor;
import com.xipa.executor.parameter.ParameterHandler;
import com.xipa.mapping.BoundSql;
import com.xipa.mapping.MappedStatement;
import com.xipa.session.Configuration;
import com.xipa.session.ResultHandler;
import com.xipa.session.RowBounds;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @description: SimpleStatementHandler
 * ...
 * @author: Uncle.Xi 2020
 * @since: 1.0
 * @Environment: JDK1.8 + CentOS7.x + ?
 */
public class SimpleStatementHandler extends BaseStatementHandler {

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter,
                                  RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameter, rowBounds, resultHandler, boundSql);
    }

    @Override
    public int update(MappedStatement statement) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
//            connection = getConnection(statement.getConfiguration());
//            parameterHandler.setParameters(connection.prepareStatement(getBoundSql().getSql()));
//            result = preparedStatement.executeUpdate();
            connection = executor.getTransaction().getConnection();
            String sql = getBoundSql().getSql();
            //System.out.printf("即将执行的 SQL 语句：%s \n", sql);
            preparedStatement = connection.prepareStatement(sql);
            parameterHandler.setParameters(preparedStatement);
            result = preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
            executor.rollback(true);
        } finally {
            if (connection != null) {
                try {
                    executor.commit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public <E> List<E> query(MappedStatement statement, ResultHandler resultHandler) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        List<E> result = null;
        try {
            connection = executor.getTransaction().getConnection();
            connection.setAutoCommit(true);
            String sql = getBoundSql().getSql();
            //System.out.printf("即将执行的 SQL 语句：%s \n", sql);
            preparedStatement = connection.prepareStatement(sql);
            parameterHandler.setParameters(preparedStatement);
            preparedStatement.executeQuery();
            try {
                result = resultSetHandler.handleResultSets(preparedStatement);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
