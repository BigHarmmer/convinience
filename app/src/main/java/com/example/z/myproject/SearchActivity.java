package com.example.z.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.z.util.SearchHistoryCacheUtils;
import com.z.util.SearchResult;
import com.z.util.Voice;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class SearchActivity extends Activity {


	private EditText etSearchContent;

	SearchResult mysearch;
	String url="http://123.206.115.220:8080/bm/user_search?question=";
	String murl;
	String result;
	private ListView mListViewResult;
	private ListView mListViewHistory;
	private SearchAdapter resultAdapter;
	List<String>resultList;
	private HistoryAdapter historyAdapter;
	private LinearLayout llHistory;
	private LinearLayout llResult;
//	private String mUrl = GlobalConstants.GET_SEARCH_RESULT;
//	private ArrayList<SearchResult.ListCourse> searchResultInfo;

	private ArrayList<SearchResult>searchResultArrayList;
	;
	private ViewStub vsNetError;
	private ViewStub vsBlankContent;
	private ImageView ivClear;

	private static final int DO_SEARCH = 1;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			StartSearch();//开始搜索操作  请求网络数据
		}
	};





	private Toast mToast;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		initViews();

	}

	public void initViews() {


		View popupview = LayoutInflater.from(this).inflate(R.layout.popvoice_layout,null);


		ivClear = (ImageView) findViewById(R.id.iv_clear_content);
		etSearchContent = (EditText) findViewById(R.id.et_search_content);

		llHistory = (LinearLayout) findViewById(R.id.ll_search_history);
		llResult = (LinearLayout) findViewById(R.id.ll_search_result);
		mListViewHistory = (ListView) findViewById(R.id.lv_search_history);
		mListViewResult = (ListView) findViewById(R.id.lv_search_result);
		etSearchContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {//搜索listview隐藏 搜索历史显示
					llResult.setVisibility(View.GONE);
					llHistory.setVisibility(View.VISIBLE);
				} else {//搜索listview显示 搜索历史隐藏
					if (llHistory.getVisibility() == View.VISIBLE) {
						llHistory.setVisibility(View.GONE);
					}
					if (ivClear.getVisibility() == View.GONE) {
						ivClear.setVisibility(View.VISIBLE);
					}
					llResult.setVisibility(View.VISIBLE);
				}

				mHandler.sendEmptyMessageDelayed(DO_SEARCH,1000);//延迟搜索，在用户输入的时候就进行搜索，但是考虑到用户流量问题，延迟一秒
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivClear.setVisibility(View.GONE);
				}
			}
		});

		initSearchHistory();
	}

	/**
	 * 初始化搜索历史的记录显示
	 */
	private void initSearchHistory() {

		String cache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
			if (cache != null) {
			List<String> historyRecordList = new ArrayList<>();
			for (String record : cache.split(",")) {
				historyRecordList.add(record);
			}
			historyAdapter = new HistoryAdapter(historyRecordList);
			if (historyRecordList.size() > 0) {
				mListViewHistory.setAdapter(historyAdapter);
				mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						etSearchContent.setText("");
						etSearchContent.setText(historyAdapter.getItem(position).toString());
					}
				});
			}
		} else {
			llHistory.setVisibility(View.GONE);
		}

	}

	private void save(String text) {
		String oldCache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
		StringBuilder builder = new StringBuilder(text);
		if (oldCache == null) {
			SearchHistoryCacheUtils.setCache(builder.toString(), SearchActivity.this);
			updateData();
		} else {
			builder.append("," + oldCache);

			if (!oldCache.contains(text)) {//避免缓存重复的数据
				SearchHistoryCacheUtils.setCache(builder.toString(), SearchActivity.this);
				updateData();
			}
		}
	}

	/**
	 * 更新搜索历史数据显示
	 */
	private void updateData() {
		String cache = SearchHistoryCacheUtils.getCache(SearchActivity.this);
		String[] recordData = new String[]{};
		if (cache != null) {
			recordData = cache.split(",");
		}
		List<String>recordlist=new ArrayList<String>();
		Collections.addAll(recordlist,recordData);
		historyAdapter = new HistoryAdapter(recordlist);
		mListViewHistory.setAdapter(historyAdapter);
		mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				etSearchContent.setText("");
				etSearchContent.setText(historyAdapter.getItem(position).toString());
			}
		});
		historyAdapter.notifyDataSetChanged();
	}

	/**
	 * "搜索" 请求网络数据
	 */
	public void StartSearch() {
		String text = etSearchContent.getText().toString();
		murl=url+text;
		if (TextUtils.isEmpty(text)) {
			return;
		}
			new getJson().execute(text);

	}


	class getJson extends AsyncTask<String,String,String> {

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			resultAdapter = new SearchAdapter(resultList);
			mListViewResult.setAdapter(resultAdapter);
			resultAdapter.notifyDataSetChanged();
			mListViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


					SearchResult sr=searchResultArrayList.get(position);
					//		//缓存搜索历史
					save(sr.getQuestion());
					Intent intent=new Intent();
					intent.setClass(SearchActivity.this,Search_Detail.class);
					intent.putExtra("title",sr.getQuestion());
					intent.putExtra("content",sr.getAnswer());
					intent.putExtra("date",sr.getData());
					startActivity(intent);

				}
			});
		}

		@Override
		protected String doInBackground(String... key) {
			String newText = key[0];
			newText = newText.trim();


				try {
					URL url=new URL(murl);
					HttpURLConnection connection=(HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(3000);
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					int code=connection.getResponseCode();
					Log.e("code", code+"");
					if(code==200){
						result= changeInputString(connection.getInputStream());

						resultList = new ArrayList<String>();

						JSONArray array=new JSONArray(result);



						for(int i=0;i<array.length();i++) {


							JSONObject item=array.getJSONObject(i);
							String  result_question=item.getString("question");
							mysearch=new SearchResult();
							mysearch.setQuestion(result_question);
							mysearch.setId(item.getInt("id"));
							mysearch.setAnswer(item.getString("answer"));
							mysearch.setPid(item.getInt("pid"));
							mysearch.setData(item.getString("date"));



							resultList.add(result_question);
							searchResultArrayList.add(mysearch);

						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}



			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			searchResultArrayList=new ArrayList<SearchResult>();
		}
	}

	private static String changeInputString(InputStream inputStream) {

		String jsonString="";
		ByteArrayOutputStream outPutStream=new ByteArrayOutputStream();
		byte[] data=new byte[1024];
		int len=0;
		try {
			while((len=inputStream.read(data))!=-1){
				outPutStream.write(data, 0, len);
			}
			jsonString=new String(outPutStream.toByteArray());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
	/**
	 * 清空输入框
	 */
	public void ClearContent(View view) {
		etSearchContent.setText("");
		ivClear.setVisibility(View.GONE);
	}

	/**
	 * 清空搜索历史
	 */
	public void ClearSearchHistory(View view) {
		SearchHistoryCacheUtils.ClearCache(SearchActivity.this);
		updateData();
	}
	public void goback(View view)
	{
		finish();
	}

	public void dovoice(View v)
	{
		Voice myvoice=new Voice(SearchActivity.this,etSearchContent,"0");
		myvoice.doVoice();


	}






	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	class HistoryAdapter extends BaseAdapter{

		List<String>mlist=new ArrayList<String>();

		TextView tv1;

		HistoryAdapter(List<String>list)
		{
			mlist=list;
		}
		@Override
		public int getCount() {
			if(mlist.size()<7) {
				return mlist.size();
			}
			else {
				return 6;
			}
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView=LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_history,null);

			tv1= (TextView) convertView.findViewById(R.id.contentTextView);
			tv1.setText(mlist.get(position).toString());
			return convertView;
		}
	}

	class SearchAdapter extends BaseAdapter{

		List<String>mlist=new ArrayList<String>();

		TextView tv1;

		SearchAdapter(List<String>list)
		{
			mlist=list;
		}
		@Override
		public int getCount() {
				return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			convertView=LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_history,null);

			tv1= (TextView) convertView.findViewById(R.id.contentTextView);
			tv1.setText(mlist.get(position).toString());
			return convertView;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();



	}
}
