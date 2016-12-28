package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import edu.zju.com.LibraryApp;
import edu.zju.com.activity.AboutActivity;
import edu.zju.com.activity.SetActivity;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.UserUtils;

public class LibraryActivity extends Activity implements OnClickListener{

	private Button btnRoom;
	private Button btnScene;
	private Button btnSet;
	private Button btnHelp;
	private Button btnTem;
	private Button btnsafe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.library);
		init();
	}
	private void init(){
		btnRoom = (Button) this.findViewById(R.id.btn_room);
		btnScene = (Button) this.findViewById(R.id.btn_scene);
		btnSet = (Button) this.findViewById(R.id.btn_set);
		btnHelp = (Button) this.findViewById(R.id.btn_help);
		btnTem = (Button)findViewById(R.id.btn_tem);

		btnRoom.setOnClickListener(this);
		btnScene.setOnClickListener(this);
		btnSet.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnTem.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		UserUtils.setCurrentPage("0");
		Intent intent;
		switch (v.getId()) {
			case R.id.btn_room:
				//传username
				intent = new Intent(LibraryActivity.this, RoomNActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_scene:
				Toast.makeText(this,"敬请期待更多功能",Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn_set:
				intent = new Intent();
				intent.setClass(edu.zju.com.activity.LibraryActivity.this, SetActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_help:
				intent = new Intent();
				intent.setClass(edu.zju.com.activity.LibraryActivity.this, AboutActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_tem:
				intent = new Intent(LibraryActivity.this,EnviormentActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
