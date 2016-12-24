package edu.zju.com.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.entity.DataBean;
import edu.zju.com.entity.LightBean;
import edu.zju.com.entity.LightInfo;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

public class LightActivity extends Activity implements OnClickListener {


	private Button btnLBack;
	private Button btnLightAdd;
	private Button btnMen;
	private Button btnDeng;
	private Button btnAir;
	private Button btnCamera;
	private ListView listView= null;
	String number;
	int count;
	List<LightInfo> lightInfoList;
	private  String username;//登录用户名
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light);
		//接收数据
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();

		if (bd != null){
			username = bd.getString("username");
			Log.i("lbk",username);

		}
		init();
	
	}

public void init(){
	btnLBack = (Button) this.findViewById(R.id.btn_lightBack);
	btnLBack.setOnClickListener(this);
	btnLightAdd = (Button) this.findViewById(R.id.btn_lightAdd);
	btnMen = (Button) this.findViewById(R.id.btn_men);
	btnDeng= (Button) this.findViewById(R.id.btn_deng);
	btnAir= (Button) this.findViewById(R.id.btn_air);
	btnCamera = (Button) this.findViewById(R.id.btn_camera);
//	listView = (ListView) findViewById(R.id.listlight);
//	List<Map<String, Object>> list=getData();
//	listView.setAdapter(new LightMyAdpter(this,list));//绑定LightMyAdpter
	btnLightAdd.setOnClickListener(this);
	btnMen.setOnClickListener(this);
	btnDeng.setOnClickListener(this);
	btnAir.setOnClickListener(this);
	btnCamera.setOnClickListener(this);

	testpost();//设备同步获取所有的灯
}
	private void testpost(){
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("username",username);
		params.put("type", "light");

		String  JsonString = JsonUtil.toJson(params);

		OkGo.post(HttpContant.getUnencryptionPath()+"synchrolight")//
				.tag(this)//
				.upJson(JsonString)//
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						//上传成功
						Log.i("灯接收测试","result"+s);//查看返回的数据
						//转换成Map
						Map data = JsonUtil.fromJson(s,Map.class);
						//get方法直接获取key对应的value
						//----------解析map--------------------
						number = (String) data.get("count");
						Log.i("lbk",number+"");
						count = Integer.parseInt(number);//转化为整型
						//读取Json数据的数组（设备信息）

						/*
						* 解析json数组
						* */
						if(count!= 0) {
							LightBean lightBean = JsonUtil.fromJson(s, LightBean.class);//拿到Json字符串S,用Gson直接解析成对象
							Log.i("lbk", lightBean + "");
							lightInfoList = lightBean.getData();
							Log.i("lbk", lightInfoList + "");
							for (int i = 0; i < lightInfoList.size(); i++) {
								Log.i("lbk", lightInfoList.get(i).getName() + "");
							}
							listView = (ListView) findViewById(R.id.listlight);
							//和listview绑定
							List<Map<String, Object>> list = null;
							list = getData();
							listView.setAdapter(new LightMyAdpter(LightActivity.this, list));

						}

						//------------------------------------
//						String result = (String) data.get("success");
//						if("success".equals(result)) {
//
//							Log.i("lbk","result="+data.get("success"));
//						}
//						else{
//							Log.i("lbk","result="+data.get("fail"));
//							Toast.makeText(getApplicationContext(),data.get("fail")+"",Toast.LENGTH_SHORT).show();
//						}

					}
					@Override
					public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
						//这里回调上传进度(该回调在主线程,可以直接更新ui)
						Log.i("test", "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						Log.i("xiaowen","error");
					}
				});
	}
	public List<Map<String, Object>> getData(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i= 0;i<lightInfoList.size();i++){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("username",username);
			map.put("name",lightInfoList.get(i).getName());
			map.put("nameBack",R.drawable.beijing);
			map.put("onOff",R.drawable.ios7_btn);

			map.put("phy_addr_did",lightInfoList.get(i).getPhy_addr_did());
			map.put("route",lightInfoList.get(i).getRoute());
			map.put("cmd",lightInfoList.get(i).getCmd());

			list.add(map);
		}
		return list;
	}
	@Override
	public void onClick(View view) {
		Intent intent;
		Bundle bundle;
		switch (view.getId()) {

			case R.id.btn_lightBack:
				intent = new Intent();
				intent.setClass(LightActivity.this, LibraryActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_lightAdd:

				//传username
				intent = new Intent(LightActivity.this, AddLightActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_men:
				//传username
				intent = new Intent(LightActivity.this,DoorActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_deng:
//				intent = new Intent();
//				intent.setClass(LightActivity.this,LightActivity.class);
//				startActivity(intent);
				break;
			case R.id.btn_air:
				//传username
				intent = new Intent(LightActivity.this,AirActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);

				break;
			case R.id.btn_camera:
				Toast.makeText(this,"功能敬请期待",Toast.LENGTH_SHORT).show();
//				//传username
//				intent = new Intent(LightActivity.this,CameraActivity.class);
//				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
//				bundle = new Bundle();
//				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录
//
//				//此处使用putExtras，接受方就响应的使用getExtras
//				intent.putExtras(bundle);
//				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
