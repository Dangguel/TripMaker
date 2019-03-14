package kr.dangguel.domestictravel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CostAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    ArrayList<ScheduleListVO> schduleLists;
    HashMap<Integer, ArrayList> totalCost;
    int index;
    int totalMoney;

    public CostAdapter(int index, LayoutInflater inflater, ArrayList<ScheduleListVO> schduleLists, HashMap<Integer, ArrayList> totalCost) {
        this.index=index;
        this.inflater = inflater;
        this.schduleLists = schduleLists;
        this.totalCost = totalCost;

        if(index==1){
            for(int i=0; i<totalCost.size(); i++){
                ArrayList timeSchedules = totalCost.get(i);
                for(int j=0; j<timeSchedules.size(); j++){
                    TimeScheduleVO timeScheduleVO = (TimeScheduleVO) timeSchedules.get(j);
                    if(timeScheduleVO.cost.equals("")){
                        timeSchedules.remove(j);
                    }
                }
                this.totalCost.put(i,timeSchedules);
            }
        }
    }

    @Override
    public int getGroupCount() {
        return schduleLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (totalCost.get(groupPosition) == null)
            return 0;
        else
            return totalCost.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return totalCost.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return totalCost.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.schedule_listview, parent, false);
        ScheduleListVO schduleList = schduleLists.get(groupPosition);
        TextView tvDay = convertView.findViewById(R.id.tv_schedule_a_day);
        TextView tvDate = convertView.findViewById(R.id.tv_schedule_date);
        tvDay.setText(schduleList.schDay);
        tvDate.setText(schduleList.schDate);

        totalMoney = 0;
        TextView tvCost = convertView.findViewById(R.id.tv_schedule_total_cost);
        tvCost.setVisibility(View.VISIBLE);
        if(index == 1){
            ArrayList<TimeScheduleVO> timeScheduleVOS = totalCost.get(groupPosition);
            if(timeScheduleVOS != null) {
                for (int i = 0; i < timeScheduleVOS.size(); i++) {
                    if (!timeScheduleVOS.get(i).cost.equals(""))
                        totalMoney += Integer.parseInt(timeScheduleVOS.get(i).cost);
                }
            }
        }
        if(index == 2){
            ArrayList<CostVO> costVOS = totalCost.get(groupPosition);
            if(costVOS!=null) {
                for (int i = 0; i < costVOS.size(); i++) {
                    if (!costVOS.get(i).cost.equals(""))
                        totalMoney += Integer.parseInt(costVOS.get(i).cost);
                }
            }
        }

        if (totalMoney > 0) {
            DecimalFormat moneyFormatter = new DecimalFormat("###,###");
            String formatMoney = moneyFormatter.format(totalMoney);
            tvCost.setVisibility(View.VISIBLE);
            tvCost.setText("총 비용 : " + formatMoney + " 원");
        } else {
            tvCost.setVisibility(View.VISIBLE);
            tvCost.setText("총 비용 : 0 원");
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(index==1){
            if (convertView == null)
                convertView = inflater.inflate(R.layout.cost_total_listview1, parent, false);
            TimeScheduleVO timeScheduleVO = (TimeScheduleVO) totalCost.get(groupPosition).get(childPosition);
            TextView placeTodo = convertView.findViewById(R.id.tv_cost_total_place_todo);
            TextView time = convertView.findViewById(R.id.tv_cost_total_time);
            TextView cost = convertView.findViewById(R.id.tv_cost_total_cost);
            TextView memo = convertView.findViewById(R.id.tv_cost_total_memo);
            ImageView costType = convertView.findViewById(R.id.iv_cost_total_cost_type);

            placeTodo.setText(timeScheduleVO.placeTodo);
            time.setText(timeScheduleVO.time);
            memo.setVisibility(View.GONE);
            if (!timeScheduleVO.detailplan.equals("")) {
                memo.setVisibility(View.VISIBLE);
                memo.setText(timeScheduleVO.detailplan);
            }
            if (timeScheduleVO.cost.equals("")) {
                cost.setVisibility(View.INVISIBLE);
                costType.setVisibility(View.INVISIBLE);
            } else {
                cost.setVisibility(View.VISIBLE);
                costType.setVisibility(View.VISIBLE);
                DecimalFormat moneyFormatter = new DecimalFormat("###,###");
                String formatMoney = moneyFormatter.format(Integer.parseInt(timeScheduleVO.cost));
                cost.setText(formatMoney + " 원");


                switch (timeScheduleVO.spinselect) {
                    case "식사":
                        costType.setImageResource(R.mipmap.ic_meal);
                        break;
                    case "쇼핑":
                        costType.setImageResource(R.mipmap.ic_shopping);
                        break;
                    case "교통":
                        costType.setImageResource(R.mipmap.ic_traffic);
                        break;
                    case "관광":
                        costType.setImageResource(R.mipmap.ic_tour);
                        break;
                    case "숙박":
                        costType.setImageResource(R.mipmap.ic_home);
                        break;
                    case "기타":
                        costType.setImageResource(R.mipmap.ic_more);
                        break;
                }
            }

        }
        if(index==2) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.cost_total_listview1, parent, false);
            CostVO costVO = (CostVO) totalCost.get(groupPosition).get(childPosition);
            TextView placeTodo = convertView.findViewById(R.id.tv_cost_total_place_todo);
            TextView time = convertView.findViewById(R.id.tv_cost_total_time);
            TextView cost = convertView.findViewById(R.id.tv_cost_total_cost);
            TextView memo = convertView.findViewById(R.id.tv_cost_total_memo);
            ImageView costType = convertView.findViewById(R.id.iv_cost_total_cost_type);

            placeTodo.setText(costVO.placeToDo);
            time.setText(costVO.time);
            memo.setVisibility(View.GONE);
            if (!costVO.detailPlan.equals("")) {
                memo.setVisibility(View.VISIBLE);
                memo.setText(costVO.detailPlan);
            }
            if (costVO.cost.equals("")) {
                cost.setVisibility(View.INVISIBLE);
                costType.setVisibility(View.INVISIBLE);
            } else {
                cost.setVisibility(View.VISIBLE);
                costType.setVisibility(View.VISIBLE);
                DecimalFormat moneyFormatter = new DecimalFormat("###,###");
                String formatMoney = moneyFormatter.format(Integer.parseInt(costVO.cost));
                cost.setText(formatMoney + " 원");

                switch (costVO.costType) {
                    case "식사":
                        costType.setImageResource(R.mipmap.ic_meal);
                        break;
                    case "쇼핑":
                        costType.setImageResource(R.mipmap.ic_shopping);
                        break;
                    case "교통":
                        costType.setImageResource(R.mipmap.ic_traffic);
                        break;
                    case "관광":
                        costType.setImageResource(R.mipmap.ic_tour);
                        break;
                    case "숙박":
                        costType.setImageResource(R.mipmap.ic_home);
                        break;
                    case "기타":
                        costType.setImageResource(R.mipmap.ic_more);
                        break;
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
