package edu.zju.com.activity;





import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import okhttp3.Call;
import okhttp3.Response;

public class SignActivity extends Activity implements OnClickListener {


	private EditText etName;
	private EditText etPwd;
	private EditText etEmail;
	private EditText etTel;
	private EditText etGateWay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_main);
		init();

	}

	private void init() {
		Button btn_add = (Button) this.findViewById(R.id.btn_signUp);
		Button btnBack = (Button) this.findViewById(R.id.btn_signBack);
		etName = (EditText) this.findViewById(R.id.sign_et_name);
		etPwd = (EditText) this.findViewById(R.id.sign_et_pwd);
		etEmail = (EditText) this.findViewById(R.id.sign_et_email);
		etTel = (EditText) this.findViewById(R.id.sign_et_tel);
		etGateWay = (EditText) this.findViewById(R.id.sign_et_gateway);

		btn_add.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	private void testpost(final String params1, String params2, String params3, String params4, String params5){
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("username", params1);
		params.put("password", params2);
		params.put("email", params3);
		params.put("cellphone",params4);
		params.put("phy_addr",params5);//会给固定物理地址00-01-6C-06-A6-29
		String  JsonString = JsonUtil.toJson(params);

		OkGo.post(HttpContant.getUnencryptionPath()+"sign")//
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
						if("success".equals(result)) {
							Log.i("lbk","返回成功信息result="+data.get("result"));
							//传username
							Intent intent = new Intent(SignActivity.this, LibraryActivity.class);
							//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
							Bundle bundle = new Bundle();
							bundle.putString("username", params1);//将空调状态传给控制界面进行状态记录

							//此处使用putExtras，接受方就响应的使用getExtras
							intent.putExtras(bundle);
							startActivity(intent);
						}
						else {
							Log.i("lbk","result="+data.get("result"));
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
	@Override
	public void onClick(View view) {
		switch (view.getId()) {

			case R.id.btn_signUp:
				String name =etName.getText().toString();
				String pwd =etPwd.getText().toString();
				String email =etEmail.getText().toString();
				String tel = etTel.getText().toString();
				String gateway = etGateWay.getText().toString();


				if (("".equals(name)&&"".equals(pwd)&&"".equals(email)&&"".equals(tel)&&"".equals(gateway))){
					Toast.makeText(getApplicationContext(),"请输入注册信息",Toast.LENGTH_SHORT).show();
				}else{
					if("".equals(name)){
						Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
					}
					else {
						if("".equals(pwd)) {
							Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
						}
						else{
							if("".equals((email))){
								Toast.makeText(getApplicationContext(),"邮箱不能为空",Toast.LENGTH_SHORT).show();
							}
							else{
								if("".equals((tel))){
									Toast.makeText(getApplicationContext(),"电话不能为空",Toast.LENGTH_SHORT).show();
								}
								else{
									if("".equals((gateway))){
										Toast.makeText(getApplicationContext(),"网关不能为空",Toast.LENGTH_SHORT).show();
									}
									else{
										testpost(name,pwd,email,tel,gateway);
									}
								}

							}

						}
					}

				}


				break;
			case R.id.btn_signBack:
				Intent intent = new Intent();
				intent.setClass(this, LoginActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

}
