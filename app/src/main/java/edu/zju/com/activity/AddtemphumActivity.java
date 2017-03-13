package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

public class AddtemphumActivity extends Activity implements OnClickListener{

	private EditText name;
	private EditText addr;
	private EditText line;
	private LinearLayout back;
	private Button save;
	private Button unsave;

	private  String username;//登录用户名


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtemhum);
		username = UserUtils.getUsername();
		init();
	}
	private void init(){
		name = (EditText) this.findViewById(R.id.et_deviceName);
		addr = (EditText) this.findViewById(R.id.et_devicePhyAddr);
		line = (EditText) this.findViewById(R.id.et_deviceLine);

		back = (LinearLayout) this.findViewById(R.id.btn_deviceBack);
		save = (Button) this.findViewById(R.id.save_device);
		unsave = (Button) this.findViewById(R.id.unsave_device);

		back.setOnClickListener(this);
		save.setOnClickListener(this);
		unsave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.save:
				String  type = "temphum";
				String thName = name.getText().toString();
				String thPhyAddr = addr.getText().toString();
				String thLine = line.getText().toString();
				if (!thName.trim().equals("")&&!thPhyAddr.trim().equals("")&&!thLine.trim().equals("")) {
					addTemphum(username, type, thName, thPhyAddr, thLine);
				}else {
					Toast.makeText(AddtemphumActivity.this,"请填写全部信息",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.unsave:
				finish();
				break;
			case R.id.btn_doorBack:
				UserUtils.setCurrentEmPage("0");
				finish();
				break;
			default:
				break;
		}
	}
	private void addTemphum(String params1,String params2,String params3,String params4,String params5){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username",params1);
		params.put("type",params2);
		params.put("name", params3);
		params.put("phy_addr_did", params4);
		params.put("route",params5);
		String  JsonString = JsonUtil.toJson(params);
		OkGo.post(HttpContant.getUnencryptionPath()+"temphumAdd")//
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
							UserUtils.setCurrentEmPage("0");
							new SweetAlertDialog(AddtemphumActivity.this,SweetAlertDialog.SUCCESS_TYPE)
									.setTitleText("Success")
									.setContentText("您已经成功添加温湿度设备")
									.setConfirmText("确认")
									.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(SweetAlertDialog sweetAlertDialog) {
											finish();
										}
									})
									.show();
						}
						else{
							//Toast.makeText(AddtemphumActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			UserUtils.setCurrentEmPage("0");
		}
		return super.onKeyDown(keyCode, event);
	}
}
