package kr.dangguel.domestictravel;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    ArrayList<ScheduleListVO> groupList;
    HashMap<Integer, ArrayList> childList;

    public ScheduleAdapter(LayoutInflater inflater, ArrayList<ScheduleListVO> groupList, HashMap<Integer, ArrayList> childList) {
        this.inflater = inflater;
        this.groupList = groupList;
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childList.get(groupPosition) == null)
            return 0;
        else
            return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
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
        ScheduleListVO schduleList = groupList.get(groupPosition);
        TextView tvDay = convertView.findViewById(R.id.tv_schedule_a_day);
        TextView tvDate = convertView.findViewById(R.id.tv_schedule_date);
        TextView tvTotalCost = convertView.findViewById(R.id.tv_schedule_total_cost);
        tvDay.setText(schduleList.schDay);
        tvDate.setText(schduleList.schDate);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.schedule_total_listview, parent, false);
        TimeScheduleVO timeScheduleVO = (TimeScheduleVO) childList.get(groupPosition).get(childPosition);
        TextView placeTodo = convertView.findViewById(R.id.tv_total_place_todo);
        TextView time = convertView.findViewById(R.id.tv_total_time);
        TextView cost = convertView.findViewById(R.id.tv_total_cost);
        TextView memo = convertView.findViewById(R.id.tv_total_memo);
        ImageView costType = convertView.findViewById(R.id.iv_total_cost_type);

        placeTodo.setText(timeScheduleVO.placeTodo);
        time.setText(timeScheduleVO.time);
        memo.setVisibility(View.GONE);
        if (!timeScheduleVO.detailplan.equals(""))
            memo.setVisibility(View.VISIBLE);
            memo.setText(timeScheduleVO.detailplan);
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

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
