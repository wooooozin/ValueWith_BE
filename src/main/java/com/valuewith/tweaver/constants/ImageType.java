package com.valuewith.tweaver.constants;

public enum ImageType {

    PROFILE("profile/"),
    THUMBNAIL("thumbnail/"),
    LOCATION("location/");

    private final String path;

    ImageType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
