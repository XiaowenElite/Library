package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import edu.zju.com.librarycontroller.R;

public class AddCameraActivity extends Activity implements OnClickListener {

    private LinearLayout btnDoorBack;
    private Button btnSave;
    private Button btnUnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcamera);
        btnDoorBack = (LinearLayout) findViewById(R.id.btn_Back);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnUnSave = (Button) findViewById(R.id.btn_unsave);

        btnDoorBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnUnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Back:
                finish();
                break;
            case R.id.btn_save:
                finish();
                break;
            case R.id.btn_unsave:
                finish();
                break;
        }
    }
}
