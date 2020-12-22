package com.xrwl.driver.module.publish.bean;

import java.io.Serializable;

/**
 * 支付结果
 * Created by www.longdw.com on 2018/4/23 下午1:23.
 */
public class PayResult implements Serializable {
    private Config config;
    public Config getConfig() {
        return config;
    }
    public void setConfig(Config config) {
        this.config = config;
    }
    @Override
    public String toString() {
        return "data{" +
                "config=" + config.toString()+'}';
    }

}