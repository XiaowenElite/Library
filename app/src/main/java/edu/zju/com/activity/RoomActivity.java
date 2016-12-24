package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import edu.zju.com.librarycontroller.R;

public class RoomActivity extends Activity {

	private Button btnMen;
	private Button btnDeng;
	private Button btnAir;
	private Button btnCamera;
	private Button btnRoomBsck;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room1);
		btnMen = (Button) this.findViewById(R.id.btn_men);
		btnDeng= (Button) this.findViewById(R.id.btn_deng);
		btnAir= (Button) this.findViewById(R.id.btn_air);
		btnCamera = (Button) this.findViewById(R.id.btn_camera);
		btnRoomBsck = (Button) this.findViewById(R.id.btn_roomBack);
		
		btnMen.setOnClickListener(l);
		btnDeng.setOnClickListener(l);
		btnAir.setOnClickListener(l);
		btnCamera.setOnClickListener(l);
		btnRoomBsck.setOnClickListener(l);
	}
	
	
	private OnClickListener l = new OnClickListener() {
		private Intent intent;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.btn_men:
				intent = new Intent();
				intent.setClass(RoomActivity.this,DoorActivity.class);
				startActivity(intent);				
				break;
			case R.id.btn_deng:
				intent = new Intent();
				intent.setClass(RoomActivity.this,LightActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_air:
				intent = new Intent();
				intent.setClass(RoomActivity.this,AirActivity.class);
				startActivity(intent);
				break;				
			case R.id.btn_camera:
				intent = new Intent();
				intent.setClass(RoomActivity.this,CameraActivity.class);
				startActivity(intent);
				break;
				
			case R.id.btn_roomBack:
				intent = new Intent();
				intent.setClass(RoomActivity.this, LibraryActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};


}
