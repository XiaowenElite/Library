package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

public class AddLightActivity extends Activity implements OnClickListener{

	private EditText etLightName;
	private EditText etLightPhyAddr;
	private EditText etLightLine;

	private Button btnLightBack;
	private Button btnConfirmD;
	private Button btnUnConfirmD;
	private  String username;//登录用户名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addlight);
		//接收数据
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();

		if (bd != null){
			username = bd.getString("username");
			Log.i("lbk",username);

		}
		init();

	}
	private void init(){
		etLightName = (EditText) this.findViewById(R.id.et_lightName);
		etLightPhyAddr = (EditText) this.findViewById(R.id.et_lightPhyAddr);
		etLightLine = (EditText) this.findViewById(R.id.et_lightline);

		btnLightBack = (Button) this.findViewById(R.id.btn_lightBack);
		btnConfirmD = (Button) this.findViewById(R.id.btn_confirmD);
		btnUnConfirmD = (Button) this.findViewById(R.id.btn_unconfirmD);
		btnLightBack.setOnClickListener(this);
		btnConfirmD.setOnClickListener(this);
		btnUnConfirmD.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
			case R.id.btn_confirmD:
				String type = "light";
				String lightName = etLightName.getText().toString();
				String lightPhyAddr = etLightPhyAddr.getText().toString();
				String lightLine = etLightLine.getText().toString();
				testpost(username,type,lightName,lightPhyAddr,lightLine);
				break;
			case R.id.btn_unconfirmD:

				//传username
				intent = new Intent(AddLightActivity.this, LightActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);

				break;
			case R.id.btn_lightBack:
				//传username
				intent = new Intent(this,LightActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			default:
				break;
		}
	}
	private void testpost(String params1,String params2,String params3,String params4,String params5){
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("username",params1);
		params.put("type", params2);
		params.put("name", params3);
		params.put("phy_addr_did", params4);
		params.put("route", params5);
		String  JsonString = JsonUtil.toJson(params);
//okgo每次使用注意在全局文件中初始化
		OkGo.post(HttpContant.getUnencryptionPath()+"lightAdd")//
				.tag(this)//
				.upJson(JsonString)//
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						//上传成功
						Log.i("xiaowen","result"+s);
						//转换成Map
						Map data = JsonUtil.fromJson(s,Map.class);
						//get方法直接获取key对应的value
						String result = (String) data.get("result");
						Log.i("lbk","result="+data.get("result"));
						if("success".equals(result)) {
							Toast.makeText(getApplicationContext(),"灯添加成功",Toast.LENGTH_SHORT).show();

							Intent intent;
							Bundle bundle;
							//传username
							intent = new Intent(AddLightActivity.this, LightActivity.class);
							//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
							bundle = new Bundle();
							bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

							//此处使用putExtras，接受方就响应的使用getExtras
							intent.putExtras(bundle);
							startActivity(intent);
						}
						else{
							Toast.makeText(getApplicationContext(),data.get("result")+"",Toast.LENGTH_SHORT).show();
						}

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
}