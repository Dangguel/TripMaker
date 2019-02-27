package kr.dangguel.domestictravel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewTotalAdapter extends BaseAdapter{
    LayoutInflater inflater;
    ArrayList<TimeSchedule> timeSchedules;

    public ListViewTotalAdapter(LayoutInflater inflater, ArrayList<TimeSchedule> timeSchedules) {
        this.inflater = inflater;
        this.timeSchedules = timeSchedules;
    }

    @Override
    public int getCount() {
        return timeSchedules.size();
    }

    @Override
    public Object getItem(int position) {
        return timeSchedules.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=inflater.inflate(R.layout.schedule_total_listview,parent,false);

        TimeSchedule timeSchedule = timeSchedules.get(position);
        TextView time = convertView.findViewById(R.id.tv_total_time);
        TextView placeTodo = convertView.findViewById(R.id.tv_total_place_todo);
        TextView cost = convertView.findViewById(R.id.tv_total_cost);
        ImageView costType = convertView.findViewById(R.id.iv_total_cost_type);

        time.setText(timeSchedule.time);
        placeTodo.setText(timeSchedule.placeTodo);
        if(timeSchedule.cost==null) {
            cost.setVisibility(View.INVISIBLE);
            costType.setVisibility(View.INVISIBLE);
        }
        else {
            cost.setVisibility(View.VISIBLE);
            costType.setVisibility(View.VISIBLE);
            cost.setText(timeSchedule.cost);
            switch (timeSchedule.spinselect){
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
}
