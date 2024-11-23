package com.group1.scansaver.dataobjects;

public class Item {

    private String ITEM_UPC;
    private String ITEM_NAME;
    private double ITEM_PRICE;
    private String[] ITEM_LOCATION_ARRAY;

    public Item() {
        this.ITEM_UPC = null;
        this.ITEM_NAME = null;
        this.ITEM_PRICE = 0.00;
        this.ITEM_LOCATION_ARRAY = null;
    }
    public Item(String name, String upc, double price) {
        this.ITEM_UPC = upc;
        this.ITEM_NAME = name;
        this.ITEM_PRICE = price;
        this.ITEM_LOCATION_ARRAY = null;
    }
    public Item(String name, String upc, double price, String[] locArray) {
        this.ITEM_UPC = upc;
        this.ITEM_NAME = name;
        this.ITEM_PRICE = price;
        this.ITEM_LOCATION_ARRAY = locArray;
    }

    public String getUPC() {
        return ITEM_UPC;
    }

    public void setUPC(String ITEM_UPC) {
        this.ITEM_UPC = ITEM_UPC;
    }

    public String getNAME() {
        return ITEM_NAME;
    }

    public void setNAME(String ITEM_NAME) {
        this.ITEM_NAME = ITEM_NAME;
    }

    public double getPRICE() {
        return ITEM_PRICE;
    }

    public void setPRICE(double ITEM_PRICE) {
        this.ITEM_PRICE = ITEM_PRICE;
    }

    public String[] getITEM_LOCATION_ARRAY() {
        return ITEM_LOCATION_ARRAY;
    }

    public void setITEM_LOCATION_ARRAY(String[] ITEM_LOCATION_ARRAY) {
        this.ITEM_LOCATION_ARRAY = ITEM_LOCATION_ARRAY;
    }
}

