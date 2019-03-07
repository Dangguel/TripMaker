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

public class CheckAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<CheckVO> checks;
    BoomMenuButton bmb;
    int[] btnImg = new int[]{R.mipmap.ic_check,R.mipmap.ic_edit, R.mipmap.ic_delete};
    String[] btnText = new String[]{"체크","항목 변경", "항목 삭제"};
    int[] btnColor = new int[]{R.color.PastelBlue,R.color.PastelGreen, R.color.PastelPink};

    public CheckAdapter(LayoutInflater inflater, ArrayList<CheckVO> checks) {
        this.inflater = inflater;
        this.checks = checks;
    }

    @Override
    public int getCount() {
        return checks.size();
    }

    @Override
    public Object getItem(int position) {
        return checks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView=inflater.inflate(R.layout.check_listview,parent,false);
        ImageView iv = convertView.findViewById(R.id.iv_check);
        TextView tv = convertView.findViewById(R.id.tv_item);

        bmb = convertView.findViewById(R.id.bmb_check);
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

        CheckVO check = checks.get(position);
        if(check.check)
            iv.setImageResource(R.mipmap.ic_check_yes);
        else
            iv.setImageResource(R.mipmap.ic_check_no);
        tv.setText(check.item);
        return convertView;
    }
}
