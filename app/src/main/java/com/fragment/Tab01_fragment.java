package com.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.z.myproject.R;
import com.example.z.myproject.SearchActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by z on 2017/4/14.
 */

public class Tab01_fragment extends Fragment implements View.OnClickListener,OnItemClickListener{


    SharedPreferences sp=null;
    PopupMenu popupMenu;
    ImageView seal_more;

    ImageView new_img1,new_img2,new_img3,new_img4,new_img5,new_img6;
    TextView zixun1,zixun2,zixun3,zixun4,zixun5,zixun6;
    ImageView serch;
    private SimpleAdapter grid_adapter;
    ConvenientBanner cb;
    List<BannerItem>datalist;
    protected ImageLoader imageLoader;
    private List<Map<String, Object>> grid_list;
MyGridView gridView;
    private String texts[]={"徐立毅主持召开市政府常务会议","杭州召开一季度全市开放型经济形势分析会","我市召开百项千亿防洪排涝工程推进会"};
    private String imges[]={"http://zjjcmspublic.oss-cn-hangzhou.aliyuncs.com/jcms_files/jcms1/web149/site/picture/0/1704280908287228368.jpg",
            "http://zjjcmspublic.oss-cn-hangzhou.aliyuncs.com/jcms_files/jcms1/web149/site/picture/0/1704280918236882527.jpg",
            "http://zjjcmspublic.oss-cn-hangzhou.aliyuncs.com/jcms_files/jcms1/web149/site/picture/0/1704271644497497569.jpg"
    };

    private int[]icon={R.mipmap.gridview_bus,R.mipmap.gridview_car,R.mipmap.gridview_edu,R.mipmap.gridview_law,R.mipmap.gridview_life,
                        R.mipmap.gridview_pay,R.mipmap.gridview_progress,R.mipmap.gridview_property,R.mipmap.gridview_socail,R.mipmap.gridview_more};
    private String[] iconName = { "公交", "机动车", "教育", "法律", "生活", "公积金", "进度",
            "动产", "社保", "更多" };

    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.tab_01,container,false);
        sp =getActivity().getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

        initview();
        initevent();

        return v;

    }

    @Override
    public void onItemClick(int position) {

    }

    class BannerItem{
        String text;
        String uri;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    private void initview()
    {


        seal_more= (ImageView) v.findViewById(R.id.seal_more);
        new_img1= (ImageView) v.findViewById(R.id.img_zixun1);
        new_img2= (ImageView) v.findViewById(R.id.img_zixun2);
        new_img3= (ImageView) v.findViewById(R.id.img_zixun3);
        new_img4= (ImageView) v.findViewById(R.id.img_zixun4);
        new_img5= (ImageView) v.findViewById(R.id.img_zixun5);
        new_img6= (ImageView) v.findViewById(R.id.img_zixun6);

        setimg();
        zixun1= (TextView) v.findViewById(R.id.zixun1);
        zixun2= (TextView) v.findViewById(R.id.zixun2);
        zixun3= (TextView) v.findViewById(R.id.zixun3);
        zixun4= (TextView) v.findViewById(R.id.zixun4);
        zixun5= (TextView) v.findViewById(R.id.zixun5);
        zixun6= (TextView) v.findViewById(R.id.zixun6);
        setText();
        serch= (ImageView) v.findViewById(R.id.ac_iv_search);
        imageLoader = ImageLoader.getInstance();
        gridView= (MyGridView) v.findViewById(R.id.first_gridview);
        cb= (ConvenientBanner) v.findViewById(R.id.convenientBanner);

        grid_list = new ArrayList<Map<String, Object>>();
        getData();
        String [] from ={"image","text"};
        int [] to = {R.id.gird_image,R.id.grid_text};
        grid_adapter = new SimpleAdapter(getActivity(), grid_list, R.layout.gridview_item, from, to);
        //配置适配器
        gridView.setAdapter(grid_adapter);




        datalist=new ArrayList<BannerItem>();
        for(int i=0;i<imges.length;i++)
        {
            BannerItem item=new BannerItem();
            item.setText(texts[i].toString());

            item.setUri(imges[i]);
            datalist.add(item);
        }


        try {
            cb.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }


            },datalist).setPointViewVisible(true)

                    .setPageIndicator(new int[]{R.drawable.circle_normal,R.drawable.circle_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                    .setOnItemClickListener(this)
                    .setManualPageable(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initevent()
    {

        serch.setOnClickListener(this);

        seal_more.setOnClickListener(this);


    }

    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<10;i++){
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("image",icon[i]);
            map.put("text", iconName[i]);
            grid_list.add(map);
        }

        return grid_list;
    }

    public class NetworkImageHolderView implements Holder<BannerItem> {
        private View view;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.banner_item, null, false);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, BannerItem data) {

            ((TextView)view.findViewById(R.id.banner_txt)).setText(data.getText());
            imageLoader.displayImage(data.getUri(),(ImageView)(view.findViewById(R.id.banner_img)));

        }
    }

    void setimg()
    {
        new_img1.setImageResource(R.mipmap.news_1);
        new_img2.setImageResource(R.mipmap.news_2);
        new_img3.setImageResource(R.mipmap.news_3);
        new_img4.setImageResource(R.mipmap.news_4);
        new_img5.setImageResource(R.mipmap.news_5);
        new_img6.setImageResource(R.mipmap.news_6);


    }

    void setText()
    {
        zixun1.setText("大标题1");
        zixun2.setText("大标题2");
        zixun3.setText("大标题3");
        zixun4.setText("大标题4");
        zixun5.setText("大标题5");
        zixun6.setText("大标题6");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.ac_iv_search:
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.seal_more:

                String loc=sp.getString("LOCATION","");
                Log.e("lyd","loc"+loc);
                popupMenu=new PopupMenu(getActivity(),v);
                popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
                popupMenu.getMenu().getItem(0).setTitle("当前位置："+loc);
                popupMenu.show();
                break;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cb.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        cb.startTurning(3000);
    }
}
