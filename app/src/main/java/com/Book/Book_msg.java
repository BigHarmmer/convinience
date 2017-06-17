package com.Book;

/**
 * Created by z on 2017/5/9.
 */

public class Book_msg {
    String title;
    String idCard;
    String code;
    String time;
    String place;


    public Book_msg()
    {

    }

    public Book_msg(String place, String time, String code, String idCard, String title) {
        this.place = place;
        this.time = time;
        this.code = code;
        this.idCard = idCard;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
