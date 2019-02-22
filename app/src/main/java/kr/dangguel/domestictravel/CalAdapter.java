package kr.dangguel.domestictravel;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;

import java.util.ArrayList;

public class CalAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<SaveCal> cals;
    int[] btnImg = new int[]{R.mipmap.ic_check,R.mipmap.ic_edit, R.mipmap.ic_delete};
    String[] btnText = new String[]{"일정 선택","일정 변경", "일정 삭제"};
    int[] btnColor = new int[]{R.color.PastelBlue,R.color.PastelGreen, R.color.PastelPink};
    MainActivity mainActivity;
    BoomMenuButton bmb;

    public CalAdapter(LayoutInflater inflater, ArrayList<SaveCal> cals, MainActivity mainActivity) {
        this.inflater = inflater;
        this.cals = cals;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return cals.size();
    }

    @Override
    public Object getItem(int position) {
        return cals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.cal_listview, parent, false);

        TextView tvTitle = convertView.findViewById(R.id.listView_tv_title);
        TextView tvRange = convertView.findViewById(R.id.listView_tv_range);
        TextView tvDays = convertView.findViewById(R.id.listView_tv_days);
        TextView tvUntilday = convertView.findViewById(R.id.listView_tv_untilday);
        ImageView iv = convertView.findViewById(R.id.listView_iv);
        bmb = convertView.findViewById(R.id.bmb);

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

        SaveCal saveCal = cals.get(position);
        tvTitle.setText(saveCal.title);
        tvRange.setText(saveCal.range);
        tvDays.setText(saveCal.days);
        tvUntilday.setTextColor(Color.RED);
        if (saveCal.untilday < 0) {
            tvUntilday.setTextColor(Color.GRAY);
            tvUntilday.setText("완료된 여행입니다");
            iv.setVisibility(View.GONE);
        } else tvUntilday.setText(saveCal.untilday + " 일 남음");

        return convertView;

    }

}