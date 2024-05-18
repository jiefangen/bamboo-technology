package org.panda.tech.core.web.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.panda.bamboo.common.constant.Commons;
import org.panda.bamboo.common.constant.basic.Strings;
import org.panda.bamboo.common.util.LogUtil;
import org.panda.tech.core.util.CommonUtil;

import java.io.IOException;

/**
 * IP转区域离线工具类
 *
 * @author fangen
 **/
public class IP2RegionUtil {

    private static final String IP_XDB_PATH = "META-INF/data/ip2region.xdb";

    private volatile Searcher searcher;

    /**
     * 获取IP区域信息
     * 并发使用，每个线程需要创建一个独立的searcher对象单独使用
     *
     * @param ip IP
     * @return 区域信息
     */
    public static String getIPRegion(String ip) {
        try {
            String dbPath = CommonUtil.getCurrentPath(IP2RegionUtil.class) + Strings.SLASH + Commons.PROJECT_RES_PATH
                    + IP_XDB_PATH;
            // 创建 searcher 对象
            Searcher searcher = Searcher.newWithFileOnly(dbPath);
            String region = searcher.search(ip);
            // 关闭资源
            searcher.close();
            return region;
        } catch (Exception e) {
            LogUtil.error(IP2RegionUtil.class, "failed to search({}): {}\n", ip, e);
        }
        return null;
    }

    /**
     * 加载缓存xdb数据的searcher对象
     */
    private void loadCachedSearcher() {
        if (searcher == null) {
            synchronized(this) { // 锁当前实例
                if (searcher == null) {
                    try {
                        String dbPath = CommonUtil.getCurrentPath(IP2RegionUtil.class) + Strings.SLASH
                                + Commons.PROJECT_RES_PATH + IP_XDB_PATH;
                        byte[] cBuff = Searcher.loadContentFromFile(dbPath);
                        searcher = Searcher.newWithBuffer(cBuff);
                    } catch (Exception e) {
                        LogUtil.error(IP2RegionUtil.class, "failed to load content cached searcher: {}\n", e);
                    }
                }
            }
        }
    }

    /**
     * 获取IP区域信息（xdb数据缓存）
     *
     * @param ip IP
     * @return 区域信息
     */
    public String getIPRegionCache(String ip) {
        loadCachedSearcher();
        try {
            return searcher.search(ip);
        } catch (Exception e) {
            LogUtil.error(IP2RegionUtil.class, "failed to search({}): {}\n", ip, e);
        }
        return null;
    }

    public void close() {
        if (this.searcher != null) {
            try {
                this.searcher.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}
