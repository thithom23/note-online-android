package com.thom;

// lớp lưu đường dẫn API
public class Constant {
    public static final String BASE_URL="http://online-note-qnu.herokuapp.com/api";
    public static final String ACCOUNT_URL = BASE_URL+"/account";
    public static final String LOGIN_URL = ACCOUNT_URL + "/login/";
    public static final String ACCOUNT_GET_INFO_URL = ACCOUNT_URL + "/get-info/";
    public static final String ACCOUNT_CREATE_ACCOUNT = ACCOUNT_URL + "/create-account";
    public static final String ACCOUNT_UPDATE_INFO = ACCOUNT_URL + "/update-info";


    public static final String NOTE_URL = BASE_URL+"/note";
    public static final String NOTE_URL_GET_ALL = NOTE_URL + "/get-all/";
    public static final String NOTE_URL_CREATE=NOTE_URL+"/create-note";
    public static final String NOTE_URL_UPDATE=NOTE_URL+"/update-note";
    public static final String NOTE_URL_DELETE=NOTE_URL+"/delete-note/";


    public static String NAME="";

    public Constant() {
    }
}
