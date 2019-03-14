package kr.dangguel.domestictravel;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ChartActivity extends AppCompatActivity {
    PieChart pieChart;

    int meal;
    int shopping;
    int traffic;
    int tour;
    int home;
    int more;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        adView = findViewById(R.id.adView);
        Intent intent = getIntent();
        HashMap cost = (HashMap) intent.getSerializableExtra("cost");
        HashMap schedule = (HashMap) intent.getSerializableExtra("schedule");

        if (cost != null) {
            for (int i = 0; i < cost.size(); i++) {
                ArrayList<CostVO> costVOS = (ArrayList<CostVO>) cost.get(i);
                for (int j = 0; j < costVOS.size(); j++) {
                    CostVO costVO = costVOS.get(j);
                    switch (costVO.costType) {
                        case "식사":
                            if (!costVO.cost.equals(""))
                                meal += Integer.parseInt(costVO.cost);
                            break;
                        case "쇼핑":
                            if (!costVO.cost.equals(""))
                                shopping += Integer.parseInt(costVO.cost);
                            break;
                        case "교통":
                            if (!costVO.cost.equals(""))
                                traffic += Integer.parseInt(costVO.cost);
                            break;
                        case "관광":
                            if (!costVO.cost.equals(""))
                                tour += Integer.parseInt(costVO.cost);
                            break;
                        case "숙박":
                            if (!costVO.cost.equals(""))
                                home += Integer.parseInt(costVO.cost);
                            break;
                        case "기타":
                            if (!costVO.cost.equals(""))
                                more += Integer.parseInt(costVO.cost);
                            break;
                    }
                }
            }
        }
        if (schedule != null) {
            for (int i = 0; i < schedule.size(); i++) {
                ArrayList<TimeScheduleVO> timeScheduleVOS = (ArrayList<TimeScheduleVO>) schedule.get(i);
                for (int j = 0; j < timeScheduleVOS.size(); j++) {
                    TimeScheduleVO timeScheduleVO = timeScheduleVOS.get(j);
                    switch (timeScheduleVO.spinselect) {
                        case "식사":
                            if (!timeScheduleVO.cost.equals(""))
                                meal += Integer.parseInt(timeScheduleVO.cost);
                            break;
                        case "쇼핑":
                            if (!timeScheduleVO.cost.equals(""))
                                shopping += Integer.parseInt(timeScheduleVO.cost);
                            break;
                        case "교통":
                            if (!timeScheduleVO.cost.equals(""))
                                traffic += Integer.parseInt(timeScheduleVO.cost);
                            break;
                        case "관광":
                            if (!timeScheduleVO.cost.equals(""))
                                tour += Integer.parseInt(timeScheduleVO.cost);
                            break;
                        case "숙박":
                            if (!timeScheduleVO.cost.equals(""))
                                home += Integer.parseInt(timeScheduleVO.cost);
                            break;
                        case "기타":
                            if (!timeScheduleVO.cost.equals(""))
                                more += Integer.parseInt(timeScheduleVO.cost);
                            break;
                    }
                }
            }
        }

        pieChart = findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        DecimalFormat moneyFormatter = new DecimalFormat("###,###");
        if (meal != 0) {
            String formatMoney = moneyFormatter.format(meal);
            yValues.add(new PieEntry(meal, "식사"));
        }
        if (shopping != 0) {
            String formatMoney = moneyFormatter.format(shopping);
            yValues.add(new PieEntry(shopping, "쇼핑"));
        }
        if (traffic != 0) {
            String formatMoney = moneyFormatter.format(traffic);
            yValues.add(new PieEntry(traffic, "교통"));
        }
        if (tour != 0) {
            String formatMoney = moneyFormatter.format(tour);
            yValues.add(new PieEntry(tour, "관광"));
        }
        if (home != 0) {
            String formatMoney = moneyFormatter.format(home);
            yValues.add(new PieEntry(home, "숙박"));
        }
        if (more != 0) {
            String formatMoney = moneyFormatter.format(more);
            yValues.add(new PieEntry(more, "기타"));
        }

        String formatMoney = moneyFormatter.format(meal+shopping+traffic+tour+home+more);
        Description description = new Description();
        description.setText("총 비용 : "+formatMoney+" 원"); //라벨
        description.setTextSize(20);
        pieChart.setDescription(description);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues, "비용 타입들");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(16);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int cost = (int) e.getY();
                DecimalFormat moneyFormatter = new DecimalFormat("###,###");
                String formatMoney = moneyFormatter.format(cost);

                Toast.makeText(ChartActivity.this, "선택한 비용은 " +formatMoney+" 원 입니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart.setCenterTextSize(16);
        pieChart.setEntryLabelTextSize(16);
        pieChart.setEntryLabelColor(Color.BLACK);
        if(description.getText().equals("총 비용 : 0 원")){
            Toast.makeText(this, "가계부에 내용이 없습니다", Toast.LENGTH_SHORT).show();
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
}
