package com.thom.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Account {
    private Long id;
    private String password;
    private String name;
    private String birthDay;
    private String createDate;
    private String updateDate;
    // map dữ liệu từ JSONObject của API sang kiểu dữ liệu Account
    public  Account(JSONObject jsonObject) throws JSONException, UnsupportedEncodingException {
        this.id= jsonObject.getLong("id");
        this.name=new String(jsonObject.getString("name").getBytes("ISO-8859-1"),"UTF-8");
        this.birthDay=parseDate(jsonObject.getString("birthDay"));
        this.createDate=jsonObject.getString("createDate");
        this.updateDate=jsonObject.getString("updateDate");
    }
    private String parseDate(String time) {
        try {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(time.substring(0, 19));
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
            return formater.format(date2);
        } catch (Exception e) {
            Log.d("parse error",e.toString());
        }
        return null;
    }
    public Account() {
    }
    @Override
    public String toString() {
        return
                "id=" + id +
                        ", password='" + password + '\'' +
                        ", name='" + name + '\'' +
                        ", birthDay=" + birthDay +
                        ", createDate=" + createDate +
                        ", updateDate=" + updateDate;
    }
    public Account(Long id, String password, String name, String birthDay, String createDate, String updateDate) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.birthDay = birthDay;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
