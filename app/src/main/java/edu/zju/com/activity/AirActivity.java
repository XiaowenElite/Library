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

import edu.zju.com.entity.AirBean;
import edu.zju.com.entity.AirInfo;
import edu.zju.com.entity.DataBean;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

public class AirActivity extends Activity implements OnClickListener{

	private Button btnAirBack;
	private Button btnAirAdd;
	private Button btnMen;
	private Button btnDeng;
	private Button btnAir;
	private Button btnCamera;
	private ListView listView= null;
	private int count;
	private String number;
	private List<AirInfo> airInfoList;

	private  String username;//登录用户名
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air);
		//接收数据
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();

		if (bd != null){
			username = bd.getString("username");
			Log.i("lbk+air",username);
		}
		init();
	}

private void init(){
	btnAirBack = (Button) this.findViewById(R.id.btn_airBack);
	btnAirAdd = (Button) this.findViewById(R.id.btn_airAdd);
	btnMen = (Button) this.findViewById(R.id.btn_men);
	btnDeng= (Button) this.findViewById(R.id.btn_deng);
	btnAir= (Button) this.findViewById(R.id.btn_air);
	btnCamera = (Button) this.findViewById(R.id.btn_camera);

	btnAirBack.setOnClickListener(this);
	btnAirAdd.setOnClickListener(this);
	btnMen.setOnClickListener(this);
	btnDeng.setOnClickListener(this);
	btnAir.setOnClickListener(this);
	btnCamera.setOnClickListener(this);

	testpost();//设备同步获取所有的空调
}

	private void testpost(){
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("username",username);
		params.put("type", "air");

		String  JsonString = JsonUtil.toJson(params);

		OkGo.post(HttpContant.getUnencryptionPath()+"synchroair")//
				.tag(this)//
				.upJson(JsonString)//
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						//上传成功
						Log.i("空调接收测试","result"+s);//查看返回的数据
						//转换成Map
						Map data = JsonUtil.fromJson(s,Map.class);

						//get方法直接获取key对应的value
						//----------解析map--------------------
						number = (String) data.get("count");
						count = Integer.parseInt(number);//转化为整型
						//读取Json数据的数组（设备信息）

						if(count!=0){
								/*
						* 解析json数组
						* */
							AirBean airBean = JsonUtil.fromJson(s,AirBean.class);//拿到Json字符串S,用Gson直接解析成对象
							Log.i("lbk",airBean+"");
							airInfoList = airBean.getData();

//                        for(int i= 0;i<dataInfoList.size();i++){
//                            Log.i("lbk",dataInfoList.get(i).getName()+"");
//                        }

							listView = (ListView) findViewById(R.id.listair);
							//和listview绑定
							List<Map<String, Object>> list= null;
							list = getData();
							listView.setAdapter(new AirMyAdpter(AirActivity.this,list));
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
		for(int i= 0;i<airInfoList.size();i++){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("username",username);
			map.put("name",airInfoList.get(i).getName());
			map.put("phy_addr_did",airInfoList.get(i).getPhy_addr_did());//空调标识和状态
			map.put("name",airInfoList.get(i).getRoute());
			map.put("power",airInfoList.get(i).getPower());//电源开关
			map.put("cmd",airInfoList.get(i).getCmd());//空调开关

			map.put("nameBack",R.drawable.beijing);
			map.put("onOff",R.drawable.ios7_btn);

			list.add(map);
		}
		return list;
	}
	@Override
	public void onClick(View view) {
		Intent intent;
		Bundle bundle;
		switch (view.getId()) {

			case R.id.btn_airBack:
				intent = new Intent();
				intent.setClass(AirActivity.this, LibraryActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_airAdd:
				//传username
				intent = new Intent(AirActivity.this, AddAirActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_men:
				//传username
				intent = new Intent(AirActivity.this, DoorActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_deng:
				//传username
				intent = new Intent(AirActivity.this, LightActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_air:
//				intent = new Intent();
//				intent.setClass(AirActivity.this,AirActivity.class);
//				startActivity(intent);
				break;
			case R.id.btn_camera:
				Toast.makeText(this,"功能敬请期待",Toast.LENGTH_SHORT).show();
//				//传username
//				intent = new Intent(AirActivity.this, CameraActivity.class);
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
