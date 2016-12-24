package edu.zju.com.activity;

/*
 * Ӧ��ȫ�ֱ����е�����
 * */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import edu.zju.com.librarycontroller.R;


public class AddRoomActivity extends Activity {

	private Button btnConfirm;
	private Button btnUnConfirm;
	private EditText etName;
	private String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addroom);
		etName = (EditText) this.findViewById(R.id.et_roomName);
		btnConfirm = (Button) this.findViewById(R.id.btn_confirm);
		btnUnConfirm = (Button) this.findViewById(R.id.btn_unconfirm);
		btnConfirm.setOnClickListener(listener);
		btnUnConfirm.setOnClickListener(listener);
		
		
		
	}
  private OnClickListener listener = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			
			name = etName.getText().toString();
		
			
			/*��Ҫ�����ִ���Library*/
			Intent intent = new Intent(AddRoomActivity.this,
					LibraryActivity.class);
			 //Bundle������Я�����ݣ���������Map�����ڴ��key-value��ֵ����ʽ��ֵ 
			Bundle bundle = new Bundle();
			bundle.putString("roomname",name);
			
			//�˴�ʹ��putExtras�����ܷ�����Ӧ��ʹ��getExtras  
			intent.putExtras(bundle);
			startActivity(intent);
			AddRoomActivity.this.finish();
			break;
		case R.id.btn_unconfirm:
			Intent intent2 = new Intent();
			intent2.setClass(AddRoomActivity.this, LibraryActivity.class);
			startActivity(intent2);
			/*ֱ�ӷ���ͼ���*/
			break;

		default:
			break;
		}
	}
};


}
