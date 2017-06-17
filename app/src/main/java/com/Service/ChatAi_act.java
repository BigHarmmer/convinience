package com.Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

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

public class ChatAi_act extends BaseActivity implements View.OnClickListener{
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList;
    private List<String> click_str;
    private List<String> click_res;
    Message_time msg_time;
    String name;
    PopupMenu popupMenu;
    Menu menu;

    Boolean isScore=false;
    Boolean isConn=false;
   // private List<Msg>cache_list;
    private chat_history history;
    Map<String,String> map=new HashMap<String, String>();
    Msg mymsg;
    Msg timemsg;
    int type;
    int pos;
    ImageView clear;
    ImageView voice;
    ImageView score;
    ImageView conn;
    ImageView sentence;

    private WebSocketClient webSocketClient;


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    addTime();
                    msgList.add(mymsg);
                    history.addDataList("save",mymsg);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    webSocketClient.send("#");
                    break;
            }
        }
    };
    String base_address = "ws://123.206.115.220:8080/bm/chat?username=sasa&id=";

    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_main);
    msg_time=new Message_time(mContext);

        Bundle bundle=this.getIntent().getExtras();
        String title=bundle.getString("theme");
        int id=bundle.getInt("id");
        type=bundle.getInt("type");
        pos=bundle.getInt("pos");
        Log.e("lyd","position"+pos);
        setTitle(title);
        address=base_address+id;
        initview();
        initevent();
        connect();

    }

    private void initview() {
        sentence= (ImageView) findViewById(R.id.sentence);
        conn= (ImageView) findViewById(R.id.connect);
        score= (ImageView) findViewById(R.id.score);
        voice= (ImageView) findViewById(R.id.voice);
        clear= (ImageView) findViewById(R.id.edit_clean);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);

        clear.setOnClickListener(this);
        voice.setOnClickListener(this);
        score.setOnClickListener(this);
        conn.setOnClickListener(this);
        sentence.setOnClickListener(this);

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

                    clear.setVisibility(View.GONE);

                    send.setClickable(false);
                    send.setEnabled(false);
                } else {

                    clear.setVisibility(View.VISIBLE);

                    send.setClickable(true);
                    send.setEnabled(true);
                }

            }
        });
        msgList = new ArrayList<Msg>();
        click_str = new ArrayList<String>();

        history=new chat_history(this,pos);
        msgList=history.getDataList("save");
        mymsg=new Msg(Msg.TYPE_HISTORY);
        msgList.add(mymsg);
        adapter = new MsgAdapter(ChatAi_act.this, R.layout.chat_ui, msgList);
        adapter.setMyOnNotifyListener(new MsgAdapter.OnNotifyListener() {
            @Override
            public void onNotifyChanged(String addstr) {
                Log.e("lyd", "return" + addstr);
                mymsg = new Msg(addstr, Msg.TYPE_SEND);
                history.addDataList("save",mymsg);
                msgList.add(mymsg);
                adapter.notifyDataSetChanged();
                mymsg=new Msg(map.get(addstr),"客服机器人",Msg.TYPE_RECEIVED);
                msgList.add(mymsg);
                history.addDataList("save",mymsg);
                adapter.notifyDataSetChanged();

            }
        });
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {

                    addTime();
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    history.addDataList("save",msg);
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
            URI uri = new URI(address);
            webSocketClient = new WebSocketClient(uri, draft) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                    if(type==2)
                    {
                        handler.sendEmptyMessage(1);
                    }
                }

                @Override
                public void onMessage(String s) {

                    Log.e("lyd", s.toString());
                      if(type==1)
                      {
                          Dealback1(s);
                      }
                    else {
                          Dealback2(s);
                      }
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

    private void Dealback1(String str) {
        JSONArray array = null;
        Msg msg;
       // cache_list=new ArrayList<Msg>();
        click_str=new ArrayList<>();
        click_res=new ArrayList<>();
        try {
            array = new JSONArray(str);
            Log.e("lyd",array.length()+"");
            if(array.length()==0)
            {
                msg=new Msg("小z不明白你在说什么,人工客服请点击工具栏","客服机器人",Msg.TYPE_RECEIVED);

            }
            else if(array.length()==1)
            {
                JSONObject item = array.getJSONObject(0);
                String content=item.getString("content");
                name=item.getString("from");
                isConn=true;
                Log.e("lyd",item.toString());
                msg=new Msg(content,name,Msg.TYPE_RECEIVED);


            }

            else {
                for (int i = 0; i < array.length(); i++) {


                    JSONObject item = array.getJSONObject(i);
                    String result_question = item.getString("question");
                    String result_answer = item.getString("answer");
                    click_str.add(result_question);
                    map.put(result_question, result_answer);

                }

                msg = new Msg("您可能遇到以下问题:", "客服机器人",Msg.TYPE_RECEIVED);
                msg.setClick_str(click_str);

            }
           // cache_list.add(msg);
            mymsg=msg;
            handler.sendEmptyMessage(0);



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void Dealback2(String str) {
        JSONArray array = null;
        Msg msg;
        // cache_list=new ArrayList<Msg>();
        click_str=new ArrayList<>();
        click_res=new ArrayList<>();
        try {
            array = new JSONArray(str);
            Log.e("lyd",array.length()+"");
            if(array.length()==0)
            {

            }
            else if(array.length()==1)
            {
                JSONObject item = array.getJSONObject(0);
                String content=item.getString("content");
                name=item.getString("from");
                isConn=true;
                Log.e("lyd",item.toString());
                msg=new Msg(content,name,Msg.TYPE_RECEIVED);
                mymsg=msg;
                handler.sendEmptyMessage(0);

            }

            else {


            }
            // cache_list.add(msg);



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
            case R.id.score:
               if (isScore==true)
               {
                   Toast.makeText(mContext,"您本次已进行评价",Toast.LENGTH_SHORT).show();
               }
                else {
                   if(isConn)
                   {
                       Intent intent=new Intent(mContext,Service_score.class);
                       Bundle bundle=new Bundle();
                       bundle.putString("name",name);
                       Log.e("lyd","name"+name);
                       bundle.putFloat("score",4.5f);
                       intent.putExtras(bundle);
                       startActivityForResult(intent,1);
                   }
                   else {
                       Toast.makeText(mContext,"无客服服务记录",Toast.LENGTH_SHORT).show();
                   }


               }
                break;

            case R.id.connect:
                webSocketClient.send("#");
                break;
            case R.id.sentence:
                popupMenu=new PopupMenu(mContext,v);
                menu=popupMenu.getMenu();
                menu.add(Menu.NONE,Menu.FIRST+0,0,"我需要带什么证件?");
                menu.add(Menu.NONE,Menu.FIRST+1,1,"我具体的办事流程是什么?");
                menu.add(Menu.NONE,Menu.FIRST+2,2,"申请完需要几个工作日?");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        inputText.setText(item.getTitle());
                        return false;
                    }
                });
                popupMenu.show();
                break;
        }
    }
    public void dovoice(View v)
    {
        Voice myvoice=new Voice(ChatAi_act.this,inputText,"0");
        myvoice.doVoice();


    }
    private void addTime()
    {
       if(msg_time.check_time()) {
           String content=msg_time.getTime();
           timemsg=new Msg(content,Msg.TYPR_tIME);
           msgList.add(timemsg);
           msg_time.save_time();
           history.addDataList("save",timemsg);

       }
        else {
           msg_time.save_time();
       }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==100)
        {
            isScore=true;
            Toast.makeText(mContext,"感谢您的评价",Toast.LENGTH_SHORT).show();
        }
    }
}
