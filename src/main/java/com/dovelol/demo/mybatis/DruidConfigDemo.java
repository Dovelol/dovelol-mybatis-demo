package com.dovelol.demo.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
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

/**
 * @author Dovelol
 * @date 2020/9/15 21:44
 */
public class DruidConfigDemo {

    public static void main(String[] args) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/demo?useSSL=false");
        druidDataSource.setInitialSize(2);
        druidDataSource.setMinIdle(3);
        druidDataSource.setMaxActive(5);

        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);

        druidDataSource.setKeepAlive(true);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, druidDataSource);
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
