package kr.dangguel.domestictravel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainPage1Fragment extends Fragment {

    CalAdapter calAdapter;
    MyListView listView;
    MainActivity mainActivity;
    ArrayList<SaveCalVO> cals;
    View view;
    MainPage2Fragment frag2;
    BoomMenuButton bmb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_page1_fragment, container, false);
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences pref = mainActivity.getSharedPreferences("cals", Context.MODE_PRIVATE);
        String json = pref.getString("cals", null);
        CalendarDay today = CalendarDay.today();

        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                cals=mainActivity.cals;
                cals.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    String range = obj.optString("range");
                    String days = obj.optString("days");
                    String title = obj.optString("title");

                    int makeDayY = obj.optInt("makeDayY");
                    int makeDayM = obj.optInt("makeDayM");
                    int makeDayD = obj.optInt("makeDayD");
                    int day1Y = obj.optInt("day1Y");
                    int day1M = obj.optInt("day1M");
                    int day1D = obj.optInt("day1D");

                    SaveCalVO saveCalVO = new SaveCalVO(range,days,title,makeDayY,makeDayM,makeDayD,day1Y,day1M,day1D,today.getYear(),today.getMonth(),today.getDay());

                    cals.add(saveCalVO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            cals = mainActivity.cals;
        listView = view.findViewById(R.id.listView);
        calAdapter = new CalAdapter(getLayoutInflater(), cals, mainActivity);
        TextView tv = view.findViewById(R.id.tv_main_empty);
        listView.setEmptyView(tv);
        listView.setAdapter(calAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                bmb = view.findViewById(R.id.bmb);
                bmb.setOnBoomListener(new OnBoomListenerAdapter() {
                    @Override
                    public void onClicked(int index, BoomButton boomButton) {
                        super.onClicked(index, boomButton);
                        switch (index) {
                            case 0:
                                Intent intent = new Intent(mainActivity, NaviActivity.class);
                                SaveCalVO saveCalVO = cals.get(position);
                                String title = saveCalVO.title;
                                String range = saveCalVO.range;
                                int untilday = saveCalVO.untilday;
                                String days = saveCalVO.days;

                                intent.putExtra("index",position);
                                intent.putExtra("title", title);
                                intent.putExtra("range", range);
                                intent.putExtra("untilday", untilday);
                                intent.putExtra("days", days);

                                startActivity(intent);
                                break;
                            case 1:
                                mainActivity.frag2.editMode();
                                mainActivity.changeIndex(position);
                                cals.remove(position);

                                getActivity().getSharedPreferences(position+"schedule",Context.MODE_PRIVATE).edit().remove(position+"schedule").commit();
                                getActivity().getSharedPreferences(position+"checklist",Context.MODE_PRIVATE).edit().remove(position+"checklist").commit();
                                getActivity().getSharedPreferences(position+"costs",Context.MODE_PRIVATE).edit().remove(position+"costs").commit();
                                getActivity().getSharedPreferences(position+"diary",Context.MODE_PRIVATE).edit().remove(position+"diary").commit();

                                mainActivity.startFragment2();
                                updateAdapter();
                                Log.e("aa","remove");
                                break;
                            case 2:
                                cals.remove(position);

                                getActivity().getSharedPreferences(position+"schedule",Context.MODE_PRIVATE).edit().remove(position+"schedule").commit();
                                getActivity().getSharedPreferences(position+"checklist",Context.MODE_PRIVATE).edit().remove(position+"checklist").commit();
                                getActivity().getSharedPreferences(position+"costs",Context.MODE_PRIVATE).edit().remove(position+"costs").commit();
                                getActivity().getSharedPreferences(position+"diary",Context.MODE_PRIVATE).edit().remove(position+"diary").commit();

                                updateAdapter();
                                Log.e("bb","remove");
                                Log.e("cc",position+"schedule");
                                break;
                        }
                    }
                });
                bmb.boom();
            }
        });

    }

    void updateAdapter() {
        calAdapter.notifyDataSetInvalidated();
        SharedPreferences pref = mainActivity.getSharedPreferences("cals", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jArray = new JSONArray();
        try {
            for (int i = 0; i < cals.size(); i++) {
                JSONObject jObject = new JSONObject();
                jObject.put("range", cals.get(i).range);
                jObject.put("days", cals.get(i).days);
                jObject.put("title", cals.get(i).title);
                jObject.put("makeDayY", cals.get(i).makeDayY);
                jObject.put("makeDayM", cals.get(i).makeDayY);
                jObject.put("makeDayD", cals.get(i).makeDayY);
                jObject.put("day1Y", cals.get(i).day1Y);
                jObject.put("day1M", cals.get(i).day1M);
                jObject.put("day1D", cals.get(i).day1D);

                jArray.put(jObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("cals", jArray.toString());
        editor.commit();
        Log.e("cc","remove");
    }

}
