package com.Service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.z.myproject.BaseActivity;
import com.example.z.myproject.R;
import com.example.z.myproject.Search_Detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2017/5/11.
 */

public class Problem_list extends BaseActivity{

    private List<Map<String,Object>> list;
    private SimpleAdapter list_adapter;
    String[]pro=new String[]{"门牌证办理流程","门牌号申请流程","社团管理流程","低收入家庭认证办理","困难家庭社会援助"};
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);
        setTitle("民政服务问题");
        listView= (ListView) findViewById(R.id.ques_detail_list);
        String [] from ={"image","text"};
        int []to={R.id.pro_icon,R.id.pro_text};
        list=new ArrayList<Map<String, Object>>();
        getListData();;
        list_adapter=new SimpleAdapter(this,list,R.layout.problem_layout,from,to);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.setClass(mContext,Search_Detail.class);
                intent.putExtra("title","门牌证办理流程");
                intent.putExtra("content","1、城镇居民办理：需带房屋的土地证或房产证（二者带一）及城镇居民户口本、身份证办理；\n" +
                        "2、门店出租，在后面居住：需带房屋的土地证或房产证（二者带一）及城镇居民户口本，办理临街门店牌和住户牌两种；\n" +
                        "3、购买二手房：需持原门牌证、过户证明及本人户口本和房产证办理；\n" +
                        "4、门牌证遗失：需持房屋的土地证或房产证（二者带一）及本人户口本、身份证办理；\n" +
                        "5、既无土地证，又未办理房产证：楼房需带房产交费手续、本人身份证办理；平房需带单位或居委会的证明信、身份证、户口本办理；\n" +
                        "6、新建小区居民入住：需由开发商提供小区居民的相关材料到县民政局地名办统一办理。");
                intent.putExtra("date","2017-4-21 18:10:13");
                startActivity(intent);
            }
        });

        listView.setAdapter(list_adapter);

    }

    public List<Map<String, Object>> getListData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("image", R.mipmap.icon_ques);
            map.put("text", pro[i]);
            list.add(map);
        }

        return list;
    }
}
