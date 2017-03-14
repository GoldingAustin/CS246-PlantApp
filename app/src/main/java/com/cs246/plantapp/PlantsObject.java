package com.cs246.plantapp;

import java.util.List;

/**
 * Created by austingolding on 2/20/17.
 */
public class PlantsObject {
    /**
     * Is name valid boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public static boolean isNameValid(String name) {
        return name.contains("@");
    }

    /**
     * Instantiates a new Plants object.
     *
     * @param name     the name
     * @param soilPH   the soil ph
     * @param category the category
     * @param spacing  the spacing
     * @param sunExpo  the sun expo
     * @param waterReq the water req
     * @param image    the image
     */
    public PlantsObject(String name, String soilPH, String category, String spacing, String sunExpo, String waterReq, String image) {
        this.name = name;
        this.soilPH = soilPH;
        this.category = category;
        this.spacing = spacing;
        this.sunExpo = sunExpo;
        this.waterReq = waterReq;
        this.image = image;
    }

    /**
     * Instantiates a new Plants object.
     */
    public PlantsObject() {}

    /**
     * Instantiates a new Plants object.
     *
     * @param name the name
     */
    public PlantsObject(String name) {
        this.name = name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets soil ph.
     *
     * @param soilPH the soil ph
     */
    public void setSoilPH(String soilPH) {
        this.soilPH = soilPH;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets spacing.
     *
     * @param spacing the spacing
     */
    public void setSpacing(String spacing) {
        this.spacing = spacing;
    }

    /**
     * Sets sun expo.
     *
     * @param sunExpo the sun expo
     */
    public void setSunExpo(String sunExpo) {
        this.sunExpo = sunExpo;
    }

    /**
     * Sets water req.
     *
     * @param waterReq the water req
     */
    public void setWaterReq(String waterReq) {
        this.waterReq = waterReq;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
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

    /**
     * Gets check days.
     *
     * @return the check days
     */
    public List<Boolean> getCheckDays() {
        return checkDays;
    }

    /**
     * Sets check days.
     *
     * @param checkDays the check days
     */
    public void setCheckDays(List<Boolean> checkDays) {
        this.checkDays = checkDays;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets soil ph.
     *
     * @return the soil ph
     */
    public String getSoilPH() {
        return soilPH;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets spacing.
     *
     * @return the spacing
     */
    public String getSpacing() {
        return spacing;
    }

    /**
     * Gets sun expo.
     *
     * @return the sun expo
     */
    public String getSunExpo() {
        return sunExpo;
    }

    /**
     * Gets water req.
     *
     * @return the water req
     */
    public String getWaterReq() {
        return waterReq;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
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
