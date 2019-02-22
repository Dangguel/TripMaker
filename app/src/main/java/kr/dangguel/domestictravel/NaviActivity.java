package kr.dangguel.domestictravel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NaviActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView title;
    TextView range;
    TextView days;
    TextView untilday;

    NavigationView navi;
    DrawerLayout drawer;
    ActionBarDrawerToggle drawerToggle;

    RelativeLayout foucsFrag;
    ScheduleFragment schedule;
    HouseKeepingFragment housekeeping;
    ChecklistFragment checklist;
    DiaryFragment diary;
    PostFragment post;

    FragmentManager manager;
    FragmentTransaction tran;

    ArrayList<SchduleList> schduleLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        Intent intent = getIntent();


        foucsFrag = findViewById(R.id.focus_frag);

        navi = findViewById(R.id.navi);
        navi.setItemIconTintList(null);
        drawer = findViewById(R.id.layout_drawer);
        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_schedule:
                        toolbar.setTitle("일정");
                        Menu menu = toolbar.getMenu();
                        menu.findItem(R.id.menu_chart).setVisible(false);
                        tran = manager.beginTransaction();
                        tran.replace(R.id.focus_frag,schedule);
                        tran.commit();
                        break;
                    case R.id.menu_homekeeping:
                        toolbar.setTitle("가계부");
                        tran = manager.beginTransaction();
                        tran.replace(R.id.focus_frag,housekeeping);
                        tran.commit();
                        break;
                    case R.id.menu_checklist:
                        toolbar.setTitle("체크리스트");
                        tran = manager.beginTransaction();
                        tran.replace(R.id.focus_frag,checklist);
                        tran.commit();
                        break;
                    case R.id.menu_diary:
                        toolbar.setTitle("여행 일기");
                        tran = manager.beginTransaction();
                        tran.replace(R.id.focus_frag,diary);
                        tran.commit();
                        break;
                    case R.id.menu_setting:
                        toolbar.setTitle("설정 & 문의");
                        tran = manager.beginTransaction();
                        tran.replace(R.id.focus_frag,post);
                        tran.commit();
                        break;
                }
                drawer.closeDrawer(navi);
                return false;
            }
        });

        View header = navi.getHeaderView(0);

        title = header.findViewById(R.id.tv_header_title);
        range = header.findViewById(R.id.tv_header_range);
        days = header.findViewById(R.id.tv_header_days);
        untilday = header.findViewById(R.id.tv_header_untilday);

        title.setText(intent.getStringExtra("title"));
        range.setText(intent.getStringExtra("range"));
        days.setText(intent.getStringExtra("days"));
        untilday.setText(intent.getIntExtra("untilday",0) + "일 남음");

        //String[] strDays = intent.getStringExtra("days").split(" ");
        String[] strDays = "10 일간 여행".split(" ");
        int daysNum = Integer.parseInt(strDays[0]);

        //String[] strRange = intent.getStringExtra("range").split("~");
        String[] strRange = "2019-2-19(목) ~ 2019-2-25(화)".split("~");
        strRange[0]=strRange[0].trim();
        strRange[1]=strRange[1].trim();
        strRange=strRange[0].split("-");
        strRange[2]=strRange[2].split("\\(")[0];
        int year = Integer.parseInt(strRange[0]);
        int month = Integer.parseInt(strRange[1]);
        int day = Integer.parseInt(strRange[2]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,day);
        String [] schDate = new String[daysNum];
        for(int i=0; i<daysNum; i++){
            if(i==0) cal.add(Calendar.DATE,0);
            else cal.add(Calendar.DATE,1);
            String dayOfWeek = CalDayOfWeek(cal);
            Date date = cal.getTime();
            schDate[i] = dateFormat.format(date);
            schDate[i] = schDate[i]+" ("+dayOfWeek+")";

            String schDay = "D"+(i+1);

            schduleLists.add(new SchduleList(schDay,schDate[i]));
        }

        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.app_name,R.string.app_name);
        toolbar = findViewById(R.id.navitoolbar);
        toolbar.setTitle("일정");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerToggle.syncState();
        drawer.addDrawerListener(drawerToggle);

        schedule = new ScheduleFragment();
        housekeeping = new HouseKeepingFragment();
        checklist = new ChecklistFragment();
        diary = new DiaryFragment();
        post = new PostFragment();

        manager = getSupportFragmentManager();
        tran = manager.beginTransaction();
        tran.add(R.id.focus_frag,schedule);
        tran.addToBackStack(null);
        tran.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerToggle.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.menu_home:
                manager.popBackStack();
                finish();
                break;
            case R.id.menu_map:
                Toast.makeText(this, "map", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_chart:
                Toast.makeText(this, "chart", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar,menu);
        menu.findItem(R.id.menu_chart).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    String CalDayOfWeek(java.util.Calendar day){
        int nWeek = day.get(java.util.Calendar.DAY_OF_WEEK);
        String strWeek = null;
        switch (nWeek){
            case 1:
                strWeek="일";
                break;
            case 2:
                strWeek="월";
                break;
            case 3:
                strWeek="화";
                break;
            case 4:
                strWeek="수";
                break;
            case 5:
                strWeek="목";
                break;
            case 6:
                strWeek="금";
                break;
            case 7:
                strWeek="토";
                break;
        }
        return strWeek;
    }

}
