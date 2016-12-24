package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.zju.com.librarycontroller.R;

/**
 * Created by bin on 2016/12/11.
 */

public class SetActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlayout);

        init();
    }

    private void init(){
        TextView tvAddr = (TextView) findViewById(R.id.et_setAddr);
        TextView tvPort = (TextView) findViewById(R.id.et_setPort);
        Button btnSetSave = (Button) findViewById(R.id.btn_setSave);
        Button btnSetBack = (Button) findViewById(R.id.btn_setBack);

        btnSetSave.setOnClickListener(this);
        btnSetBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_setSave:

                break;
            case R.id.btn_setBack:
                intent = new Intent();
                intent.setClass(this, LibraryActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
