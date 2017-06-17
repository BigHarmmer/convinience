package com.Book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.z.myproject.R;

import java.util.List;

/**
 * Created by z on 2017/5/8.
 */

public class Book_Cancel_Adapter extends BaseAdapter{

    List<Book_msg>mlist;
    Context mcontext;

    public Book_Cancel_Adapter(List<Book_msg> mlist,Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder vh1;

        if(convertView==null)
        {
            convertView= LayoutInflater.from(mcontext).inflate(R.layout.my_cancel_item,null);
            vh1=new ViewHolder();
            vh1.title= (TextView) convertView.findViewById(R.id.my_cancel_title);
            vh1.phone= (TextView) convertView.findViewById(R.id.my_cancel_phone);
            vh1.code= (TextView) convertView.findViewById(R.id.my_cancel_code);
            vh1.time= (TextView) convertView.findViewById(R.id.my_cancel_time);
            vh1.place= (TextView) convertView.findViewById(R.id.my_cancel_place);
            vh1.cancel= (LinearLayout) convertView.findViewById(R.id.do_cancel);
            convertView.setTag(vh1);
        }
        else
        {
            vh1= (ViewHolder) convertView.getTag();
        }

        Book_msg msg=mlist.get(position);

        vh1.title.setText(msg.getTitle());
        vh1.phone.setText(msg.getIdCard());
        vh1.code.setText(msg.getCode());
        vh1.time.setText(msg.getTime());
        vh1.place.setText(msg.getPlace());
        vh1.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myOnNotifyListener.onNotifyChanged(position);
            }
        });


        return convertView;

    }
    class ViewHolder{
        public TextView title;
        public TextView phone;
        public TextView code;
        public TextView time;
        public TextView place;
        public LinearLayout cancel;

    }
    private OnNotifyListener myOnNotifyListener;

    public interface OnNotifyListener{
        void onNotifyChanged(int pos);
    }
    public void setMyOnNotifyListener(OnNotifyListener myOnNotifyListener)
    {
        this.myOnNotifyListener=myOnNotifyListener;
    }
}
