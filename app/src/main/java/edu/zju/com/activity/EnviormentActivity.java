package edu.zju.com.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.zju.com.Fragment.LightSenceFragment;
import edu.zju.com.Fragment.TemHumidityFragment;
import edu.zju.com.adapter.LbFragmentAdapter;
import edu.zju.com.librarycontroller.R;

public class EnviormentActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{


    private ViewPager myViewPager;
    private Button btntemp;
    private Button btnlight;
    private Button back;

    TextPaint tp;


    LbFragmentAdapter lbFragmentAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviorment);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(lbFragmentAdapter!=null){
            lbFragmentAdapter.notifyDataSetChanged();
        }
    }



    private void init(){

        myViewPager = (ViewPager) findViewById(R.id.en_myViewPager);

        btntemp = (Button)findViewById(R.id.btn_temhumidity);
        btnlight = (Button)findViewById(R.id.btn_lightSence);
        back = (Button)findViewById(R.id.mf_doorBack);

        btntemp.setOnClickListener(this);
        btnlight.setOnClickListener(this);
        back.setOnClickListener(this);
        TemHumidityFragment temHumidityFragment = new TemHumidityFragment(this);
        LightSenceFragment lightSenceFragment = new LightSenceFragment(this);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(temHumidityFragment);
        fragmentList.add(lightSenceFragment);


        lbFragmentAdapter = new LbFragmentAdapter(getSupportFragmentManager(),fragmentList);
        myViewPager.setAdapter(lbFragmentAdapter);

        myViewPager.setOnPageChangeListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_temhumidity:
                myViewPager.setCurrentItem(0);
                break;
            case R.id.btn_lightSence:
                myViewPager.setCurrentItem(1);
                break;
            case R.id.btn_Back:
                finish();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                tp = btntemp.getPaint();
                tp.setFakeBoldText(true);
                btnlight.getPaint().setFakeBoldText(false);
                btntemp.setTextSize(18);
                btnlight.setTextSize(16);
                btntemp.setTextColor(getResources().getColor(R.color.white));
                btnlight.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 1:
                tp = btnlight.getPaint();
                btnlight.setTextSize(18);
                btntemp.setTextSize(16);
                tp.setFakeBoldText(true);
                btntemp.getPaint().setFakeBoldText(false);
                btnlight.setTextColor(getResources().getColor(R.color.white));
                btntemp.setTextColor(getResources().getColor(R.color.gray));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
