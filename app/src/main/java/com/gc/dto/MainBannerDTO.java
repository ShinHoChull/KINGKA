package com.gc.dto;

public class MainBannerDTO {

    private String sid;
    private String subject;
    private String linkurl;
    private String file1;
    private String file2;
    private String file3;


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

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public void setFile3(String file3) {
        this.file3 = file3;
    }

    public String getFile1() {
        return file1;
    }

    public String getFile2() {
        return file2;
    }

    public String getFile3() {
        return file3;
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

}
