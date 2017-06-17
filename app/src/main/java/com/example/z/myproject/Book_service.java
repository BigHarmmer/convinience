package com.example.z.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.Book.Book;
import com.Book.Cancel_book;
import com.Book.My_book;
import com.Book.Person_list;

/**
 * Created by z on 2017/5/6.
 */

public class Book_service extends BaseActivity implements View.OnClickListener{


    Intent intent;
    LinearLayout book,cancel_book,my_book,my_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_layout);
        setTitle("办事预约");

        initview();

    }

    private void initview(){

        book= (LinearLayout) findViewById(R.id.book);
        cancel_book= (LinearLayout) findViewById(R.id.cancel_book);
        my_book= (LinearLayout) findViewById(R.id.my_book);
        my_info= (LinearLayout) findViewById(R.id.my_info);

        book.setOnClickListener(this);
        cancel_book.setOnClickListener(this);
        my_book.setOnClickListener(this);
        my_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.book:

                intent=new Intent(this, Book.class);
                startActivity(intent);
                break;
            case R.id.cancel_book:
                intent=new Intent(this, Cancel_book.class);
                startActivity(intent);

                break;
            case R.id.my_book:
                intent=new Intent(this, My_book.class);
                startActivity(intent);

                break;
            case R.id.my_info:
                intent=new Intent(this, Person_list.class);
                startActivity(intent);
                break;



        }
    }
}
