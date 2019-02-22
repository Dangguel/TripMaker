package kr.dangguel.domestictravel;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainTabPagerAdapter extends FragmentPagerAdapter {

    Fragment[] frags = new Fragment[2];
    String[] titles = new String[]{"여행 목록","새 여행"};

    public MainTabPagerAdapter(FragmentManager fm) {
        super(fm);

        frags[0] = new MainPage1Fragment();
        frags[1] = new MainPage2Fragment();
    }

        @Override
    public Fragment getItem(int i) {
        return frags[i];
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
