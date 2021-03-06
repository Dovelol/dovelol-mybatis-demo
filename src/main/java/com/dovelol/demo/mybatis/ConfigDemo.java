package com.dovelol.demo.mybatis;

import com.dovelol.demo.mybatis.entity.User;
import com.dovelol.demo.mybatis.mapper.UserMapper;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.apache.ibatis.mapping.SqlCommandType.SELECT;

/**
 * @author Dovelol
 * @date 2020/9/9 22:06
 */
public class ConfigDemo {

    public static void main(String[] args) {
        //DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
        DataSource dataSource = new PooledDataSource("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/demo?useSSL=false","root","");
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("dev", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        //configuration.addMapper(UserMapper.class);

        List<ResultMapping> resultMappings = new ArrayList<>();
        ResultMapping parameterMapping = new ResultMapping.Builder(configuration,"id","id",Long.class).build();
        resultMappings.add(parameterMapping);
        ResultMapping parameterMapping1 = new ResultMapping.Builder(configuration,"name","name",String.class).build();
        resultMappings.add(parameterMapping1);


        RawSqlSource staticSqlSource = new RawSqlSource(configuration, "select * from user where id = #{id}", Long.class);

        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(configuration,"user",User.class,resultMappings,true).build();
        resultMaps.add(resultMap);

        MappedStatement mappedStatement = new  MappedStatement.Builder(configuration,"selectUserById",
                staticSqlSource,SELECT).resultMaps(resultMaps).build();
        configuration.addMappedStatement(mappedStatement);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            User user = session.selectOne("selectUserById", 101L);
            System.out.println(user);
        }

    }
}
