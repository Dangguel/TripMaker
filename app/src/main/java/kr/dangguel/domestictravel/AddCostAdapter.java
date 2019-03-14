package kr.dangguel.domestictravel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class AddCostAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<CostVO> costs;

    int btnImg = R.mipmap.ic_delete;
    String btnText = "비용 삭제";
    int btnColor = R.color.PastelPink;
    BoomMenuButton bmb;

    public AddCostAdapter(LayoutInflater inflater, ArrayList<CostVO> costs) {
        this.inflater = inflater;
        this.costs = costs;
    }

    @Override
    public int getCount() {
        return costs.size();
    }

    @Override
    public Object getItem(int position) {
        return costs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.cost_total_listview2,parent,false);
        TextView tvTime = view.findViewById(R.id.tv_cost_total_time);
        TextView tvPlace = view.findViewById(R.id.tv_cost_total_place_todo);
        TextView tvMemo = view.findViewById(R.id.tv_cost_total_memo);
        TextView tvCost = view.findViewById(R.id.tv_cost_total_cost);
        ImageView ivCostType = view.findViewById(R.id.iv_cost_total_cost_type);

        bmb = view.findViewById(R.id.bmb_add_cost);
        if(bmb!=null) {
            bmb.clearBuilders();
            for (int i = 0; i < 1; i++) {
                TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                        .normalImageRes(btnImg)
                        .normalText(btnText).textSize(16)
                        .normalColorRes(btnColor);
                bmb.addBuilder(builder);
            }
        }

        CostVO costVO = costs.get(position);
        tvTime.setText(costVO.time);
        tvPlace.setText(costVO.placeToDo);
        tvMemo.setText(costVO.detailPlan);
        tvCost.setText(costVO.cost);
        switch (costVO.costType){
            case "식당":
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
        return view;
    }
}
