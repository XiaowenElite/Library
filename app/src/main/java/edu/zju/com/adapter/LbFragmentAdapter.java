package edu.zju.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lixiaowen on 16/12/12.
 */

public class LbFragmentAdapter extends FragmentPagerAdapter {

    LbFragmentAdapter adapter = null;

    private List<Fragment> fragmentList;


    public LbFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
