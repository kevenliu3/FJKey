package com.fjdynamics.fjkey.bean;

/**
 * @author Administrator QQ:824679118
 * @class name：com.keven.hibox.Bean
 * @time 2018/7/8 15:56
 * @class describe
 */
public class Order {

    private String tradeDate;
    private int num;

    public Order(String tradeDate, int num) {
        this.tradeDate = tradeDate;
        this.num = num;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
