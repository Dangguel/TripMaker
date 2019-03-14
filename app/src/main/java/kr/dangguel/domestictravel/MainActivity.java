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
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager pager;
    MainTabPagerAdapter adapter;

    MainPage1Fragment frag1;
    MainPage2Fragment frag2;

    static int index=0;

    ArrayList<SaveCalVO> cals = new ArrayList<>();

    BackPressCloswHandler backPressCloswHandler;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloswHandler = new BackPressCloswHandler(Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면\n종료 됩니다", Toast.LENGTH_SHORT),this);

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

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                if(i==0){
                    if(frag2.tvRange == null || frag2.tvDays == null){
                        return;
                    }else {
                        frag2.tvRange.setText("여행 기간");
                        frag2.tvDays.setText("여행 일수");
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        tabLayout.setupWithViewPager(pager);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int checkSelfPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(checkSelfPermission==PackageManager.PERMISSION_DENIED){
                String[] permissions = new String [] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,100);

                return;
            }
        }

        adView=findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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
                if(grantResults[0]==PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "외부저장소 사용 거부\n이미지 업로드 불가", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloswHandler.onBackPressed();
    }
}
