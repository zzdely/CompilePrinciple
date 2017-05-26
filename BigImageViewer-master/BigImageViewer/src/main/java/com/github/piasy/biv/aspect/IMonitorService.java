package com.github.piasy.biv.aspect;

/**
 * Created by 50567 on 17/5/18.
 */

public interface IMonitorService {
    /** */
    /**
     * 获得当前的监控对象.
     *
     * @return 返回构造好的监控对象
     * @throws Exception
     * @author amgkaka Creation date: 2008-4-25 - 上午10:45:08
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception;
}
