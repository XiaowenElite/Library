package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import edu.zju.com.librarycontroller.R;

public class CameraActivity extends Activity {

	private Button btnAdd;
	private Button btnCameraBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		btnCameraBack = (Button) this.findViewById(R.id.btn_cameraBack);

		btnCameraBack.setOnClickListener(listener);

	}
private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.btn_cameraBack:
				Intent intent2 = new Intent();
				intent2.setClass(CameraActivity.this, RoomActivity.class);
				startActivity(intent2);
				break;
				
//			case R.id.btn_camera1:
//				/*����*/
//				break;

			default:
				break;
			}
		}
	};

}
