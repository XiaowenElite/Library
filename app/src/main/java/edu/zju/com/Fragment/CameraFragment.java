package edu.zju.com.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import edu.zju.com.activity.CameraStateActivity;
import edu.zju.com.librarycontroller.R;

/**
 * Created by lixiaowen on 16/12/13.
 */

public class CameraFragment extends Fragment {

    private RelativeLayout cameraSet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera, null);
        cameraSet = (RelativeLayout)view.findViewById(R.id.cameraset);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        cameraSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CameraStateActivity.class);
                startActivity(intent);
            }
        });
    }
}
