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
import edu.zju.com.Fragment.DoorFragment;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.HttpContant;
import edu.zju.com.utils.JsonUtil;
import edu.zju.com.utils.UserUtils;
import okhttp3.Call;
import okhttp3.Response;

public class AddDoorActivity extends Activity implements OnClickListener{

	private EditText etDoorName;
	private EditText etDoorPhyAddr;
	private EditText etDoorLine;

	private LinearLayout btnDoorBack;
	private Button save;
	private Button unsave;
	private  String username;//登录用户名

	HashMap aa = new HashMap();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adddoor);
		username = UserUtils.getUsername();
		HashMap<String,String> sd= new HashMap<>();
		sd.put("","");

		init();
	}
	private void init(){
		etDoorName = (EditText) this.findViewById(R.id.et_doorName);
		etDoorPhyAddr = (EditText) this.findViewById(R.id.et_doorPhyAddr);
		etDoorLine = (EditText) this.findViewById(R.id.et_doorLine);

		btnDoorBack = (LinearLayout) this.findViewById(R.id.btn_doorBack);
		save = (Button) this.findViewById(R.id.save);
		unsave = (Button) this.findViewById(R.id.unsave);

		btnDoorBack.setOnClickListener(this);
		save.setOnClickListener(this);
		unsave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.save:
				String  type = "door";
				String doorName = etDoorName.getText().toString();
				String doorPhyAddr = etDoorPhyAddr.getText().toString();
				String doorLine = etDoorLine.getText().toString();
				if (!doorName.trim().equals("")&&!doorPhyAddr.trim().equals("")&&!doorLine.trim().equals("")) {
					addDoor(username, type, doorName, doorPhyAddr, doorLine);
				}else {
					Toast.makeText(AddDoorActivity.this,"请填写全部信息",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.unsave:
				finish();
				break;
			case R.id.btn_doorBack:
				UserUtils.setCurrentPage("0");
				finish();
				break;
			default:
				break;
		}
	}
	private void addDoor(String params1,String params2,String params3,String params4,String params5){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("username",params1);
		params.put("type",params2);
		params.put("name", params3);
		params.put("phy_addr_did", params4);
		params.put("route",params5);
		String libid = UserUtils.getLibraryid();
		params.put("library_id",libid);
		String  JsonString = JsonUtil.toJson(params);
		OkGo.post(HttpContant.getUnencryptionPath()+"doorAdd")//
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
							UserUtils.setCurrentPage("0");
							new SweetAlertDialog(AddDoorActivity.this,SweetAlertDialog.SUCCESS_TYPE)
									.setTitleText("Success")
									.setContentText("您已经成功添加门")
									.setConfirmText("确认")
									.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
										@Override
										public void onClick(SweetAlertDialog sweetAlertDialog) {
											UserUtils.setisRefreshDoor("true");
											finish();
										}
									})
									.show();
						}
						else{
							Toast.makeText(AddDoorActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
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
			UserUtils.setCurrentPage("0");
		}
		return super.onKeyDown(keyCode, event);
	}
}
