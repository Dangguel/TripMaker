package kr.dangguel.domestictravel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment {
    ArrayList<SchduleList> schduleLists;
    ScheduleAdapter adapter;
    ExpandableListView listView;
    ArrayList<TimeSchedule> timeSchedules = new ArrayList<>();
    HashMap<Integer,ArrayList> totalSchedule = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        final NaviActivity naviActivity = (NaviActivity) getActivity();
        this.schduleLists = naviActivity.schduleLists;

        listView = view.findViewById(R.id.schedule_listview);
        adapter = new ScheduleAdapter(naviActivity.getLayoutInflater(),schduleLists,totalSchedule);
        listView.setAdapter(adapter);

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(totalSchedule.get(groupPosition)==null){
                    Intent intent = new Intent(naviActivity, AddSchedule.class);
                    if (totalSchedule != null && totalSchedule.get(groupPosition) != null) {
                        intent.putExtra("schedule", totalSchedule.get(groupPosition));
                    }
                    intent.putExtra("position", groupPosition);
                    startActivityForResult(intent, 10);
                }
                return false;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(naviActivity, AddSchedule.class);
                if (totalSchedule != null && totalSchedule.get(groupPosition) != null) {
                    intent.putExtra("schedule", totalSchedule.get(groupPosition));
                }
                intent.putExtra("position", groupPosition);
                startActivityForResult(intent, 10);
                return false;
            }
        });
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (totalSchedule.get(groupPosition) != null) {
                    TextView tvTotalCost = listView.findViewById(R.id.tv_schedule_total_cost);
                    tvTotalCost.setVisibility(View.GONE);
                    int totalMoney = 0;
                    for (int i = 0; i < totalSchedule.get(groupPosition).size(); i++) {
                        TimeSchedule timeSchedule = (TimeSchedule) totalSchedule.get(groupPosition).get(i);
                        if (!timeSchedule.cost.equals("")) {
                            totalMoney += Integer.parseInt(timeSchedule.cost);
                        }
                    }

                    if (totalMoney > 0) {
                        DecimalFormat moneyFormatter = new DecimalFormat("###,###");
                        String formatMoney = moneyFormatter.format(totalMoney);
                        tvTotalCost.setVisibility(View.VISIBLE);
                        tvTotalCost.setText("총 비용 : " + formatMoney + " 원");
                    }
                }
            }
        });
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(totalSchedule.get(groupPosition) != null){
                    TextView tvTotalCost = listView.findViewById(R.id.tv_schedule_total_cost);
                    tvTotalCost.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    timeSchedules = (ArrayList<TimeSchedule>) data.getSerializableExtra("schedule");
                    int day = data.getIntExtra("day", -1);

                    totalSchedule.put(day,timeSchedules);
                    listView.expandGroup(day,true);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
