package org.panda.bamboo.common.util.algorithm;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * hutool雪花算法
 */
public class HutoolSnowflake implements Algorithm {

	/**
	 * 利用雪花算法生成分布式ID
	 * 适合多机分布式场景
	 */
	public static Long getDistributedId() {
		// 数据中心dataCenterId
		long dataCenterId = IdUtil.getDataCenterId(31L);
		// 机器工作号码workerId
		long workerId = IdUtil.getWorkerId(dataCenterId, 31L);
		Snowflake snowflake = IdUtil.getSnowflake(workerId, dataCenterId);
		return snowflake.nextId();
	}

	/**
	 * 利用雪花算法生成ID
	 */
	public static Long getSnowflakeId() {
		Snowflake snowflake = IdUtil.getSnowflake(1, 1);
		return snowflake.nextId();
	}

}


