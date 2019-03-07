package kr.dangguel.domestictravel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ScheduleFragment extends Fragment {
    ArrayList<ScheduleListVO> schduleLists;
    ScheduleAdapter adapter;
    ExpandableListView listView;
    ArrayList<TimeScheduleVO> timeSchedules = new ArrayList<>();
    HashMap<Integer, ArrayList> totalSchedule = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        final NaviActivity naviActivity = (NaviActivity) getActivity();
        this.schduleLists = naviActivity.schduleLists;

        listView = view.findViewById(R.id.schedule_listview);
        adapter = new ScheduleAdapter(naviActivity.getLayoutInflater(), schduleLists, totalSchedule);
        listView.setAdapter(adapter);

        SharedPreferences pref = getActivity().getSharedPreferences("schedule", Context.MODE_PRIVATE);
        String json = pref.getString("schedule", null);
        if (json != null) {
            try {
                totalSchedule.clear();
                JSONObject jsonObject = new JSONObject(json);


                /*JSONArray jsonArray = jsonObject.toJSONArray(jsonObject.names());
                Log.e("aa",jsonArray.length()+"");*/

                for (int i = 0; i < schduleLists.size(); i++) {
                    if (jsonObject.getJSONArray(i + "") == null) {
                        Log.e(i + "", "null");
                        continue;
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray(i + "");
                        Log.e(i+"번째 어레이", jsonArray.toString());
                        timeSchedules = new ArrayList<>();

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                            Log.e(k+"번째 아이템",jsonObject1.toString());
                            String placeTodo = jsonObject1.getString("placeTodo");
                            double mapLat = jsonObject1.getDouble("mapLat");
                            double mapLng = jsonObject1.getDouble("mapLng");
                            String time = jsonObject1.getString("time");
                            String cost = jsonObject1.getString("cost");
                            String detailpaln = jsonObject1.getString("detailplan");
                            String spinselect = jsonObject1.getString("spinselect");

                            TimeScheduleVO timeScheduleVO = new TimeScheduleVO(placeTodo, mapLat, mapLng, time, cost, detailpaln, spinselect);
                            timeSchedules.add(timeScheduleVO);
                        }

                        totalSchedule.put(i, timeSchedules);
                        listView.expandGroup(i,true);
                        Log.e("size",totalSchedule.get(i).size()+"");
                    }
                }
            } catch (JSONException e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()

        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (totalSchedule.get(groupPosition) == null) {
                    Intent intent = new Intent(naviActivity, AddSchedule.class);
                    if (totalSchedule.get(groupPosition) != null) {
                        intent.putExtra("schedule", totalSchedule.get(groupPosition));
                    }
                    intent.putExtra("position", groupPosition);
                    startActivityForResult(intent, 10);
                }
                return false;
            }
        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                Intent intent = new Intent(naviActivity, AddSchedule.class);
                if (totalSchedule != null && totalSchedule.get(groupPosition) != null) {
                    intent.putExtra("schedule", totalSchedule.get(groupPosition));
                }
                intent.putExtra("position", groupPosition);
                startActivityForResult(intent, 10);
                return false;
            }
        });
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()

        {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (totalSchedule.get(groupPosition) != null) {
                    TextView tvTotalCost = listView.findViewById(R.id.tv_schedule_total_cost);
                    tvTotalCost.setVisibility(View.GONE);
                    int totalMoney = 0;
                    for (int i = 0; i < totalSchedule.get(groupPosition).size(); i++) {
                        TimeScheduleVO timeScheduleVO = (TimeScheduleVO) totalSchedule.get(groupPosition).get(i);
                        if (!timeScheduleVO.cost.equals("")) {
                            totalMoney += Integer.parseInt(timeScheduleVO.cost);
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
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener()

        {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if (totalSchedule.get(groupPosition) != null) {
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
                    timeSchedules = (ArrayList<TimeScheduleVO>) data.getSerializableExtra("schedule");
                    int day = data.getIntExtra("day", -1);

                    totalSchedule.put(day, timeSchedules);

                    SharedPreferences pref = getActivity().getSharedPreferences("schedule", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(totalSchedule);
                    Log.e("aa", json);
                    editor.putString("schedule", json);
                    editor.commit();

                    listView.expandGroup(day, true);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}

