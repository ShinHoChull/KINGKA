package com.gc.dto;

import java.io.Serializable;

public class MainGetPhotoDTO implements Serializable {

    private String url;

    public MainGetPhotoDTO(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
