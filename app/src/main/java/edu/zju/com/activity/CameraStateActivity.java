package edu.zju.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import edu.zju.com.librarycontroller.R;

public class CameraStateActivity extends Activity implements View.OnClickListener{

    private LinearLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameracontrol);
        back =(LinearLayout) findViewById(R.id.btn_cameraBack);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cameraBack:
                finish();
                break;
        }
    }
}
