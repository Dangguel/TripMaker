package kr.dangguel.domestictravel;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class DiaryAdapter extends BaseExpandableListAdapter {

    LayoutInflater inflater;
    ArrayList<ScheduleListVO> schduleLists;
    HashMap<Integer,ArrayList> totalDiary;

    public DiaryAdapter(LayoutInflater inflater, ArrayList<ScheduleListVO> schduleLists, HashMap<Integer, ArrayList> totalDiary) {
        this.inflater = inflater;
        this.schduleLists = schduleLists;
        this.totalDiary = totalDiary;
    }

    @Override
    public int getGroupCount() {
        return schduleLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (totalDiary.get(groupPosition) == null)
            return 0;
        else
            return totalDiary.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return schduleLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return totalDiary.get(groupPosition).get(childPosition);
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
        if(convertView == null)
            convertView = inflater.inflate(R.layout.diary_listview,parent,false);
        TextView day = convertView.findViewById(R.id.tv_diary_a_day);
        TextView date = convertView.findViewById(R.id.tv_diary_date);

        ScheduleListVO scheduleList = schduleLists.get(groupPosition);
        day.setText(scheduleList.schDay);
        date.setText(scheduleList.schDate);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.diary_total_listview, parent, false);
        DiaryVO diary = (DiaryVO) totalDiary.get(groupPosition).get(childPosition);
        Log.e("sizecheck",totalDiary.get(groupPosition).size()+"");

        ImageView iv = convertView.findViewById(R.id.iv_total_pic);
        TextView tv = convertView.findViewById(R.id.tv_total_diary);
        tv.setVisibility(View.GONE);

        if(diary.memo!=null) {
            tv.setText(diary.memo);
            tv.setVisibility(View.VISIBLE);
        }

        if(diary.picPath!=null) {
            Uri uri = Uri.parse(diary.picPath);
            Log.e("picPath",diary.picPath);
            Picasso.get().load(uri).into(iv);
            iv.setVisibility(View.VISIBLE);
        }
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
