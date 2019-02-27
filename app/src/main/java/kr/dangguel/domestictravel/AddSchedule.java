package kr.dangguel.domestictravel;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class AddSchedule extends AppCompatActivity {

    int day;

    Toolbar toolbar;
    EditText etPlaceTodo;
    TextView tvMap;
    TextView tvTime;
    EditText etCost;
    EditText etDetailPlan;
    TextView tvViewMap;
    MyListView listView;
    LatLng latLng;

    TextView tvDialogTime;
    TextView tvDialogMap;
    EditText etDialogPlaceTodo;
    Spinner DialogSpinner;
    EditText etDialogCost;
    EditText etDialogDetail;
    TimeSchedule dialogTimeSchedule;

    Spinner spinner;
    String spinSelect;

    BoomMenuButton bmb;

    ArrayList<TimeSchedule> timeSchedules;
    AddScheduleAdapter adapter;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        toolbar = findViewById(R.id.add_schedule_toolbar);

        etPlaceTodo = findViewById(R.id.et_place_todo);
        tvMap = findViewById(R.id.tv_map);
        tvTime = findViewById(R.id.tv_time);
        tvTime.setText("시간");
        etCost = findViewById(R.id.et_cost);
        etDetailPlan = findViewById(R.id.et_detail_plan);
        tvViewMap = findViewById(R.id.tv_view_map);

        spinner = findViewById(R.id.spin_cost);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        Intent intent = getIntent();
        day = intent.getIntExtra("position", -1);
        if (day != -1) {
            toolbar.setTitle("Day " + (day + 1) + " 일정");
        }

        timeSchedules = (ArrayList<TimeSchedule>) intent.getSerializableExtra("schedule");
        if(timeSchedules==null)
            timeSchedules = new ArrayList<>();

        listView = findViewById(R.id.schedule_listview_in_listview);
        adapter = new AddScheduleAdapter(getLayoutInflater(),timeSchedules);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(AddSchedule.this, "저장 되었습니다", Toast.LENGTH_SHORT).show();
                Intent resultintent = new Intent();
                resultintent.putExtra("schedule",timeSchedules);
                resultintent.putExtra("day",day);
                setResult(RESULT_OK,resultintent);
                finish();
            }
        });
        builder.setNeutralButton("취소",null);
    }

    @Override
    public void onBackPressed() {
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                builder.create().show();
                break;
        }
        return true;
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    spinSelect = "식사";
                    break;
                case 1:
                    spinSelect = "쇼핑";
                    break;
                case 2:
                    spinSelect = "교통";
                    break;
                case 3:
                    spinSelect = "관광";
                    break;
                case 4:
                    spinSelect = "숙박";
                    break;
                case 5:
                    spinSelect = "기타";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public void clickAdd(View view) {
        if (latLng == null)
            latLng = new LatLng(0, 0);
        if (etPlaceTodo.getText().toString().equals("") || tvTime.getText().toString().equals("시간")){
            Toast.makeText(this, "장소와 시간은 필수 입력 값입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        timeSchedules.add(new TimeSchedule(etPlaceTodo.getText().toString(), latLng.latitude, latLng.longitude, tvTime.getText().toString(), etCost.getText().toString(), etDetailPlan.getText().toString(), spinSelect));
        listSort(timeSchedules);
        adapter.notifyDataSetChanged();

        etPlaceTodo.setText("");
        tvMap.setText("지도");
        tvTime.setText("시간");
        tvTime.setTextColor(Color.GRAY);
        etCost.setText("");
        etDetailPlan.setText("");
        latLng = null;
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            bmb=view.findViewById(R.id.bmb_add_schedule);
            bmb.setOnBoomListener(new OnBoomListenerAdapter(){
                @Override
                public void onClicked(int index, BoomButton boomButton) {
                    super.onClicked(index, boomButton);
                    switch (index){
                        case 0:
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddSchedule.this);
                            builder.setTitle("일정 변경");
                            dialogTimeSchedule = timeSchedules.get(position);

                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.add_schedule_dialog,null);
                            
                            etDialogPlaceTodo = layout.findViewById(R.id.et_dialog_place_todo);
                            tvDialogMap = layout.findViewById(R.id.tv_dialog_map);
                            tvDialogTime = layout.findViewById(R.id.tv_dialog_time);
                            DialogSpinner = layout.findViewById(R.id.spin_dialog_cost);
                            spinSelect=null;
                            DialogSpinner.setOnItemSelectedListener(onItemSelectedListener);
                            etDialogCost = layout.findViewById(R.id.et_dialog_cost);
                            etDialogDetail = layout.findViewById(R.id.et_dialog_detail_plan);

                            builder.setView(layout);
                            
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(etDialogPlaceTodo.getText().toString().equals("") || tvDialogTime.getText().toString().equals("")){
                                        Toast.makeText(AddSchedule.this, "장소와 시간은 필수 입력 값입니다.", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    timeSchedules.remove(position);
                                    if(latLng==null)
                                        latLng = new LatLng(0,0);
                                    timeSchedules.add(position,new TimeSchedule(etDialogPlaceTodo.getText().toString(),latLng.latitude,latLng.longitude,tvDialogTime.getText().toString(),etDialogCost.getText().toString(),etDialogDetail.getText().toString(),spinSelect));
                                    listSort(timeSchedules);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(AddSchedule.this, "일정 변경 완료", Toast.LENGTH_SHORT).show();
                                    tvDialogTime=null;
                                    latLng=null;
                                }
                            });

                            builder.setNegativeButton("취소",null);
                            builder.create().show();
                            break;
                        case 1:
                            timeSchedules.remove(position);
                            adapter.notifyDataSetChanged();
                            break;
                    }
                }
            });
            bmb.boom();
        }
    };


    public void clickMapView(View view) {
        Intent intent = new Intent(this,MapActivity.class);
        int cnt=0;
        for(TimeSchedule t : timeSchedules){
            if(t.mapLng != 0 && t.mapLat != 0){
                cnt++;
            }
        }
        double[] mapLngs,mapLats;
        mapLats = new double[cnt];
        mapLngs = new double[cnt];
        String[] toDos = new String[cnt];
        int longcnt=0;
        for(int i=0; i<timeSchedules.size(); i++){
            TimeSchedule t = timeSchedules.get(i);
            if (t.mapLng != 0 && t.mapLat != 0) {
                mapLats[longcnt] = t.mapLat;
                mapLngs[longcnt] = t.mapLng;
                toDos[longcnt] = t.placeTodo +"\n"+ t.time;
                longcnt++;
            }
        }
        intent.putExtra("mapLats",mapLats);
        intent.putExtra("mapLngs",mapLngs);
        intent.putExtra("toDos",toDos);
        startActivity(intent);
    }

    public void setMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        if(view.getId()==tvMap.getId()) {
            if (latLng == null) {
                startActivityForResult(intent, 20);
            } else {
                intent.putExtra("lat", latLng.latitude);
                intent.putExtra("lng", latLng.longitude);
                startActivityForResult(intent, 20);
            }
        }else{
            if (latLng == null) {
                startActivityForResult(intent, 25);
            } else {
                intent.putExtra("lat", dialogTimeSchedule.mapLat);
                intent.putExtra("lng", dialogTimeSchedule.mapLng);
                startActivityForResult(intent, 25);
            }
        }
    }

    public void setTime(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String msg = String.format("%d:%02d", hourOfDay, minute);

                if(tvDialogTime!=null) {
                    tvDialogTime.setText(msg);
                    tvDialogTime.setTextColor(Color.RED);
                }else{
                    tvTime.setText(msg);
                    tvTime.setTextColor(Color.RED);
                }
            }
        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 20:
                    tvMap.setText("지도 저장됨");
                    if (data.getDoubleExtra("lat", 0) != 0 && data.getDoubleExtra("lng", 0) != 0) {
                        latLng = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0));
                    }
                    break;
                case 25:
                    tvDialogMap.setText("지도 저장됨");
                    if (data.getDoubleExtra("lat", 0) != 0 && data.getDoubleExtra("lng", 0) != 0) {
                        latLng = new LatLng(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lng", 0));
                    }
                    break;

            }
        }
    }

    public void listSort(ArrayList<TimeSchedule> timeSchedules){
        Collections.sort(timeSchedules);
    }

}
