package com.dovelol.demo.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.dovelol.demo.mybatis.entity.User;
import com.dovelol.demo.mybatis.mapper.UserMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

/**
 * @author Dovelol
 * @date 2020/9/16 22:34
 */
public class HikaricpConfigDemo {


    public static void main(String[] args) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("");
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
        hikariDataSource.setMaximumPoolSize(5);
        hikariDataSource.setMinimumIdle(3);

        hikariDataSource.setConnectionTestQuery("select 1");

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, hikariDataSource);
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

        for(int i = 0; i < 20; i++){
            try (SqlSession session = sqlSessionFactory.openSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user = mapper.getUserById(101L);
                System.out.println(user);
            }

        }
    }

}
