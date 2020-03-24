package com.github.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static ThreadLocal<String> dataSourceKey = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSourceKey) {
        DynamicDataSource.dataSourceKey.set(dataSourceKey);
    }
    public static void removeDataSourceKey() {
        DynamicDataSource.dataSourceKey.remove();
    }

    /**
     * 获取数据源的key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKey.get();
    }


}
