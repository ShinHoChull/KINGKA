package com.gc.dto;

public class SponsorDTO {

    private String sid;
    private String subject;
    private String linkurl;
    private String file;

    public SponsorDTO(String sid, String subject, String linkurl, String file) {
        this.sid = sid;
        this.subject = subject;
        this.linkurl = linkurl;
        this.file = file;
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

    public void setFile(String file) {
        this.file = file;
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

    public String getFile() {
        return file;
    }
}
