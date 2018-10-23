package com.fjdynamics.fjkey.bean;

/**
 * @author Administrator QQ:824679118
 * @class name：com.keven.hibox.Bean
 * @time 2018/7/8 15:57
 * @class describe
 */
public class SellSum {

    private String tradeDate;
    private double value;

    public SellSum(String tradeDate, double value) {
        this.tradeDate = tradeDate;
        this.value = value;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
