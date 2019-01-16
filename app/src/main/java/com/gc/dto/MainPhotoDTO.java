package com.gc.dto;

public class MainPhotoDTO {

    private String code;
    private String title;

    public MainPhotoDTO(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
