package com.example.vvallabb.calldetailsbackup.pojo;

/**
 * Created by vasanthnaib on 29/10/2014.
 */
public class CallDetailsRecord {

    public CallDetailsRecord(String number, String name, String duration, int type, String timestamp) {
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.type = type;
        this.timestamp = timestamp;
    }

    private String number;
    private String name;
    private String duration;
    private int type;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
