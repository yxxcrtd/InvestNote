package com.google.code.routing4db.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.google.code.routing4db.holder.RoutingHolder;

/**
 * 常规数据源路由
 */
public class Routing4DBDataSource extends AbstractRoutingDataSource {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public Routing4DBDataSource() {
        this.setLenientFallback(false); // 根据key找不到对应数据源时，不进行容错处理，直接抛出找不到对应数据源的错误。 防止路由时因数据源配置不对，而才有默认数据源导致的错误
    }

    /**
     * 该方法返回需要使用的 DataSource 的 key 值，然后根据这个 key 从 resolvedDataSources 这个 map 里取出对应的 DataSource，如果找不到，则用默认的 resolvedDefaultDataSource
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceKey = RoutingHolder.getCurrentDataSourceKey();
        if (null == dataSourceKey) {
            logger.debug("没有 Routing Key！ 默认将会为当前 connection 使用默认的 DataSource！");
        } else {
            logger.info("当前 connection 的 DataSource 的 Routing Key 是：" + dataSourceKey);
        }
        return dataSourceKey;
    }

}
