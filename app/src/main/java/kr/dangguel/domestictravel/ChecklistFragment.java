package kr.dangguel.domestictravel;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChecklistFragment extends Fragment {

    ImageView addButton;
    MyListView checkList;
    EditText dialogEt;
    AlertDialog.Builder builder;
    ArrayList<CheckVO> checks = new ArrayList<>();
    CheckAdapter adapter;
    BoomMenuButton bmb;
    LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checklist, container, false);
        this.inflater = inflater;
        checkList = view.findViewById(R.id.checklist);
        adapter = new CheckAdapter(inflater, checks);
        checkList.setAdapter(adapter);
        checkList.setOnItemClickListener(onItemClickListener);
        TextView empty = view.findViewById(R.id.tv_empty);
        checkList.setEmptyView(empty);

        SharedPreferences pref = getActivity().getSharedPreferences("checklist", Context.MODE_PRIVATE);
        String json = pref.getString("checklist", null);

        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                checks.clear();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.optJSONObject(i);
                    String item = obj.optString("item");
                    boolean check = obj.optBoolean("check");

                    CheckVO checkVO = new CheckVO(item, check);

                    checks.add(checkVO);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("새 항목 추가");
        builder.setMessage("추가 할 이름을 입력 해주세요");
        View view1 = inflater.inflate(R.layout.checklist_dialog, null);
        builder.setView(view1);
        dialogEt = view1.findViewById(R.id.check_dialog_et);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogEt.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "입력 한 내용이 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                checks.add(new CheckVO(dialogEt.getText().toString()));
                dialogEt.setText("");
                saveData();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogEt.setText("");
            }
        });
        final AlertDialog dialog = builder.create();

        addButton = view.findViewById(R.id.iv_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        return view;
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            bmb = view.findViewById(R.id.bmb_check);
            bmb.setOnBoomListener(new OnBoomListenerAdapter() {
                @Override
                public void onClicked(int index, BoomButton boomButton) {
                    super.onClicked(index, boomButton);
                    switch (index) {
                        case 0:
                            checks.get(position).checkChange();
                            saveData();
                            adapter.notifyDataSetChanged();
                            break;
                        case 1:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View view1 = inflater.inflate(R.layout.checklist_dialog, null);
                            final EditText et = view1.findViewById(R.id.check_dialog_et);
                            builder.setTitle("항목 변경");
                            builder.setMessage("변경 할 이름을 입력 해주세요");
                            builder.setView(view1);
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(et.getText().toString().equals("")){
                                        Toast.makeText(getContext(), "이름이 입력되지 않았습니다", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    checks.remove(position);
                                    checks.add(position, new CheckVO(et.getText().toString()));
                                    saveData();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            builder.setNegativeButton("취소", null);
                            builder.create().show();
                            break;
                        case 2:
                            checks.remove(position);
                            saveData();
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            });
            bmb.boom();
        }
    };

    public void saveData(){
        SharedPreferences pref = getActivity().getSharedPreferences("checklist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jArray = new JSONArray();
        try {
            for (int i = 0; i < checks.size(); i++) {
                JSONObject jObject = new JSONObject();
                jObject.put("item", checks.get(i).item);
                jObject.put("check", checks.get(i).check);
                Log.e("check",checks.get(i).check+"");
                jArray.put(jObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putString("checklist", jArray.toString());
        editor.commit();
    }

}
