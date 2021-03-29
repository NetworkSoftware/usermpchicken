package com.network.moeidbannerlibrary.banner;

public class BannerBean {
    String images, id,description;

    public BannerBean() {
    }

    public BannerBean(String images, String id,String description) {
        this.images = images;
        this.id = id;
        this.description=description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}