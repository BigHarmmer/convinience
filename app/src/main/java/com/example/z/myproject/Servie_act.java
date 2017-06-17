package com.example.z.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.Service.ChatAi_act;
import com.Service.Chat_act;
import com.Service.Problem_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2017/4/30.
 */

public class Servie_act extends BaseActivity implements View.OnClickListener{

    private List<Map<String, Object>> grid_list;
    private List<Map<String,Object>> list;
    private SimpleAdapter grid_adapter;
    private SimpleAdapter list_adapter;

    FloatingActionButton fab;
    ListView listview;
    GridView myGridView;

    String[]txt=new String[]{"住房服务","工商服务","残障服务","卫生服务","安全监管","教育服务","司法援助","更多"};

    String[]pro=new String[]{"民政服务问题","住房公积金问题","物业管理问题","社保问题","教育划片问题","机动车相关问题"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_layout);

        setTitle("客服咨询");
        initview();

    }
    private void initview()
    {
        fab= (FloatingActionButton) findViewById(R.id.float_service);
        fab.setOnClickListener(this);
        listview= (ListView) findViewById(R.id.normal_problem);
        String []from1={"image","text"};
        int []to1={R.id.pro_icon,R.id.pro_text};
        list=new ArrayList<Map<String, Object>>();
        getListData();
        list_adapter=new SimpleAdapter(this,list,R.layout.problem_layout,from1,to1);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Servie_act.this, Problem_list.class);
                startActivity(intent);

            }
        });
        listview.setAdapter(list_adapter);

        
        myGridView= (GridView) findViewById(R.id.service_gridview);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position!=7) {
                    String title = txt[position];

                    Intent intent = new Intent(Servie_act.this, ChatAi_act.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("theme", title);
                    bundle.putInt("id", 3);
                    bundle.putInt("pos",position);
                    bundle.putInt("type",1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                }
        });
        String [] from ={"image","text"};
        int [] to = {R.id.gird_image,R.id.grid_text};
        grid_list = new ArrayList<Map<String, Object>>();
        getGridData();
        grid_adapter = new SimpleAdapter(this, grid_list, R.layout.gridview_item, from, to);
        //配置适配器
        myGridView.setAdapter(grid_adapter);


    }




    public List<Map<String, Object>> getGridData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<8;i++){
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("image",R.mipmap.icon_service);
            map.put("text", txt[i]);
            grid_list.add(map);
        }

        return grid_list;
    }


    public List<Map<String, Object>> getListData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<6;i++){
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("image",R.mipmap.icon_ques);
            map.put("text", pro[i]);
            list.add(map);
        }

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.float_service:
                Intent intent = new Intent(Servie_act.this,ChatAi_act.class);
                Bundle bundle = new Bundle();
                bundle.putString("theme", "人工客服");
                bundle.putInt("id",3);
                bundle.putInt("type",2);
                bundle.putInt("pos",100);
                intent.putExtras(bundle);
                startActivity(intent);
        }
    }


}
