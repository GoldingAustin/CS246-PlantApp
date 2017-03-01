package com.cs246.plantapp;

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

    public PlantsObject(String name) {
        this.name = name;
    }

    private String name;
    private String soilPH;
    private String category;
    private String spacing;
    private String sunExpo;
    private String waterReq;
    private String image;


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


}
