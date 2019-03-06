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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class DiaryFragment extends Fragment {

    ExpandableListView listView;
    DiaryAdapter adapter;
    ArrayList<ScheduleListVO> schduleLists;
    ArrayList<DiaryVO> diarys;
    HashMap<Integer, ArrayList> totalDiary = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        NaviActivity naviActivity = (NaviActivity) getActivity();
        schduleLists = naviActivity.schduleLists;

        listView = view.findViewById(R.id.diary_listview);
        adapter = new DiaryAdapter(getLayoutInflater(), schduleLists, totalDiary);
        listView.setAdapter(adapter);

        SharedPreferences pref = getActivity().getSharedPreferences("diary", Context.MODE_PRIVATE);
        String json = pref.getString("diary", null);
        if (json != null) {
            try {
                totalDiary.clear();
                JSONObject jsonObject = new JSONObject(json);

                for (int i = 0; i < schduleLists.size(); i++) {
                    diarys = new ArrayList<>();
                    if (jsonObject.getJSONArray(i + "") == null) {
                        continue;
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray(i + "");

                        for (int k = 0; k < jsonArray.length(); k++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                            String picPath = jsonObject1.getString("picPath");
                            DiaryVO diaryVO = null;
                            if (jsonObject1.optString("memo") != null) {
                                String memo = jsonObject1.optString("memo");
                                diaryVO = new DiaryVO(picPath, memo);
                            } else {
                                diaryVO = new DiaryVO(picPath);
                            }
                            diarys.add(diaryVO);
                        }
                        totalDiary.put(i, diarys);
                        listView.expandGroup(i, true);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (totalDiary.get(groupPosition) == null) {
                    Intent intent = new Intent(getActivity(), AddDiary.class);
                    if (totalDiary.get(groupPosition) != null) {
                        intent.putExtra("diary", totalDiary.get(groupPosition));
                    }
                    intent.putExtra("position", groupPosition);
                    startActivityForResult(intent, 1);
                } else if (totalDiary.get(groupPosition).size() == 0) {
                    Intent intent = new Intent(getActivity(), AddDiary.class);
                    intent.putExtra("position", groupPosition);
                    startActivityForResult(intent, 1);
                }
                return false;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), AddDiary.class);
                if (totalDiary.get(groupPosition) != null) {
                    intent.putExtra("diary", totalDiary.get(groupPosition));
                }
                intent.putExtra("position", groupPosition);
                startActivityForResult(intent, 1);
                return false;
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    diarys = (ArrayList<DiaryVO>) data.getSerializableExtra("diary");
                    int day = data.getIntExtra("day", -1);

                    totalDiary.put(day, diarys);

                    SharedPreferences pref = getActivity().getSharedPreferences("diary", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(totalDiary);
                    editor.putString("diary", json);
                    editor.commit();

                    listView.expandGroup(day, true);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
