package kr.dangguel.domestictravel;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;

public class HouseKeepingFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager pager;
    CostTabPagerAdapter adapter;
    TextView tvAllTotalCost;
    AdView adView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_keeping, container, false);
        tabLayout = view.findViewById(R.id.layout_cost_tab);
        adView = view.findViewById(R.id.adView);

        pager = view.findViewById(R.id.pager_cost);
        adapter = new CostTabPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);

        tvAllTotalCost = view.findViewById(R.id.tv_total_all_cost);

        final Cost1Fragment cost1Fragment = (Cost1Fragment) adapter.frags[0];
        final Cost2Fragment cost2Fragment = (Cost2Fragment) adapter.frags[1];

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:
                        setAllTotalCost(cost1Fragment.allTotalCostS);
                        break;
                    case 1:
                        setAllTotalCost(cost2Fragment.allTotalCostS);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager.setCurrentItem(0);
    }

    public void setAllTotalCost(String s){
        tvAllTotalCost.setText(s);
    }

}
