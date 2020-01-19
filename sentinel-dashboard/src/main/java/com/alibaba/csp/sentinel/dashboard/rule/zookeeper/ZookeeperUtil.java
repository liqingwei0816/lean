package com.alibaba.csp.sentinel.dashboard.rule.zookeeper;

import com.alibaba.csp.sentinel.dashboard.util.SpringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZookeeperUtil {
   // public static ApplicationContext applicationContext = null;

    public static void setData(String path, byte[] data) throws Exception {
        CuratorFramework zkClient = SpringUtils.applicationContext.getBean(CuratorFramework.class);
        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, data);
    }

    public static String getData(String zkPath) {
        CuratorFramework zkClient = SpringUtils.applicationContext.getBean(CuratorFramework.class);
         try {
             byte[] bytes = zkClient.getData().forPath(zkPath);
             return new String(bytes);
         }catch (Exception ex){
             return "[]";
         }

    }

}
