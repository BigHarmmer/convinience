package com.example.z.myproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.network.UploadUtil;
import com.z.util.UpLoadAsycTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by z on 2017/4/17.
 */

public class Submit_activity extends Activity implements View.OnClickListener{

    EditText Edit_tittle;
    EditText Edit_content;
    EditText Edit_phone;
    EditText Edit_add;
    String title;
    String content;
    String phone;
    String username;
    String add;

    String path;
    Button submit;
    private GridView gridView1; // 网格显示缩略图
    private final int IMAGE_OPEN = 1; // 打开图片标记
    private String pathImage; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter; // 适配器
    int count=0;

    SharedPreferences sp=null;
    private List<String> list;
    String uploadFile = "";
    Intent intent;
    ImageView back;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                {
                    Toast.makeText(Submit_activity.this,"上传成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
                case 1:
                {
                    Toast.makeText(Submit_activity.this,"上传失败，请检查网络",Toast.LENGTH_SHORT).show();
                    break;
                }


            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*
		 * 防止键盘挡住输入框 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
		 * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
		 */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.layout_submit);


        sp = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        username=sp.getString("USERNAME","");

        initview();
        initevent();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initview()
    {
        back= (ImageView) findViewById(R.id.upload_back);
        gridView1= (GridView) findViewById(R.id.gridview);
        submit= (Button) findViewById(R.id.submit_qiuzhu);
        Edit_tittle= (EditText) findViewById(R.id.Edit_tittle);
        Edit_content= (EditText) findViewById(R.id.Edit_content);
        Edit_phone= (EditText) findViewById(R.id.Edit_phone);

        Edit_add= (EditText) findViewById(R.id.Edit_add);
        Edit_phone.setText(sp.getString("PHONE",""));

    }
    private void initevent()
    {
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        list = new ArrayList<>();

        bmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.timg); // 加号
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem,
                R.layout.griditem_addpic, new String[] { "itemImage" },
                new int[] { R.id.imageView1 });
		/*
		 * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如 map.put("itemImage",
		 * R.drawable.img); 解决方法: 1.自定义继承BaseAdapter实现 2.ViewBinder()接口实现
		 */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);

		/*
		 * 监听GridView点击事件 报错:该函数必须抽象方法 故需要手动导入import android.view.View;
		 */
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                    if (position == 0) { // 点击图片位置为+ 0对应0张图片
                        if(count>2)
                        {
                            Toast.makeText(Submit_activity.this,"最多添加3张图片",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Submit_activity.this, "添加图片",
                                    Toast.LENGTH_SHORT).show();
                            // 选择图片
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, IMAGE_OPEN);
                        }
                        // 通过onResume()刷新数据
                    } else {
                        dialog(position);

                        Toast.makeText(Submit_activity.this,
                                "点击第" + position + " 号图片", Toast.LENGTH_SHORT)
                                .show();
                    }
}


});
        }

// 刷新图片
    @Override
    protected void onResume() {
        super.onResume();

        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem,
                    R.layout.griditem_addpic, new String[] { "itemImage" },
                    new int[] { R.id.imageView1 });
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            // 刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }
    /*
     * Dialog对话框提示用户删除操作 position为删除图片位置
     */
    protected void dialog(final int position) {
        String string = "是否删除该图片";
        AlertDialog.Builder builder = new AlertDialog.Builder(Submit_activity.this);
        builder.setMessage(string);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                list.remove(position - 1);
                simpleAdapter.notifyDataSetChanged();
                count--;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    // 获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                // 查询选择图片
                Cursor cursor = getContentResolver().query(uri,
                        new String[] { MediaStore.Images.Media.DATA }, null,
                        null, null);
                // 返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                // 光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                list.add(pathImage);
                count++;

            }
        } // end if 打开图片

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.upload_back:
                finish();
                break;
            case R.id.submit_qiuzhu:
                title=Edit_tittle.getText().toString();
                content=Edit_content.getText().toString();
                phone=Edit_phone.getText().toString();
                add=Edit_add.getText().toString();
                final String []data=new String[5+count];
                data[0]=username;
                data[1]=title;
                data[2]=content;
                data[3]=phone;
                data[4]=add;

                if((!data[1].equals(""))&&(!data[2].equals(""))&&(!data[3].equals(""))&&(!data[4].equals(""))) {

                    if (list.size()!= 0) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {


                                path = list.get(0);
                                File file = new File(path);
                                String res = UploadUtil.uploadFile(data, file, "upload");
                                if (res.equals("SUCCUSS")) {
                                    handler.sendEmptyMessage(0);
                                } else {
                                    handler.sendEmptyMessage(1);
                                }

                            }
                        });
                        thread.start();
                    } else {

                        Toast.makeText(this, "请配图加以说明", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "请完善你的信息", Toast.LENGTH_SHORT).show();
                }


                break;



        }
    }
}
