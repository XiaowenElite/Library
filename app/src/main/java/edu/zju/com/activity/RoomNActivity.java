package edu.zju.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import edu.zju.com.Fragment.AirFragment;
import edu.zju.com.Fragment.CameraFragment;
import edu.zju.com.Fragment.DoorFragment;
import edu.zju.com.Fragment.LigthFragment;
import edu.zju.com.adapter.LbFragmentAdapter;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.UserUtils;


/**
 * Created by bin on 2016/12/11.
 */

public class RoomNActivity extends FragmentActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private ViewPager myViewPager;

    private Button door;
    private Button air;
    private Button light;
    private Button camera;

    private LinearLayout btnBack;
    private LinearLayout btnAdd;

    private String currentPage;


    LbFragmentAdapter lbFragmentAdapter = null;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_n);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(lbFragmentAdapter!=null){
            lbFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(lbFragmentAdapter!=null){
            lbFragmentAdapter.notifyDataSetChanged();
        }
        currentPage = UserUtils.getCurrentPage();
        switch (Integer.parseInt(currentPage)) {
            case 0:
                myViewPager.setCurrentItem(0);
                index = 0;
                break;
            case 1:
                myViewPager.setCurrentItem(1);
                index = 1;
                break;
            case 2:
                myViewPager.setCurrentItem(2);
                index = 2;
                break;
            case 3:
                myViewPager.setCurrentItem(3);
                index = 3;
                break;
        }
    }

    private void initView() {
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);

        door = (Button) findViewById(R.id.btn_men);
        air = (Button) findViewById(R.id.btn_air);
        light = (Button) findViewById(R.id.btn_deng);
        camera = (Button) findViewById(R.id.btn_camera);

        btnBack = (LinearLayout) this.findViewById(R.id.btn_Back);
        btnAdd = (LinearLayout) this.findViewById(R.id.btn_adddevice);

        DoorFragment doorFragment = new DoorFragment(this);
        LigthFragment ligthFragment = new LigthFragment(this);
        AirFragment airFragment = new AirFragment(this);
        CameraFragment cameraFragment = new CameraFragment();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(doorFragment);
        fragmentList.add(ligthFragment);
        fragmentList.add(airFragment);
        fragmentList.add(cameraFragment);

        lbFragmentAdapter = new LbFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(lbFragmentAdapter);

        //设置初始颜色

        door.setTextColor(getResources().getColor(R.color.white));
        light.setTextColor(getResources().getColor(R.color.gray));
        air.setTextColor(getResources().getColor(R.color.gray));
        camera.setTextColor(getResources().getColor(R.color.gray));

        door.setOnClickListener(this);
        light.setOnClickListener(this);
        air.setOnClickListener(this);
        camera.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        myViewPager.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //当前页面
    }

    @Override
    public void onPageSelected(int position) {
        TextPaint tp;
        switch (position) {
            case 0:
                door.setTextColor(getResources().getColor(R.color.white));
                light.setTextColor(getResources().getColor(R.color.gray));
                air.setTextColor(getResources().getColor(R.color.gray));
                camera.setTextColor(getResources().getColor(R.color.gray));
                tp = door.getPaint();
                tp.setFakeBoldText(true);
                door.setTextSize(18);
                light.setTextSize(16);
                air.setTextSize(16);
                camera.setTextSize(16);
                index = 0;
                break;
            case 1:
                door.setTextColor(getResources().getColor(R.color.gray));
                light.setTextColor(getResources().getColor(R.color.white));
                air.setTextColor(getResources().getColor(R.color.gray));
                camera.setTextColor(getResources().getColor(R.color.gray));
                tp = light.getPaint();
                door.getPaint().setFakeBoldText(false);
                air.getPaint().setFakeBoldText(false);
                camera.getPaint().setFakeBoldText(false);
                tp.setFakeBoldText(true);
                door.setTextSize(16);
                light.setTextSize(18);
                air.setTextSize(16);
                camera.setTextSize(16);
                //LigthFragment.getInstance().getLights();
                index = 1;
                break;
            case 2:
                door.setTextColor(getResources().getColor(R.color.gray));
                light.setTextColor(getResources().getColor(R.color.gray));
                air.setTextColor(getResources().getColor(R.color.white));
                camera.setTextColor(getResources().getColor(R.color.gray));
                tp = air.getPaint();
                tp.setFakeBoldText(true);
                door.setTextSize(16);
                light.setTextSize(16);
                air.setTextSize(18);
                camera.setTextSize(16);
                door.getPaint().setFakeBoldText(false);
                light.getPaint().setFakeBoldText(false);
                camera.getPaint().setFakeBoldText(false);
                index = 2;
                break;
            case 3:
                door.setTextColor(getResources().getColor(R.color.gray));
                light.setTextColor(getResources().getColor(R.color.gray));
                air.setTextColor(getResources().getColor(R.color.gray));
                camera.setTextColor(getResources().getColor(R.color.white));
                tp = camera.getPaint();
                tp.setFakeBoldText(true);
                door.setTextSize(16);
                light.setTextSize(16);
                air.setTextSize(16);
                camera.setTextSize(18);
                index = 3;
                door.getPaint().setFakeBoldText(false);
                air.getPaint().setFakeBoldText(false);
                light.getPaint().setFakeBoldText(false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //0 什么都没做  1 正在滑动 2 滑动完毕
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_men:
                myViewPager.setCurrentItem(0);
                index = 0;
                break;
            case R.id.btn_deng:
                myViewPager.setCurrentItem(1);
                index = 1;
                break;
            case R.id.btn_air:
                myViewPager.setCurrentItem(2);
                index = 2;
                break;
            case R.id.btn_camera:
                myViewPager.setCurrentItem(3);
                index = 3;
                break;
            case R.id.btn_Back:
                finish();
                break;
            case R.id.btn_adddevice:
                //判断当前在那个页面
                switch (index) {
                    case 0:
                        addDoor();
                        break;
                    case 1:
                        addLight();
                        break;
                    case 2:
                        addAir();
                        break;
                    case 3:
                        addCamera();
                        break;
                }
            default:
                break;
        }
    }

    public void addDoor() {
        //传username
        Intent intent = new Intent(RoomNActivity.this, AddDoorActivity.class);
        startActivity(intent);
    }

    public void addLight() {
        Intent intent = new Intent(RoomNActivity.this, AddLightActivity.class);
        startActivity(intent);
    }

    public void addAir() {
        Intent intent = new Intent(RoomNActivity.this, AddAirActivity.class);
        startActivity(intent);
    }

    public void addCamera() {
        Intent intent2 = new Intent();
        intent2.setClass(RoomNActivity.this, AddCameraActivity.class);
        startActivity(intent2);
    }
}
