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
import android.widget.Toast;

import edu.zju.com.librarycontroller.R;

public class LibraryActivity extends Activity implements OnClickListener{

	private LinearLayout layout;
	private Button btnRoom;
	private Button btnScene;
	private Button btnSet;
	private Button btnHelp;
	private  String username;//登录用户名
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library);
		//接收数据
		Intent intent = this.getIntent();
		Bundle bd = intent.getExtras();

		if (bd != null){
			username = bd.getString("username");
			Log.i("lbk+library",username);

		}
		init();
	}
	private void init(){
		btnRoom = (Button) this.findViewById(R.id.btn_room);
		btnScene = (Button) this.findViewById(R.id.btn_scene);
		btnSet = (Button) this.findViewById(R.id.btn_set);
		btnHelp = (Button) this.findViewById(R.id.btn_help);

		btnRoom.setOnClickListener(this);
		btnScene.setOnClickListener(this);
		btnSet.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		Bundle bundle;
		switch (v.getId()) {
			case R.id.btn_room:

				//传username
				intent = new Intent(LibraryActivity.this, DoorActivity.class);
				//Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
				bundle = new Bundle();
				bundle.putString("username", username);//将空调状态传给控制界面进行状态记录

				//此处使用putExtras，接受方就响应的使用getExtras
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.btn_scene:
//				intent = new Intent();
//				intent.setClass(LibraryActivity.this, RoomActivity.class);
//				startActivity(intent);
				Toast.makeText(this,"敬请期待更多功能",Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_set:
				intent = new Intent();
				intent.setClass(LibraryActivity.this, SetActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_help:
				intent = new Intent();
				intent.setClass(LibraryActivity.this, AboutActivity.class);
				startActivity(intent);

				break;

			default:
				break;
		}
	}
}
