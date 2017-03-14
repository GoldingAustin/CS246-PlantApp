package com.cs246.plantapp;

import java.util.List;

/**
 * Created by austingolding on 2/20/17.
 */

public class PlantsObject {
    public static boolean isNameValid(String name) {
        return name.contains("@");
    }

    public PlantsObject(String name, String soilPH, String category, String spacing, String sunExpo, String waterReq, String image) {
        this.name = name;
        this.soilPH = soilPH;
        this.category = category;
        this.spacing = spacing;
        this.sunExpo = sunExpo;
        this.waterReq = waterReq;
        this.image = image;
    }

    public PlantsObject() {}

    public PlantsObject(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSoilPH(String soilPH) {
        this.soilPH = soilPH;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    public void setSunExpo(String sunExpo) {
        this.sunExpo = sunExpo;
    }

    public void setWaterReq(String waterReq) {
        this.waterReq = waterReq;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String name;
    private String soilPH;
    private String category;
    private String spacing;
    private String sunExpo;
    private String waterReq;
    private String image;
    private List<Boolean> checkDays;

    public List<Boolean> getCheckDays() {
        return checkDays;
    }

    public void setCheckDays(List<Boolean> checkDays) {
        this.checkDays = checkDays;
    }

    public String getName() {
        return name;
    }

    public String getSoilPH() {
        return soilPH;
    }

    public String getCategory() {
        return category;
    }

    public String getSpacing() {
        return spacing;
    }

    public String getSunExpo() {
        return sunExpo;
    }

    public String getWaterReq() {
        return waterReq;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "PlantsObject{" +
                "name='" + name + '\'' +
                ", soilPH='" + soilPH + '\'' +
                ", category='" + category + '\'' +
                ", spacing='" + spacing + '\'' +
                ", sunExpo='" + sunExpo + '\'' +
                ", waterReq='" + waterReq + '\'' +
                ", image='" + image + '\'' +
                ", checkDays=" + checkDays +
                '}';
    }
}
