package com.Service;

/**
 * Created by z on 2017/4/21.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bean.Msg;
import com.example.z.myproject.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jared on 16/2/10.
 */
public class MsgAdapter extends ArrayAdapter<Msg> {
    private int resourceId;
    List<String>mylist;
    Context mcontext;


    public MsgAdapter(Context context, int textViewResourceId, List<Msg> objects) {
        super(context, textViewResourceId, objects);
        mcontext=context;
        resourceId = textViewResourceId;
        mylist=new ArrayList<String>();



    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = getItem(position);
        mylist=msg.getClick_str();

        Log.e("lyd","list"+mylist.size());
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.left_chat= (LinearLayout) view.findViewById(R.id.chat_left);
            viewHolder.right_chat= (LinearLayout) view.findViewById(R.id.chat_right);
            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_layout);
            viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);
            viewHolder.head1 = (ImageView)view.findViewById(R.id.head_left);
            viewHolder.head2 = (ImageView)view.findViewById(R.id.head_right);
            viewHolder.left_list= (ListView) view.findViewById(R.id.chat_click_list);
            viewHolder.chat_history= (LinearLayout) view.findViewById(R.id.chat_history);
            viewHolder.liner_time= (LinearLayout) view.findViewById(R.id.liner_time);
            viewHolder.chat_time= (TextView) view.findViewById(R.id.chat_time);
            viewHolder.service_name= (TextView) view.findViewById(R.id.service_name);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(msg.getType() == Msg.TYPE_RECEIVED) {
            viewHolder.right_chat.setVisibility(View.GONE);
            viewHolder.left_chat.setVisibility(View.VISIBLE);
            viewHolder.liner_time.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.head1.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.head2.setVisibility(View.GONE);
            viewHolder.chat_history.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
            viewHolder.service_name.setText(msg.getName());
            if(mylist.isEmpty())
            {
                viewHolder.left_list.setVisibility(View.GONE);
            }
            else {

                ClickStrAdapter myclickadapter=new ClickStrAdapter(mcontext,R.layout.chat_click_item,mylist);
                myclickadapter.setMyOnNotifyListener(new ClickStrAdapter.OnNotifyListener() {
                    @Override
                    public void onNotifyChanged(String addstr) {
                        myOnNotifyListener.onNotifyChanged(addstr);
                    }
                });
                viewHolder.left_list.setAdapter(myclickadapter);
                setListViewHeightBasedOnChildren(viewHolder.left_list);
                viewHolder.left_list.setVisibility(View.VISIBLE);

            }
        } else if(msg.getType() == Msg.TYPE_SEND) {
            viewHolder.left_chat.setVisibility(View.GONE);
            viewHolder.right_chat.setVisibility(View.VISIBLE);
            viewHolder.liner_time.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.head2.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.head1.setVisibility(View.GONE);
            viewHolder.chat_history.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getContent());

        }
        else if(msg.getType()==Msg.TYPE_HISTORY)
        {
            viewHolder.right_chat.setVisibility(View.GONE);
            viewHolder.left_chat.setVisibility(View.GONE);
            viewHolder.liner_time.setVisibility(View.GONE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.head1.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.head2.setVisibility(View.GONE);
            viewHolder.chat_history.setVisibility(View.VISIBLE);
        }
     else if(msg.getType()==Msg.TYPR_tIME)
        {
            viewHolder.right_chat.setVisibility(View.GONE);
            viewHolder.left_chat.setVisibility(View.GONE);
            viewHolder.liner_time.setVisibility(View.VISIBLE);

            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.head1.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.head2.setVisibility(View.GONE);
            viewHolder.chat_history.setVisibility(View.GONE);
            viewHolder.chat_time.setText(msg.getContent());
        }
        return view;
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView head1;
        ImageView head2;
        ListView left_list;
        LinearLayout chat_history;
        LinearLayout liner_time;
        TextView chat_time;
        TextView service_name;
        LinearLayout left_chat;
        LinearLayout right_chat;
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
