package org.panda.tech.core.web.util;

import org.lionsoul.ip2region.xdb.Searcher;
import org.panda.bamboo.common.util.LogUtil;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * IP转区域离线工具类
 *
 * @author fangen
 **/
public class IP2RegionUtil {
    /**
     * xdb文件相对路径
     */
    private static final String IP_XDB_PATH = "/META-INF/data/ip2region.xdb";
    /**
     * 全局searcher对象
     */
    private volatile Searcher searcher;

    /**
     * 获取IP区域信息
     * 并发使用，每个线程需要创建一个独立的searcher对象单独使用
     *
     * @param ip IP
     * @return 区域信息
     */
    public static String getIPRegion(String ip) {
        if (NetUtil.isLocalHost(ip)) { // 本地IP判断
            return "0|0|0|本地IP|本地IP";
        }
        try (InputStream ris = IP2RegionUtil.class.getResourceAsStream(IP_XDB_PATH)) {
            byte[] dbBinStr = FileCopyUtils.copyToByteArray(ris);
            Searcher searcher = Searcher.newWithBuffer(dbBinStr);
            String region = searcher.search(ip);
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
                    try (InputStream ris = IP2RegionUtil.class.getResourceAsStream(IP_XDB_PATH)) {
                        byte[] dbBinStr = FileCopyUtils.copyToByteArray(ris);
                        searcher = Searcher.newWithBuffer(dbBinStr);
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
        this.loadCachedSearcher();
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
