package com.Book;

/**
 * Created by z on 2017/5/8.
 */

public class Book_person {

    private String name;
   private String phone;
    private String idCard;

    public Book_person(String name, String phone, String idCard) {
        this.name = name;
        this.phone = phone;
        this.idCard = idCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
