package kr.dangguel.domestictravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddDiaryAdapter extends BaseAdapter {

    Context context;
    ArrayList<DiaryVO> diarys;

    public AddDiaryAdapter(Context context, ArrayList<DiaryVO> diarys) {
        this.context = context;
        this.diarys = diarys;
    }

    @Override
    public int getCount() {
        return diarys.size();
    }

    @Override
    public Object getItem(int position) {
        return diarys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((AddDiary) context).getLayoutInflater().inflate(R.layout.add_diary_listview, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.tv_add_diary_title);
        final ImageView iv = convertView.findViewById(R.id.iv_add_diary_camera);
        final EditText et = convertView.findViewById(R.id.et_add_diary_memo);

        final ImageView btn = convertView.findViewById(R.id.iv_add_diary_cancel);
        iv.setTag(position);
        btn.setTag(position);
        et.setTag(position);
        btn.setVisibility(View.INVISIBLE);

        final DiaryVO diary = diarys.get(position);
        if (diary.picPath != null) {
            Uri uri = Uri.parse(diary.picPath);
            Picasso.get().load(uri).into(iv);
            btn.setVisibility(View.VISIBLE);
        } else {
            iv.setImageResource(R.mipmap.ic_add_photo);
            btn.setVisibility(View.INVISIBLE);
        }
        if (diary.memo != null) {
            et.setText(diary.memo);
        }else{
            et.setText("");
        }

        String num = String.format("%02d", position + 1);
        tv.setText("일기#" + num);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddDiary) context).getUri(v);
            }
        });

        if (diary.picPath != null)
            btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diarys.remove((int) v.getTag());
                Log.e("size",diarys.size()+"");
                btn.setVisibility(View.INVISIBLE);
                notifyDataSetChanged();
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DiaryVO diary = diarys.get(position);
                if (!et.getText().toString().equals(""))
                    diary.setMemo(et.getText().toString());
                else
                    diary.setMemo("");
            }
        });

        return convertView;
    }
}
