package edu.zju.com.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.com.entity.DataBean;
import edu.zju.com.entity.DataInfo;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;




public class DoorActivity extends Activity implements OnClickListener{


	private Button btnDoorBack;
	private Button btnDoorAdd;
	private Button btnMen;
	private Button btnDeng;
	private Button btnAir;
	private Button btnCamera;

	private ToggleButton mTogBtn;
	private SharedPreferences sharedPreferences;
	private int countx;
	private LinearLayout layout;
	private ListView listView= null;
	private String number;//记录从服务器读取的设备数量,字符串
	private int count;//设备数量
    private List<DataInfo> dataInfoList;//获取JSon数组相关

	private  String username;//登录用户名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.door);
		//接收数据
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();

		if (bd != null){
			username = bd.getString("username");
			Log.i("lbk+men",username);

		}
		init();
//		Intent intentDoor = getIntent();
//		Bundle doorName = intentDoor.getExtras();//接受DoorNameActivity传过来的门和名字
//		if (doorName != null) {
//			String door = doorName.getString("doorname");
//			/*
//			* 逻辑：先读，再保存
//			* 使用SharedPreferences保存数据方法
//			* */
//
//			sharedPreferences = getSharedPreferences(
//						"doorStatus", Activity.MODE_PRIVATE);//			在读取SharedPreference数据前要实例化出一个sharedPreferences对象
//				String btnname = sharedPreferences.getString("btnname", "");
//				String status = sharedPreferences.getString("status", "");
//				countx = sharedPreferences.getInt("countx",0);
//				countx ++;
//
//
//			/*
//			 * 使用SharedPreferences保存数据方法
//			 */
//
//			SharedPreferences mySharedPreferences = getSharedPreferences(
//					"doorStatus", Activity.MODE_PRIVATE);// 实例化SharedPreferences对象（第一步）
//			SharedPreferences.Editor editor = mySharedPreferences.edit();// 实例化SharedPreferences.Edittor对象（第一步）
//			editor.putInt("countx", countx);//记录创建按钮的数量
//			editor.putString("btnname"+Integer.toString(countx), door); // 存储名字
//			editor.putString("status", "true");// 存储按钮的状态
//			editor.commit();	// 提交当前数据
//			Toast.makeText(this, "数据写入成功", Toast.LENGTH_LONG)
//					.show();
//
//		} else {
//
//		}
//		/*
//		 * 使用sharedPreferences读取数据方法如下
//		 */
//		SharedPreferences sharedPreferences = getSharedPreferences(
//				"doorStatus", Activity.MODE_PRIVATE);//	在读取SharedPreference数据前要实例化出一个sharedPreferences对象
//		String status = sharedPreferences.getString("status", "");//getstring方法获得value,注意第二个参数是value的默认值
//		countx = sharedPreferences.getInt("countx",0);//获取按钮的个数
//
//		/*
//		 * 根据sharedPreferences状态判断是否创建按钮
//		 */
//		for (int i = 1; i <= countx; i++) {
//			if (status.equals("true") == true) {
//
//				RelativeLayout rlayout = new RelativeLayout(this);
//				TextView tv = new TextView(this);
//				ToggleButton tgBtn = new ToggleButton(this);
//				layout.addView(tv);
//				layout.addView(tgBtn);
//				tv.setBackgroundResource(R.drawable.men);
//				tgBtn.setBackgroundResource(R.drawable.ios7_btn);
//
//				tv.setText(sharedPreferences.getString("btnname"+Integer.toString(i), ""));
//				tv.setTextSize(15);// 单位是sp
//				tv.setTextColor(getResources().getColor(R.color.gray));
//
//				/* 动态设置边距*/
//				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv
//						.getLayoutParams();// 获取按钮的layoutParams
//				layoutParams.setMargins(0, 60, 0, 0);// 4个参数分别对应上下左右的间距
//				layoutParams.width = DensityUtil.dip2px(getApplicationContext(),
//						320);// 设置控件的宽度
//				layoutParams.height = DensityUtil.dip2px(getApplicationContext(),
//						40);// 设置控件的高度
//				tv.setLayoutParams(layoutParams);
//			}
//		}

	}

