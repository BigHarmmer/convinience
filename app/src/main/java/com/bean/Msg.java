package com.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/4/21.
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SEND = 1;
    public static final int TYPE_HISTORY=2;
    public static final int TYPR_tIME=3;

    private String content;
    private String name;
    private List<String>click_str=new ArrayList<String>();
    private int type;


    public Msg(int type)
    {
        this.type = type;
    }
    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }
    public Msg(String content,String name,int type)
    {
        this.content = content;
        this.type = type;
        this.name=name;
    }

    public List<String> getClick_str() {
        return click_str;
    }

    public void setClick_str(List<String> click_str) {
        this.click_str = click_str;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public String getName()
    {
        return name;
    }
}