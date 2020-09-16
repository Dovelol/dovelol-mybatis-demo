package com.dovelol.demo.mybatis;

import com.dovelol.demo.mybatis.entity.User;
import com.dovelol.demo.mybatis.mapper.UserMapper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.beans.PropertyVetoException;

/**
 * @author Dovelol
 * @date 2020/9/14 23:49
 */
public class C3P0ConfigDemo {

    public static void main(String[] args) throws PropertyVetoException {
        //DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/demo","root","");
        ComboPooledDataSource c3p0DataSource = new ComboPooledDataSource();
        c3p0DataSource.setUser("root");
        c3p0DataSource.setPassword("");
        c3p0DataSource.setDriverClass("com.mysql.jdbc.Driver");
        c3p0DataSource.setJdbcUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
        c3p0DataSource.setInitialPoolSize(10);
        c3p0DataSource.setMaxIdleTime(5);
        c3p0DataSource.setMaxPoolSize(10);
        c3p0DataSource.setMinPoolSize(5);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, c3p0DataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        configuration.setLogImpl(Log4j2Impl.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            for(int i = 0; i < 20; i++){
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user = mapper.getUserById(101L);
                System.out.println(user);
            }
        }
    }
}
