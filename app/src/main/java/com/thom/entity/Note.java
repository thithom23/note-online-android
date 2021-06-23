package com.thom.entity;

import android.util.Log;
import android.widget.Toast;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class Note {
    private Long id;
    private String note;
    private String createDate;
    private String updateDate;
    // map dữ liệu từ JSONObject của API sang kiểu dữ liệu Note
    public Note(JSONObject jsonObject) throws JSONException, UnsupportedEncodingException {
        this.id = jsonObject.getLong("id");
        this.note = new String(jsonObject.getString("note").getBytes("ISO-8859-1"),"UTF-8");
        this.createDate = parseDate(jsonObject.getString("createDate"));
        this.updateDate = parseDate(jsonObject.getString("updateDate"));
    }
    private String parseDate(String time) {
        try {
            Date date2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(time.substring(0, 19));
            SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return formater.format(date2);
        } catch (Exception e) {
            Log.d("parse error",e.toString());
        }

        return null;
    }
    public Note(Long id, String note, String createDate, String updateDate) {
        this.id = id;
        this.note = note;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }


    @Override
    public String toString() {
        return decodeEmoji(note);
    }
    public static String decodeEmoji (String message) {
        String myString= null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }
    public Note() {
    }

    public Long getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
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
