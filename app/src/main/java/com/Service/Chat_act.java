package com.Service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bean.Msg;
import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;
import com.z.util.Voice;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2017/4/27.
 */

public class Chat_act extends BaseActivity implements View.OnClickListener{
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList;
    private List<String> click_str;
    private List<String> click_res;
    private List<Msg>cache_list;
    Map<String,String> map=new HashMap<String, String>();
    Msg mymsg;
    ImageView clear;
    ImageView voice;
    private WebSocketClient webSocketClient;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    msgList.addAll(cache_list);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    webSocketClient.send("#");
                    break;
            }
        }
    };
    String base_address = "ws://192.168.1.107:8080/bm/chat?username=sasa&id=3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_main);
        initview();
        initevent();
        Bundle bundle=this.getIntent().getExtras();
        String title=bundle.getString("theme");
        int id=bundle.getInt("id");
        setTitle(title);

        connect();
        final Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    private void initview() {
        voice= (ImageView) findViewById(R.id.voice);
        clear= (ImageView) findViewById(R.id.edit_clean);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);

        clear.setOnClickListener(this);
        voice.setOnClickListener(this);

    }

    private void initevent() {
        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    voice.setVisibility(View.VISIBLE);
                    clear.setVisibility(View.GONE);
                    send.setVisibility(View.GONE);
                    send.setClickable(false);
                    send.setEnabled(true);
                } else {
                    voice.setVisibility(View.GONE);
                    clear.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    send.setClickable(true);
                    send.setEnabled(true);
                }

            }
        });
        msgList = new ArrayList<Msg>();
        click_str = new ArrayList<String>();
//
//        Msg msg = new Msg("你好，这里是客服小z，您有什么问题吗，我会为你寻找答案，如有需求点击", Msg.TYPE_RECEIVED);
//        click_str = msg.getClick_str();
//        click_str.add("人工服务");
//
//        msg.setClick_str(click_str);
//        msgList.add(0, msg);
        adapter = new MsgAdapter(Chat_act.this, R.layout.chat_ui, msgList);
        adapter.setMyOnNotifyListener(new MsgAdapter.OnNotifyListener() {
            @Override
            public void onNotifyChanged(String addstr) {
                Log.e("lyd", "return" + addstr);
                mymsg = new Msg(addstr, Msg.TYPE_SEND);
                msgList.add(mymsg);
                adapter.notifyDataSetChanged();
                mymsg=new Msg(map.get(addstr),Msg.TYPE_RECEIVED);
                msgList.add(mymsg);
                adapter.notifyDataSetChanged();

            }
        });
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();

                    inputText.setText("");
                    webSocketClient.send(content);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onStop() {
        super.onStop();
        webSocketClient.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    void connect() {
        try {
            Draft draft = new Draft_17();
            URI uri = new URI(base_address);
            webSocketClient = new WebSocketClient(uri, draft) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    Log.e("lyd", "open");
                }

                @Override
                public void onMessage(String s) {

                    Log.e("lyd", s.toString());
                    Dealback(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                    Log.e("lyd", "close");
                }

                @Override
                public void onError(Exception e) {

                    Log.e("lyd", e.toString());
                }
            };
            webSocketClient.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

    private void Dealback(String str) {
        JSONArray array = null;
        Msg msg;
        cache_list=new ArrayList<Msg>();

        try {
            array = new JSONArray(str);
            Log.e("lyd",array.length()+"");
                JSONObject item = array.getJSONObject(0);
                String content=item.getString("content");
                Log.e("lyd",content);
                msg=new Msg(content,Msg.TYPE_RECEIVED);

            cache_list.add(msg);
            handler.sendEmptyMessage(0);



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.edit_clean:
                inputText.setText("");
                break;
            case R.id.voice:

                dovoice(v);
                break;
        }
    }
    public void dovoice(View v)
    {
        Voice myvoice=new Voice(Chat_act.this,inputText,"0");
        myvoice.doVoice();


    }
}
