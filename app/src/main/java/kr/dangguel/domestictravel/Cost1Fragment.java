package kr.dangguel.domestictravel;


import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Cost1Fragment extends Fragment {

    ExpandableListView listView;
    CostAdapter adapter;
    ArrayList<ScheduleListVO> schduleLists;
    HashMap<Integer, ArrayList> totalSchedule;
    int allTotalCost;
    String allTotalCostS;
    ImageView chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cost1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NaviActivity navi = (NaviActivity) getActivity();
        chart = view.findViewById(R.id.chart);
        schduleLists = navi.schduleLists;
        totalSchedule = navi.schedule.totalSchedule;
        listView = view.findViewById(R.id.cost1_listview);
        adapter = new CostAdapter(1, getLayoutInflater(), schduleLists, totalSchedule);
        listView.setAdapter(adapter);

        for (int i = 0; i < totalSchedule.size(); i++) {
            ArrayList timeSchedules = totalSchedule.get(i);
            for (int j = 0; j < timeSchedules.size(); j++) {
                TimeScheduleVO timeScheduleVO = (TimeScheduleVO) timeSchedules.get(j);
                if (!timeScheduleVO.cost.equals("")) {
                    allTotalCost += Integer.parseInt(timeScheduleVO.cost);
                }
            }
        }
        HouseKeepingFragment houseKeepingFragment = ((NaviActivity) getActivity()).housekeeping;
        DecimalFormat moneyFormatter = new DecimalFormat("###,###");
        String formatMoney = moneyFormatter.format(allTotalCost);
        if (allTotalCost == 0) {
            houseKeepingFragment.setAllTotalCost("총 지출 : 0 원");
            allTotalCostS = "총 지출 : 0 원";
        } else {
            houseKeepingFragment.setAllTotalCost("총 지출 : " + formatMoney + " 원");
            allTotalCostS = "총 지출 : " + formatMoney + " 원";
        }
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                TextView tv = v.findViewById(R.id.tv_schedule_total_cost);
                if(tv.getText().equals("총 비용 : 0 원")){
                    Toast.makeText(navi, "클릭한 날짜의 비용은 0원 입니다", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(navi, "예상 비용의 추가나 변경은 '일정' 에서만 가능합니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChartActivity.class);
                intent.putExtra("schedule",totalSchedule);
                startActivity(intent);
            }
        });

    }

}
