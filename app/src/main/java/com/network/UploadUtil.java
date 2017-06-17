package com.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Book.Book_msg;
import com.bean.NewsBean;
import com.example.z.myproject.Submit_activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by z on 2017/4/28.
 */

public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码
    public static String baseIp="http://123.206.115.220:8080/bm/";




    /**
     * android上传文件到服务器
     * @param file  需要上传的文件
     * @param RequestURL  请求的rul
     * @return  返回响应的内容
     */
    public  static String uploadFile(String[]data, File file, String RequestURL){
        String result = "";
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        String username=data[0];
        String  title=data[1];
        String  content=data[2];
        String  phone=data[3];
        String  address=data[4];
        int type=1;
        String murl=baseIp+RequestURL+"?title="+title+"&username="+username+"&content="+content+"&phone="+phone+"&address="+address+"&type="+type;

        try {

            Log.e("lyd","aaaaaaa");
            URL url = new URL(murl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if(file!=null){

                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                Log.e("lyd","bbbbbb");
                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */



                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1){
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

            }



            /**
             * 获取响应码  200=成功
             * 当响应成功，获取响应的流
             */

            int res = conn.getResponseCode();
            Log.e("lyd",res+"");
            if(res==200){
                InputStream input =  conn.getInputStream();
                StringBuffer sb1= new StringBuffer();
                int ss ;
                while((ss=input.read())!=-1){
                    sb1.append((char)ss);
                }
                result = sb1.toString();
                System.out.println(result);
                return "SUCCUSS";
            }
            else {
                return "FAIL";
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<Book_msg> DecodeJson(String url) {


        List<Book_msg>mList=new ArrayList<Book_msg>();
        Book_msg msg;
        String date="";
        String time="";
        String line="";
        String result="";
        try {

            URL myurl = new URL(url);
            InputStream is = myurl.openStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                result += line;
            }
            Log.e("lyd","reslut"+result);

            JSONArray array=new JSONArray(result);

            for(int i=0;i<array.length();i++) {


                JSONObject item=array.getJSONObject(i);


                date=item.getString("date");

                switch (item.getInt("timeid"))
                {
                    case 1:
                        time=" 9:00-10:00";
                        break;
                    case 2:
                        time=" 10:00-11:00";
                        break;
                    case 3:
                        time=" 14:00-15:00";
                        break;
                    case 4:
                        time=" 15:00-16:00";
                        break;

                }
                msg=new Book_msg();
                msg.setTitle(item.getString("type"));
                msg.setTime(date+time);
                msg.setCode(item.getString("booknum"));
                msg.setPlace(item.getString("bookCenter"));
                msg.setIdCard(item.getString("idCard"));

                mList.add(0,msg);




            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;

    }
}