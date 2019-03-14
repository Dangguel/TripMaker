package kr.dangguel.domestictravel;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.zagum.switchicon.SwitchIconView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.naver.maps.geometry.LatLng;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class AddCost extends AppCompatActivity {

    int day;
    Toolbar toolbar;

    ArrayList<CostVO> costs;
    MyListView listView;
    AddCostAdapter adapter;

    AlertDialog.Builder builder;

    SwitchIconView si1;
    SwitchIconView si2;
    SwitchIconView si3;
    SwitchIconView si4;
    SwitchIconView si5;
    SwitchIconView si6;

    String costType = "식사";

    EditText etPlace;
    EditText etDetail;
    EditText etCost;
    TextView tvTime;

    BoomMenuButton bmb;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);
        adView = findViewById(R.id.adView);

        toolbar = findViewById(R.id.toolbar);

        etPlace = findViewById(R.id.et_place_todo);
        etDetail = findViewById(R.id.et_detail_plan);
        etCost = findViewById(R.id.et_cost);
        tvTime = findViewById(R.id.tv_time);

        si1 = findViewById(R.id.si_1);
        si2 = findViewById(R.id.si_2);
        si3 = findViewById(R.id.si_3);
        si4 = findViewById(R.id.si_4);
        si5 = findViewById(R.id.si_5);
        si6 = findViewById(R.id.si_6);

        si1.setOnClickListener(onClickListener);
        si2.setOnClickListener(onClickListener);
        si3.setOnClickListener(onClickListener);
        si4.setOnClickListener(onClickListener);
        si5.setOnClickListener(onClickListener);
        si6.setOnClickListener(onClickListener);

        Intent intent = getIntent();
        day = intent.getIntExtra("position", -1);
        if (day != -1) {
            toolbar.setTitle("Day " + (day + 1) + " 실 비용");
        }

        costs = (ArrayList<CostVO>) intent.getSerializableExtra("costs");
        if(costs ==null)
            costs = new ArrayList<>();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listview_in_listview);
        adapter = new AddCostAdapter(getLayoutInflater(),costs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);



        builder = new AlertDialog.Builder(this);
        builder.setMessage("작성 하던 내용을 저장하시겠습니까?");
        builder.setNegativeButton("저장안함", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddCost.this, "저장 되었습니다", Toast.LENGTH_SHORT).show();
                Intent resultintent = new Intent();
                resultintent.putExtra("costs", costs);
                resultintent.putExtra("day", day);
                setResult(RESULT_OK, resultintent);
                finish();
            }
        });
        builder.setNeutralButton("취소", null);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    @Override
    public void onBackPressed() {
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                builder.create().show();
                break;
        }
        return true;
    }

    public void setTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String msg = String.format("%d:%02d", hourOfDay, minute);
                tvTime.setText(msg);
                tvTime.setTextColor(Color.RED);
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        dialog.show();
    }

    public void clickAdd(View view) {
        if (etPlace.getText().toString().equals("") || tvTime.getText().toString().equals("시간") || etCost.getText().toString().equals("")){
            Toast.makeText(this, "장소와 시간, 비용은 필수 입력입니다", Toast.LENGTH_LONG).show();
            return;
        }

        String place = etPlace.getText().toString();
        String time = tvTime.getText().toString();
        String detail = etDetail.getText().toString();
        String cost = etCost.getText().toString();

        costs.add(new CostVO(place,time,detail,cost,costType));
        listSort(costs);
        adapter.notifyDataSetChanged();

        etPlace.setText("");
        tvTime.setText("시간");
        tvTime.setTextColor(Color.GRAY);
        etCost.setText("");
        etDetail.setText("");
        costType="식사";
        for(int i=0; i<6; i++){
            SwitchIconView switchIconView = findViewById(R.id.si_1+i);
            switchIconView.setAlpha((float)1);
            switchIconView.setIconEnabled(false,true);
        }
        si1.setIconEnabled(true);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int i = 0; i < 6; i++) {
                SwitchIconView s = findViewById(R.id.si_1 + i);
                s.setAlpha((float) 1);
                s.setIconEnabled(false, true);
            }
            v.setAlpha((float) 0.6);
            ((SwitchIconView) v).switchState(true);

            switch (v.getId()) {
                case R.id.si_1:
                    costType = "식사";
                    break;
                case R.id.si_2:
                    costType = "쇼핑";
                    break;
                case R.id.si_3:
                    costType = "교통";
                    break;
                case R.id.si_4:
                    costType = "관광";
                    break;
                case R.id.si_5:
                    costType = "숙박";
                    break;
                case R.id.si_6:
                    costType = "기타";
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            bmb=view.findViewById(R.id.bmb_add_cost);
            bmb.setOnBoomListener(new OnBoomListenerAdapter(){
                @Override
                public void onClicked(int index, BoomButton boomButton) {
                    super.onClicked(index, boomButton);
                    switch (index){
                        case 0:
                            costs.remove(position);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            });
            bmb.boom();
        }
    };



    public void listSort(ArrayList<CostVO> costs){
        Collections.sort(costs);
    }
}
