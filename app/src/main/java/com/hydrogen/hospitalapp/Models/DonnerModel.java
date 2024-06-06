package com.hydrogen.hospitalapp.Models;

public class DonnerModel {

    private String DONER_NAME;
    private String EMAIL;
    private String PHONE;
    private String DISTRICT;
    private String ZONE;
    private String BLOOD_GROUP;
    private String PIN;
    private int WEIGHT;
    private int DONER_STATUS;
    private int LAST_DONATE_DATE;

    public DonnerModel(String DONER_NAME, String EMAIL, String PHONE, String DISTRICT, String ZONE, String BLOOD_GROUP, String PIN, int WEIGHT) {
        this.DONER_NAME = DONER_NAME;
        this.EMAIL = EMAIL;
        this.PHONE = PHONE;
        this.DISTRICT = DISTRICT;
        this.ZONE = ZONE;
        this.BLOOD_GROUP = BLOOD_GROUP;
        this.PIN = PIN;
        this.WEIGHT = WEIGHT;
    }

    public String getDONER_NAME() {
        return DONER_NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public String getPHONE() {
        return PHONE;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public String getZONE() {
        return ZONE;
    }

    public String getBLOOD_GROUP() {
        return BLOOD_GROUP;
    }

    public String getPIN() {
        return PIN;
    }

    public int getWEIGHT() {
        return WEIGHT;
    }

    public int getONER_STATUS() {
        return DONER_STATUS;
    }

    public int getLAST_DONATE_DATE() {
        return LAST_DONATE_DATE;
    }
}
