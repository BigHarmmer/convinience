package com.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.z.myproject.R;

import java.util.List;

/**
 * Created by z on 2017/5/8.
 */

public class Person_Adapter extends BaseAdapter{

List<Book_person>mlist;
    Context mcontext;

    public Person_Adapter(List<Book_person> mlist,Context context) {
        this.mlist = mlist;
        mcontext=context;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder vh1;

        if(convertView==null)
        {
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.person_list_item,null);
            vh1=new ViewHolder();
           vh1.name= (TextView) convertView.findViewById(R.id.person_item_name);
            vh1.phone= (TextView) convertView.findViewById(R.id.person_item_phone);
            vh1.idcard= (TextView) convertView.findViewById(R.id.person_item_idcard);
            convertView.setTag(vh1);
        }
        else
        {
            vh1= (ViewHolder) convertView.getTag();
        }

        Book_person person=mlist.get(position);
        vh1.name.setText(person.getName());
        vh1.phone.setText(person.getPhone());
        vh1.idcard.setText(person.getIdCard());




        return convertView;

    }
    class ViewHolder{
        public TextView name;
        public TextView phone;
        public TextView idcard;

    }
}
