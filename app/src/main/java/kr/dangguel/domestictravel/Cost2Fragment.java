package kr.dangguel.domestictravel;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Cost2Fragment extends Fragment {


    ExpandableListView listView;
    CostAdapter adapter;
    ArrayList<ScheduleListVO> schduleLists;
    ArrayList<CostVO> costs = new ArrayList<>();
    HashMap<Integer, ArrayList> totalCost = new HashMap<>();
    int allTotalCost;
    int prefIndex;
    String allTotalCostS;
    ImageView chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cost2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NaviActivity navi = (NaviActivity) getActivity();
        schduleLists = navi.schduleLists;
        chart=view.findViewById(R.id.chart);

        listView = view.findViewById(R.id.cost2_listview);
        adapter = new CostAdapter(2, getLayoutInflater(), schduleLists, totalCost);
        listView.setAdapter(adapter);

        Intent intent = getActivity().getIntent();
        prefIndex = intent.getIntExtra("index", -1);

        SharedPreferences pref = getActivity().getSharedPreferences(prefIndex + "costs", Context.MODE_PRIVATE);
        String json = pref.getString(prefIndex + "costs", null);
        if (json != null) {
            try {
                totalCost.clear();
                JSONObject jsonObject = new JSONObject(json);

                for (int i = 0; i < schduleLists.size(); i++) {
                    if (jsonObject.getJSONArray(i + "") == null) {
                        costs = new ArrayList<>();
                        totalCost.put(i, costs);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray(i + "");
                        costs = new ArrayList<>();

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                            String placeTodo = jsonObject1.getString("placeToDo");
                            String time = jsonObject1.getString("time");
                            String cost = jsonObject1.getString("cost");
                            String detailpaln = jsonObject1.getString("detailPlan");
                            String costType = jsonObject1.getString("costType");

                            CostVO costVO = new CostVO(placeTodo, time, detailpaln, cost, costType);
                            costs.add(costVO);
                        }

                        totalCost.put(i, costs);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }

        calMoney();

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (totalCost.get(groupPosition) == null || totalCost.get(groupPosition).size() == 0) {
                    Intent intent = new Intent(navi, AddCost.class);
                    intent.putExtra("position", groupPosition);
                    startActivityForResult(intent, 10);
                }
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(navi, AddCost.class);
                if (totalCost.get(groupPosition) != null) {
                    intent.putExtra("costs", totalCost.get(groupPosition));
                }
                intent.putExtra("position", groupPosition);
                startActivityForResult(intent, 10);
                return false;
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChartActivity.class);
                intent.putExtra("cost",totalCost);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 10:
                    costs = (ArrayList<CostVO>) data.getSerializableExtra("costs");
                    int day = data.getIntExtra("day", -1);

                    totalCost.put(day, costs);

                    SharedPreferences pref = getActivity().getSharedPreferences(prefIndex + "costs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(totalCost);
                    editor.putString(prefIndex + "costs", json);
                    editor.commit();

                    calMoney();

                    NaviActivity naviActivity = (NaviActivity) getActivity();
                    HouseKeepingFragment houseKeepingFragment = naviActivity.housekeeping;
                    houseKeepingFragment.setAllTotalCost(allTotalCostS);

                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    public void calMoney() {
        allTotalCost = 0;
        for (int i = 0; i < totalCost.size(); i++) {
            ArrayList costs = totalCost.get(i);
            if (costs != null) {
                for (int j = 0; j < costs.size(); j++) {
                    CostVO costVO = (CostVO) costs.get(j);
                    if (!costVO.cost.equals("")) {
                        allTotalCost += Integer.parseInt(costVO.cost);
                    }
                }
            }
        }

        DecimalFormat moneyFormatter = new DecimalFormat("###,###");
        String formatMoney = moneyFormatter.format(allTotalCost);
        if (allTotalCost == 0) {
            allTotalCostS = "총 지출 : 0 원";
        } else {
            allTotalCostS = "총 지출 : " + formatMoney + " 원";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chart:
                Intent intent = new Intent(getContext(),ChartActivity.class);
                intent.putExtra("cost",totalCost);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
