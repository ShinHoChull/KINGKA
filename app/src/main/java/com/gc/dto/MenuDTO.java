package com.gc.dto;

public class MenuDTO {

    private String sid;
    private String name;
    private String main;
    private String introduce;
    private String photo;
    private String raw;

    public MenuDTO(String sid, String name) {
        this.sid = sid;
        this.name = name;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getSid() {
        return sid;
    }

    public String getMain() {
        return main;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getPhoto() {
        return photo;
    }

    public String getRaw() {
        return raw;
    }

    public String getName() {
        return name;
    }
}
