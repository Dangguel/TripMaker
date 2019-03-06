package kr.dangguel.domestictravel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

public class AddDiary extends AppCompatActivity {

    ListView listView;
    AddDiaryAdapter adapter;
    Toolbar toolbar;
    AlertDialog.Builder builder;
    ArrayList<DiaryVO> diarys = new ArrayList<>();
    ImageView iv;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        final Intent intent = getIntent();
        day = intent.getIntExtra("position", -1);
        if(intent.getSerializableExtra("diary")!=null){
            diarys = (ArrayList<DiaryVO>) intent.getSerializableExtra("diary");
            View v = getLayoutInflater().inflate(R.layout.add_diary_listview,null);
        }else{
            diarys.add(new DiaryVO());
        }

        toolbar = findViewById(R.id.add_diary_toolbar);
        if (day != -1) {
            toolbar.setTitle("Day " + (day + 1) + " 일기 쓰기");
        }

        listView = findViewById(R.id.diary_listview_in_listview);
        adapter = new AddDiaryAdapter(this, diarys);
        listView.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder = new AlertDialog.Builder(this);
        builder.setMessage("작성 하던 내용을 저장하시겠습니까?");
        builder.setNegativeButton("저장안함", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddDiary.this, "저장 되었습니다", Toast.LENGTH_SHORT).show();
                Intent resultintent = new Intent();
                DiaryVO diary = diarys.get(diarys.size()-1);
                if(diary.memo==null)
                    diarys.remove(diary);
                if(diarys.size()==1 && diarys.get(0).picPath==null){
                    diarys.clear();
                }
                resultintent.putExtra("diary", diarys);
                resultintent.putExtra("day", day);
                setResult(RESULT_OK, resultintent);
                finish();
            }
        });
        builder.setNeutralButton("취소", null);
    }

    @Override
    public void onBackPressed() {
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                builder.create().show();
                break;
        }
        return true;
    }

    public void getUri(View v) {
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        startActivityForResult(intent1, 2);
        iv = v.findViewById(R.id.iv_add_diary_camera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2:
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        int i = (int) iv.getTag();
                        DiaryVO diary = diarys.get(i);
                        if(diary.picPath == null) {
                            diarys.add(new DiaryVO());
                        }
                        diary.setPicPath(uri.toString());
                        Picasso.get().load(uri).into(iv);
                        adapter.notifyDataSetChanged();

                    }
                    break;
            }
        }
    }



}
