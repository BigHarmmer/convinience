package com.example.z.myproject;

import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidumap.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.List;


/**
 * Created by z on 2017/4/4.
 */

public class Map_activity extends Activity implements View.OnClickListener,OnGetRoutePlanResultListener{

    RouteLine route = null;
    int nodeIndex=-1;
    //检索类型

    //标记显示覆盖点
    boolean flag=false;
    int nowSearchType = -1;
    RelativeLayout v;
    View popupview;
    //地图界面
    MapView mMapView = null;

    String now_City;
    PopupWindow popupWindow;
    int searchType = 0;  // 搜索的类型，在显示时区分
    //点击回到当前位置

    TextView overlay_info;
    ImageView loc;
    //查找便民点
    ImageView find_conv;
    //记录经纬度
    private double latitude;
    private double longtitude;
    //是否首次进入
    private boolean isFirstIn=true;
    LocationClient mLocationClient;
    MyLocationListener myLocationListener;
    BaiduMap mbaiduMap;
    //设置定位方向图标
    BitmapDescriptor myLocationIcon;
    //传感器
    private MyorientationListener myorientationListener;
    //poi检索
    PoiSearch mPoiSearch;
    //当前方向
    private float currentX;

    String nowAdd="";
    String targetAdd="";
    MapStatusUpdate msu;
    LatLng latLng;

    LatLng center ;
    int radius = 5000;
//    LatLng southwest;
//    LatLng northeast;
    LatLngBounds searchbound;
    //规划路线
    ImageView walk;
    ImageView car;
    ImageView bus;


    WalkingRouteResult nowResultwalk = null;
    TransitRouteResult nowResultransit = null;
    DrivingRouteResult nowResultdrive = null;

   // Button mBtnPre = null; // 上一个节点
   // Button mBtnNext = null; // 下一个节点

    // 搜索相关
    RoutePlanSearch mSearch = null;

