package com.Book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;

import java.util.List;

/**
 * Created by z on 2017/5/8.
 */

public class Person_list extends BaseActivity implements View.OnClickListener{

    RelativeLayout addperson;
    Save mysave;
    Person_Adapter myadapter;
   ListView lv;
    List<Book_person>mylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_list);
        initview();

    }
    private void initview(){
       lv= (ListView) findViewById(R.id.person_list);

        addperson= (RelativeLayout) findViewById(R.id.new_person);
        addperson.setOnClickListener(this);
        mysave=new Save(this);
        mylist=mysave.getDataList("save");

        myadapter=new Person_Adapter(mylist,this);

        lv.setAdapter(myadapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Person_list.this)
                        .setTitle("系统提示")//设置对话框标题
                        .setIcon(R.mipmap.logo)

                        .setMessage("确认删除此条数据？")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮



                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                // TODO Auto-generated method stub
                                mylist=mysave.getDataList("save");
                                mylist.remove(position);
                                mysave.setDataList("save",mylist);
                                myadapter=new Person_Adapter(mylist,mContext);
                                lv.setAdapter(myadapter);


                            }

                        }).setNegativeButton("返回",new DialogInterface.OnClickListener() {//添加返回按钮

                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件

                    }

                }).show();//
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.new_person:
                Intent intent=new Intent(this,Edit_msg.class);
                startActivity(intent);
                break;

        }
    }
}
