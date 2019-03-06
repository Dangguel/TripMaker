package kr.dangguel.domestictravel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager pager;
    MainTabPagerAdapter adapter;

    MainPage1Fragment frag1;
    MainPage2Fragment frag2;

    static int index=0;

    ArrayList<SaveCalVO> cals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*for(int i=0; i<cals.size(); i++){
            cals.get(i).changeToday(); // 앱을 켰을때의 날짜로 바꾸는 메소드
        }*/

        //툴바를 액션바로 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //탭버튼 객체 추가
        tabLayout = findViewById(R.id.layout_tab);

        //뷰페이저와 아답터 연결
        pager = findViewById(R.id.pager);
        adapter = new MainTabPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        frag1 = (MainPage1Fragment) adapter.frags[0];
        frag2 = (MainPage2Fragment) adapter.frags[1];

        tabLayout.setupWithViewPager(pager);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkSelfPermission==PackageManager.PERMISSION_DENIED){
                String[] permissions = new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,100);

                return;
            }
        }

    }

    void startFragment1() {
        pager.setCurrentItem(0);
    }
    void startFragment2() { pager.setCurrentItem(1); }

    void changeIndex(int index){
        this.index=index;
    }
    int getIndex(){
        return index;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100 :
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "사진 저장 가능", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "사진 저장 기능 제한", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
