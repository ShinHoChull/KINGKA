package com.gc.dto;

public class NoticeDTO {

    private String sid;
    private String subject;

    public NoticeDTO(String sid, String subject) {
        this.sid = sid;
        this.subject = subject;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSid() {
        return sid;
    }

    public String getSubject() {
        return subject;
    }

}
