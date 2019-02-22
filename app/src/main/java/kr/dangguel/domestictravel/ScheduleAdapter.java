package kr.dangguel.domestictravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {

    ArrayList<SchduleList> schduleLists;
    LayoutInflater inflater;

    public ScheduleAdapter(ArrayList<SchduleList> schduleList, LayoutInflater inflater) {
        this.schduleLists = schduleList;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return schduleLists.size();
    }

    @Override
    public Object getItem(int position) {
        return schduleLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        convertView = inflater.inflate(R.layout.schedule_listview,parent,false);
        TextView aDay = convertView.findViewById(R.id.tv_schedule_a_day);
        TextView date = convertView.findViewById(R.id.tv_schedule_date);

        SchduleList schdule = schduleLists.get(position);
        aDay.setText(schdule.schDay);
        date.setText(schdule.schDate);

        return convertView;
    }
}
