package edu.zju.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.zju.com.librarycontroller.R;


/**
 * Created by bin on 2016/12/11.
 */

public class AboutActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutlayout);
        init();
    }

    private void init(){
        Button btnAboutBack = (Button) findViewById(R.id.btn_aboutBack);
        btnAboutBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_aboutBack:
                intent = new Intent(this,LibraryActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }

    }
}
