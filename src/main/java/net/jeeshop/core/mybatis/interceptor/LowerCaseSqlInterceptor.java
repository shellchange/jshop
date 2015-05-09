package net.jeeshop.core.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 将待执行SQL转换为全小写的拦截器
 *
 * @author dylan
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        , @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class LowerCaseSqlInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(LowerCaseSqlInterceptor.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String method = invocation.getMethod().getName();
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        SqlSource sqlSource = ms.getSqlSource();
        BoundSql boundSql = sqlSource.getBoundSql(invocation.getArgs()[1]);
        String sql = boundSql.getSql();
        logger.info("method type : {}, source sql : {}", method, sql);
        Field sqlField = BoundSql.class.getDeclaredField("sql");
        ReflectionUtils.makeAccessible(sqlField);
        ReflectionUtils.setField(sqlField, boundSql, sql.toLowerCase());
        logger.info("method type : {}, converted sql : {}", method, boundSql.getSql());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
