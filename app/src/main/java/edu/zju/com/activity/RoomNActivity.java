package edu.zju.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.zju.com.Fragment.AirFragment;
import edu.zju.com.Fragment.CameraFragment;
import edu.zju.com.Fragment.DoorFragment;
import edu.zju.com.Fragment.LightSenceFragment;
import edu.zju.com.Fragment.LigthFragment;
import edu.zju.com.Fragment.TemHumidityFragment;
import edu.zju.com.adapter.LbFragmentAdapter;
import edu.zju.com.librarycontroller.R;
import edu.zju.com.utils.UserUtils;


/**
 * Created by bin on 2016/12/11.
 */

public class RoomNActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager myViewPager;

    private Button door;
    private Button air;
    private Button light;
    private Button camera;
    private Button btntemp;
    private Button btnlight;
    private TextView tx_roomname;

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
        if (lbFragmentAdapter != null) {
            lbFragmentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lbFragmentAdapter != null) {
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
            case 4:
                myViewPager.setCurrentItem(4);
                index = 4;
                break;
            case 5:
                myViewPager.setCurrentItem(5);
                index = 5;
                break;
        }
    }

    private void initView() {
        myViewPager = (ViewPager) findViewById(R.id.myViewPager);

        door = (Button) findViewById(R.id.btn_men);
        air = (Button) findViewById(R.id.btn_air);
        light = (Button) findViewById(R.id.btn_deng);
        camera = (Button) findViewById(R.id.btn_camera);

        btntemp = (Button) findViewById(R.id.btn_temhumidity);
        btnlight = (Button) findViewById(R.id.btn_lightSence);
        tx_roomname = (TextView)findViewById(R.id.tx_roomname);

        btnBack = (LinearLayout) this.findViewById(R.id.btn_Back);
        btnAdd = (LinearLayout) this.findViewById(R.id.btn_adddevice);

        DoorFragment doorFragment = new DoorFragment(this);
        LigthFragment ligthFragment = new LigthFragment(this);
        AirFragment airFragment = new AirFragment(this);
        CameraFragment cameraFragment = new CameraFragment();
        TemHumidityFragment temHumidityFragment = new TemHumidityFragment(this);
        LightSenceFragment lightSenceFragment = new LightSenceFragment(this);


        tx_roomname.setText(UserUtils.getLibraryname());
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(doorFragment);
        fragmentList.add(ligthFragment);
        fragmentList.add(airFragment);
        fragmentList.add(cameraFragment);
        fragmentList.add(temHumidityFragment);
        fragmentList.add(lightSenceFragment);

        lbFragmentAdapter = new LbFragmentAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(lbFragmentAdapter);

        //设置初始颜色

        door.setTextColor(getResources().getColor(R.color.white));
        light.setTextColor(getResources().getColor(R.color.gray));
        air.setTextColor(getResources().getColor(R.color.gray));
        camera.setTextColor(getResources().getColor(R.color.gray));
        btntemp.setTextColor(getResources().getColor(R.color.gray));
        btnlight.setTextColor(getResources().getColor(R.color.gray));

        door.setOnClickListener(this);
        light.setOnClickListener(this);
        air.setOnClickListener(this);
        camera.setOnClickListener(this);
        btntemp.setOnClickListener(this);
        btnlight.setOnClickListener(this);

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
                setColorAndSize();
                door.setTextColor(getResources().getColor(R.color.white));
                tp = door.getPaint();
                tp.setFakeBoldText(true);
                door.setTextSize(18);
                index = 0;
                break;
            case 1:
                setColorAndSize();
                light.setTextColor(getResources().getColor(R.color.white));
                tp = light.getPaint();
                tp.setFakeBoldText(true);
                light.setTextSize(18);
                //LigthFragment.getInstance().getLights();
                index = 1;
                break;
            case 2:
                setColorAndSize();
                air.setTextColor(getResources().getColor(R.color.white));
                tp = air.getPaint();
                tp.setFakeBoldText(true);
                air.setTextSize(18);
                index = 2;
                break;
            case 3:
                setColorAndSize();
                camera.setTextColor(getResources().getColor(R.color.white));
                tp = camera.getPaint();
                tp.setFakeBoldText(true);
                camera.setTextSize(18);
                index = 3;
                break;
            case 4:
                setColorAndSize();
                tp = btntemp.getPaint();
                tp.setFakeBoldText(true);
                btntemp.setTextSize(18);
                btntemp.setTextColor(getResources().getColor(R.color.white));
                index = 4;
                break;
            case 5:
                setColorAndSize();
                tp = btnlight.getPaint();
                btnlight.setTextSize(18);
                tp.setFakeBoldText(true);
                btnlight.setTextColor(getResources().getColor(R.color.white));
                index = 5;
                break;
        }
    }

    public void setColorAndSize() {
        btntemp.setTextColor(getResources().getColor(R.color.gray));
        btnlight.setTextColor(getResources().getColor(R.color.gray));
        door.setTextColor(getResources().getColor(R.color.gray));
        light.setTextColor(getResources().getColor(R.color.gray));
        air.setTextColor(getResources().getColor(R.color.gray));
        camera.setTextColor(getResources().getColor(R.color.gray));

        door.setTextSize(16);
        light.setTextSize(16);
        air.setTextSize(16);
        btntemp.setTextSize(16);
        btnlight.setTextSize(16);
        camera.setTextSize(16);


        door.getPaint().setFakeBoldText(false);
        light.getPaint().setFakeBoldText(false);
        camera.getPaint().setFakeBoldText(false);
        air.getPaint().setFakeBoldText(false);
        btntemp.getPaint().setFakeBoldText(false);
        btnlight.getPaint().setFakeBoldText(false);

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
            case R.id.btn_temhumidity:
                myViewPager.setCurrentItem(4);
                index = 4;
                break;
            case R.id.btn_lightSence:
                myViewPager.setCurrentItem(5);
                index = 5;
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
                    case 4:
                        addTemphum();
                        break;
                    case 5:
                        addLightsense();
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
    public void addTemphum() {
        //传username
        Intent intent = new Intent(RoomNActivity.this, AddtemphumActivity.class);
        startActivity(intent);
    }

    public void addLightsense() {
        Intent intent = new Intent(RoomNActivity.this, AddLightSenseActivity.class);
        startActivity(intent);
    }
}
