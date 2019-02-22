package kr.dangguel.domestictravel;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class AddScheduleAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<TimeSchedule>timeSchedules;
    int[] btnImg = new int[]{R.mipmap.ic_edit, R.mipmap.ic_delete};
    String[] btnText = new String[]{"일정 변경", "일정 삭제"};
    int[] btnColor = new int[]{R.color.PastelGreen, R.color.PastelPink};
    BoomMenuButton bmb;

    public AddScheduleAdapter(LayoutInflater inflater, ArrayList<TimeSchedule> timeSchedules) {
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
        if(convertView==null) convertView=inflater.inflate(R.layout.add_schedule_listview,parent,false);
        TextView tvPlaceTodo = convertView.findViewById(R.id.tv_add_schedule_place_todo);
        TextView tvTime = convertView.findViewById(R.id.tv_add_schedule_time);
        TextView tvCost = convertView.findViewById(R.id.tv_add_schedule_cost);
        ImageView ivMap = convertView.findViewById(R.id.iv_add_schedule_map);
        ImageView ivCostType = convertView.findViewById(R.id.iv_add_schedule_cost_type);
        bmb = convertView.findViewById(R.id.bmb_add_schedule);
        if(bmb!=null) {
            bmb.clearBuilders();
            for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                        .normalImageRes(btnImg[i])
                        .normalText(btnText[i]).textSize(16)
                        .normalColorRes(btnColor[i]);
                bmb.addBuilder(builder);
            }
        }

        TimeSchedule timeSchedule = timeSchedules.get(position);
        tvPlaceTodo.setText(timeSchedule.placeTodo);
        tvTime.setText(timeSchedule.time);
        if(!timeSchedule.cost.equals("")) {
            tvCost.setVisibility(View.VISIBLE);
            ivCostType.setVisibility(View.VISIBLE);
            tvCost.setText(timeSchedule.cost + " 원");
            switch (timeSchedule.spinselect){
                case "식사":
                    ivCostType.setImageResource(R.mipmap.ic_meal);
                    break;
                case "쇼핑":
                    ivCostType.setImageResource(R.mipmap.ic_shopping);
                    break;
                case "교통":
                    ivCostType.setImageResource(R.mipmap.ic_traffic);
                    break;
                case "관광":
                    ivCostType.setImageResource(R.mipmap.ic_tour);
                    break;
                case "숙박":
                    ivCostType.setImageResource(R.mipmap.ic_home);
                    break;
                case "기타":
                    ivCostType.setImageResource(R.mipmap.ic_more);
                    break;
            }
        }
        else{
            tvCost.setVisibility(View.INVISIBLE);
            ivCostType.setVisibility(View.INVISIBLE);
        }
        if(timeSchedule.mapLat==0 && timeSchedule.mapLng==0)
            ivMap.setVisibility(View.INVISIBLE);
        else
            ivMap.setVisibility(View.VISIBLE);

        return convertView;
    }
}