private void init(){
	btnDoorBack = (Button) this.findViewById(R.id.btn_doorBack);
	btnDoorAdd = (Button) this.findViewById(R.id.btn_doorAdd);
	btnMen = (Button) this.findViewById(R.id.btn_men);
	btnDeng= (Button) this.findViewById(R.id.btn_deng);
	btnAir= (Button) this.findViewById(R.id.btn_air);
	btnCamera = (Button) this.findViewById(R.id.btn_camera);

	btnDoorBack.setOnClickListener(this);
	btnDoorAdd.setOnClickListener(this);
	btnMen.setOnClickListener(this);
	btnDeng.setOnClickListener(this);
	btnAir.setOnClickListener(this);
	btnCamera.setOnClickListener(this);

	testpost(username);//设备同步获取所有的门

//	mTogBtn = (ToggleButton) findViewById(R.id.mTogBtnDoor1);
//	mTogBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView,
//									 boolean isChecked) {
//			// TODO Auto-generated method stub
//			if(isChecked){
//				//选中
//			}else{
//				//未选中
//			}
//		}
//	});//添加监听事件

}

	private void testpost(String params1){
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("username",params1);
		params.put("type", "door");

		String  JsonString = JsonUtil.toJson(params);

		OkGo.post(HttpContant.getUnencryptionPath()+"synchrodoor")//
				.tag(this)//
				.upJson(JsonString)//
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						//上传成功
						Log.i("门接收测试","result"+s);//查看返回的数据
						//转换成Map
						Map data = JsonUtil.fromJson(s,Map.class);
						//get方法直接获取key对应的value
						//----------解析map--------------------


						number = (String) data.get("count");
						count = Integer.parseInt(number);//转化为整型
						//读取Json数据的数组（设备信息）
						if(count!= 0){
							/*
						* 解析json数组
						* */
							DataBean dataBean = JsonUtil.fromJson(s,DataBean.class);//拿到Json字符串S,用Gson直接解析成对象
							Log.i("lbk",dataBean+"");
							dataInfoList = dataBean.getData();

//                        for(int i= 0;i<dataInfoList.size();i++){
//                            Log.i("lbk",dataInfoList.get(i).getName()+"");
//                        }

							listView = (ListView) findViewById(R.id.list);
							//和listview绑定
							List<Map<String, Object>> list= null;
							try {
								list = getData();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							listView.setAdapter(new MyAdpter(DoorActivity.this,list));
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
	public List<Map<String, Object>> getData() throws JSONException {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

		for(int i= 0;i<dataInfoList.size();i++){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("username",username);
			map.put("name",dataInfoList.get(i).getName());
            map.put("phy_addr_did",dataInfoList.get(i).getPhy_addr_did());
			map.put("route",dataInfoList.get(i).getRoute());
            map.put("cmd",dataInfoList.get(i).getCmd());
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

			case R.id.btn_doorBack:
				intent = new Intent();
				intent.setClass(DoorActivity.this, LibraryActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_doorAdd:
				//传username
				intent = new Intent(DoorActivity.this, AddDoorActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_men:
//				intent = new Intent();
//				intent.setClass(DoorActivity.this,DoorActivity.class);
//				startActivity(intent);
				break;
			case R.id.btn_deng:
				//传username
				intent = new Intent(DoorActivity.this,LightActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_air:
				//传username
				intent = new Intent(DoorActivity.this,AirActivity.class);
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
//				Intent intent = new Intent(DoorActivity.this,CameraActivity.class);
//				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
//				Bundle bundle = new Bundle();
//				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录
//				//此处使用putExtras，接受方就响应的使用getExtras
//				intent.putExtras(bundle);
//				startActivity(intent);
				break;

			default:
				break;
		}
	}

	/* 
	 * px和dp之间的转换
	 *  */
//	public static class DensityUtil {
//
//		/**
//		 * 根据手机的分辨率从dp(像素)的单位转成位px
//		 */
//		public static int dip2px(Context context, float dpValue) {
//			final float scale = context.getResources().getDisplayMetrics().density;
//			return (int) (dpValue * scale + 0.5f);
//		}
//
//		/**
//		 * 根据手机的分辨率从px(像素)的单位转成位dp
//		 */
//		public static int px2dip(Context context, float pxValue) {
//			final float scale = context.getResources().getDisplayMetrics().density;
//			return (int) (pxValue / scale + 0.5f);
//		}
//	}

}