    boolean hasShownDialogue = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mbaiduMap= mMapView.getMap();
        mbaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        MapStatusUpdate msu =MapStatusUpdateFactory.zoomTo(15);
        mbaiduMap.setMapStatus(msu);

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        initLocation();
        initview();
        findplace();
    }

    private void findplace() {
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {

                if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                    Toast.makeText(Map_activity.this, "未找到结果", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    mbaiduMap.clear();
                    PoiOverlay overlay = new MyPoiOverlay(mbaiduMap);
                    mbaiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result);
                    overlay.addToMap();
                    overlay.zoomToSpan();



                    switch( searchType ) {
                        case 2:
                            showNearbyArea(center, radius);
                            break;
                        case 3:
                            showBound(searchbound);
                            break;

                        default:
                            break;
                    }

                    return;
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                    // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                    String strInfo = "在";
                    for (CityInfo cityInfo : result.getSuggestCityList()) {
                        strInfo += cityInfo.city;
                        strInfo += ",";
                    }
                    strInfo += "找到结果";
                    Toast.makeText(Map_activity.this, strInfo, Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {

                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(Map_activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {

                    latLng=new LatLng(result.getLocation().latitude,result.getLocation().longitude);
                    msu= MapStatusUpdateFactory.newLatLng(latLng);
                    mbaiduMap.animateMapStatus(msu);


                    overlay_info.setText(result.getName()+":"+result.getAddress());

                    targetAdd=result.getName();
                    showpopwindow();
                }
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

//        mPoiSearch.searchInCity(new PoiCitySearchOption().city("杭州").keyword("便民中心").pageNum());
//           searchType = 2;
//            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword("便民中心").sortType(PoiSortType.distance_from_near_to_far).location(center)
//                    .radius(radius).pageNum(10);
//            mPoiSearch.searchNearby(nearbySearchOption);



    }

    public void initview()
    {
        v= (RelativeLayout) findViewById(R.id.map_view);


        loc= (ImageView) findViewById(R.id.loc);
        loc.setOnClickListener(this);
        find_conv= (ImageView) findViewById(R.id.find_conv);
        find_conv.setOnClickListener(this);
        popupview= View.inflate(this,R.layout.overlay_pop,null);
        overlay_info= (TextView) popupview.findViewById(R.id.overlay_info);
        walk= (ImageView) popupview.findViewById(R.id.walk);
        car= (ImageView) popupview.findViewById(R.id.car);
        bus= (ImageView) popupview.findViewById(R.id.bus);
        walk.setOnClickListener(this);
        car.setOnClickListener(this);
        bus.setOnClickListener(this);
    }
    public void initLocation()
    {
        mLocationClient=new LocationClient(this);

        myLocationListener=new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option=new LocationClientOption();
        //设置坐标类型
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");

        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //请求时间间隔
        option.setScanSpan(1000);

        mLocationClient.setLocOption(option);
        myLocationIcon= BitmapDescriptorFactory.fromResource(R.mipmap.arrow);

        myorientationListener=new MyorientationListener(Map_activity.this);
        myorientationListener.setMyOnOrientationListener(new MyorientationListener.OnOrientationListener() {
            @Override
            public void OnOrientationChanged(float x) {

                currentX=x;

            }
        });
    }

    @Override
    public void onClick(View v) {

        //mBtnPre.setVisibility(View.INVISIBLE);
        //mBtnNext.setVisibility(View.INVISIBLE);
        // 设置起终点信息


        PlanNode stNode = PlanNode.withLocation(center);
        PlanNode enNode = PlanNode.withLocation(latLng);
        switch (v.getId())
        {
            case R.id.loc:
                find_loc();
                break;
            case R.id.find_conv:
                if(flag==false) {
                    find_in_bound();
                    flag=true;
                    break;
                }else
                {
                    mMapView.getMap().clear();
                    flag=false;
                    break;
                }


            case R.id.walk:

                popupWindow.dismiss();

                mbaiduMap.clear();
                mSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(stNode).to(enNode));
                nowSearchType = 1;

                break;
            case R.id.car:

                popupWindow.dismiss();
                mbaiduMap.clear();
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
                nowSearchType = 2;

                break;
            case R.id.bus:

                popupWindow.dismiss();
                mbaiduMap.clear();
                mSearch.transitSearch((new TransitRoutePlanOption())
                        .from(stNode).city(now_City).to(enNode));
                nowSearchType = 3;
                break;

        }

    }



    OverlayManager routeOverlay = null;
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(Map_activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
             //result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            //mBtnPre.setVisibility(View.VISIBLE);
            //mBtnNext.setVisibility(View.VISIBLE);

            if (result.getRouteLines().size() > 1) {
                nowResultwalk = result;
                if (!hasShownDialogue) {
                    MyTransitDlg myTransitDlg = new MyTransitDlg(Map_activity.this,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.WALKING_ROUTE);
                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShownDialogue = false;
                        }
                    });
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultwalk.getRouteLines().get(position);
                            WalkingRouteOverlay overlay = new WalkingRouteOverlay(mbaiduMap);
                            mbaiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultwalk.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                    hasShownDialogue = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mbaiduMap);
                mbaiduMap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        Toast.makeText(this,"抱歉，该功能正在完善中",Toast.LENGTH_SHORT);
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(Map_activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;


            if (result.getRouteLines().size() > 1) {
                nowResultdrive = result;
                if (!hasShownDialogue) {
                    MyTransitDlg myTransitDlg = new MyTransitDlg(Map_activity.this,
                            result.getRouteLines(),
                            RouteLineAdapter.Type.DRIVING_ROUTE);
                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShownDialogue = false;
                        }
                    });
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultdrive.getRouteLines().get(position);
                            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mbaiduMap);
                            mbaiduMap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultdrive.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                    hasShownDialogue = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(mbaiduMap);
                routeOverlay = overlay;
                mbaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                //mBtnPre.setVisibility(View.VISIBLE);
                //mBtnNext.setVisibility(View.VISIBLE);
            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    private class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {


            MyLocationData data=new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .direction(currentX)
                    .build();

            //更新经纬度
            latitude=bdLocation.getLatitude();
            longtitude=bdLocation.getLongitude();
            bdLocation.getCity();

            nowAdd=bdLocation.getAddrStr();

            mbaiduMap.setMyLocationData(data);

            MyLocationConfiguration config=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,myLocationIcon);
            mbaiduMap.setMyLocationConfigeration(config);
            if(isFirstIn)
            {
                find_loc();
                Toast.makeText(Map_activity.this,"当前位置："+bdLocation.getAddrStr(),Toast.LENGTH_SHORT).show();
                now_City=bdLocation.getCity();
                isFirstIn=false;
            }


        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void find_in_bound() {
        searchType = 2;

        center=new LatLng(latitude,longtitude);
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword("便民中心")
                .sortType(PoiSortType.distance_from_near_to_far).location(center)
                .radius(radius).pageCapacity(10).pageNum(0);
        mPoiSearch.searchNearby(nearbySearchOption);

    }

    private void find_loc() {
        latLng=new LatLng(latitude,longtitude);
        msu= MapStatusUpdateFactory.newLatLng(latLng);
        mbaiduMap.animateMapStatus(msu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mbaiduMap.setMyLocationEnabled(true);

            mLocationClient.start();
        myorientationListener.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mbaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        myorientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mPoiSearch.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);

            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));

            return true;
        }
    }
    /**
     * 对周边检索的范围进行绘制
     * @param center
     * @param radius
     */
    public void showNearbyArea( LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mbaiduMap.addOverlay(ooMarker);


//        OverlayOptions ooCircle = new CircleOptions().fillColor( 0x00000000 )
//                .center(center).stroke(new Stroke(5, 0x00000000 ))
//                .radius(radius);
//        mbaiduMap.addOverlay(ooCircle);
    }

    /**
     * 对区域检索的范围进行绘制
     * @param bounds
     */
    public void showBound( LatLngBounds bounds) {
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_geo);

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mbaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mbaiduMap.setMapStatus(u);

        bdGround.recycle();
   }
    public void showpopwindow() {


        popupWindow = new PopupWindow(Map_activity.this);


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow
                .setWidth(getWindowManager().getDefaultDisplay().getWidth() );
        popupWindow.setHeight(500);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setContentView(popupview);

        popupWindow.showAtLocation(v,Gravity.TOP,0,0);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void setOnDismissListener(OnDismissListener listener) {
            super.setOnDismissListener(listener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                   // mBtnPre.setVisibility(View.VISIBLE);
                    //mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();
                    hasShownDialogue = false;
                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }



    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }


}
