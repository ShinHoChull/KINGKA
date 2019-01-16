package com.gc.dto;

public class InfoDTO {

    private String sid;
    private String subject;
    private String linkurl;
    private String file1;

    public InfoDTO(String sid, String subject, String linkurl, String file1) {
        this.sid = sid;
        this.subject = subject;
        this.linkurl = linkurl;
        this.file1 = file1;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getSid() {
        return sid;
    }

    public String getSubject() {
        return subject;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public String getFile1() {
        return file1;
    }
}
