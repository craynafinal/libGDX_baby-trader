package com.jsl.babytrader.Data;

import com.jsl.babytrader.Utilities.CommonUtilities;

/**
 * This enum represents attributes of babies and they also save sales values.
 */

public enum Attribute {
    // positive
    Smart(0, true),
    Humorous(1, true),
    Fast(2, true),
    Self_Confidence(3, true),
    Nice_Job(4, true),
    Rich(5, true),
    Handsome(6, true),
    Tall(7, true),

    // negative
    Dumb(8, false),
    Boring(9, false),
    Slow(10, false),
    Lazy(11, false),
    Bad_Job(12, false),
    Poor(13, false),
    Ugly(14, false),
    Short(15, false);

    private int index = 0;
    private int sellValue = 0;
    private int buyValue = 0;
    private boolean isPositive = false;

    private static final int DEFAULT_BUY_VALUE_POSITIVE = 50;
    private static final int DEFAULT_SELL_VALUE_POSITIVE = 75;
    private static final int DEFAULT_BUY_VALUE_NEGATIVE = 25;
    private static final int DEFAULT_SELL_VALUE_NEGATIVE = 50;
    private static final int DEFAULT_VARIATION = 20;

    // depending on the type, it will assign default sale values
    Attribute(int index, boolean isPositive) {
        this.index = index;
        this.isPositive = isPositive;

        if (isPositive) {
            sellValue = DEFAULT_SELL_VALUE_POSITIVE + CommonUtilities.getRandomNumber(-DEFAULT_VARIATION, DEFAULT_VARIATION);
            buyValue = DEFAULT_BUY_VALUE_POSITIVE + CommonUtilities.getRandomNumber(-DEFAULT_VARIATION, DEFAULT_VARIATION);
        } else {
            sellValue = DEFAULT_SELL_VALUE_NEGATIVE + CommonUtilities.getRandomNumber(-DEFAULT_VARIATION, DEFAULT_VARIATION);
            buyValue = DEFAULT_BUY_VALUE_NEGATIVE + CommonUtilities.getRandomNumber(-DEFAULT_VARIATION, DEFAULT_VARIATION);
        }
    }

    Attribute(int index, int initSell, int initBuy) {
        this.index = index;
        sellValue = initSell;
        buyValue = initBuy;
    }

    public int getSellValue() {
        return sellValue;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public void setSellValue(int sellValue) {
        this.sellValue = sellValue;
    }

    public void setBuyValue(int buyValue) {
        this.buyValue = buyValue;
    }

    public int getIndex() {
        return index;
    }

    public boolean isPositive() {
        return isPositive;
    }

    // get max number of enum elements
    public static int getMaxNum() {
        return Attribute.values().length;
    }

    // this will return a name of instance but replaces underscore with blank space
    public String getName() {
        return this.name().replace("_", " ");
    }

}