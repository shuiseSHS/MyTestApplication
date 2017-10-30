package com.example.shisong.testapplication;

/**
 * Created by shisong on 2016/8/17.
 *
 */

public class StockItem {

    StockItem yesItem;
    String date;
    float startPrice;
    float endPrice;
    float percent;

    public StockItem(String s, StockItem item) {
        String[] ss = s.split(" ");
        date = ss[0];
        startPrice = Float.parseFloat(ss[1]);
        endPrice = Float.parseFloat(ss[4]);
        yesItem = item;
        if (yesItem == null) {
            percent = (endPrice - startPrice) / startPrice;
        } else {
            percent = (endPrice - yesItem.endPrice) / yesItem.endPrice;
        }
    }

    @Override
    public String toString() {
        java.text.NumberFormat percentFormat =java.text.NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2); //最大小数位数
        percentFormat.setMaximumIntegerDigits(2);//最大整数位数
        percentFormat.setMinimumFractionDigits(2); //最小小数位数
        percentFormat.setMinimumIntegerDigits(1);//最小整数位数
        return date + "\t" + percentFormat.format(percent) + "\t\t" + endPrice;
    }
}
