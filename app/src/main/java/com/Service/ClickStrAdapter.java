package com.Service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z.myproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2017/4/28.
 */

public class ClickStrAdapter extends BaseAdapter {


private Context mContext;
    private int recourceId;
    List<String>mylist=new ArrayList<String>();



    public ClickStrAdapter(Context mContext, int recourceId, List<String> mylist) {
        this.mContext = mContext;
        this.recourceId = recourceId;
        this.mylist = mylist;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder1 viewHolder;
        String str=mylist.get(position);

        if(convertView == null) {
            view = LayoutInflater.from(mContext).inflate(recourceId, null);
            viewHolder = new ViewHolder1();
            viewHolder.textView= (TextView) view.findViewById(R.id.check_click_tx);

            view.setTag(viewHolder);

        }
        else {
            view = convertView;
            viewHolder =( ViewHolder1) view.getTag();
        }
        viewHolder.textView.setText(str);
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv= (TextView) v.findViewById(R.id.check_click_tx);
                String result=tv.getText().toString();


                myOnNotifyListener.onNotifyChanged(result);


            }
        });


        return view;
    }


    class ViewHolder1 {

        TextView textView;
    }

    private OnNotifyListener myOnNotifyListener;

    public interface OnNotifyListener{
        void onNotifyChanged(String addstr);
    }
    public void setMyOnNotifyListener(OnNotifyListener myOnNotifyListener)
    {
        this.myOnNotifyListener=myOnNotifyListener;
    }

}
