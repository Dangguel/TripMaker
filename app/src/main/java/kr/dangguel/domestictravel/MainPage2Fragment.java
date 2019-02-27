package kr.dangguel.domestictravel;

import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

public class MainPage2Fragment extends Fragment {
    View view;
    TextView tvDays;
    TextView tvRange;
    MaterialCalendarView calendarView;
    ImageView iv;
    CalendarDay day1;
    CalendarDay day2;
    CalendarDay makeDay;
    Bundle bundle;
    EditText dialogEt;
    boolean isEdit=false;

    String d1,d2;

    MainPage1Fragment frag1;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_page2_fragment, container, false);
        tvDays = view.findViewById(R.id.tv_days);
        tvRange = view.findViewById(R.id.tv_range);
        calendarView = view.findViewById(R.id.calendarView);
        iv = view.findViewById(R.id.iv);
        makeDay = CalendarDay.today();
        bundle = new Bundle();

        makeCal();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvRange == null && tvDays == null) return;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("여행 제목 설정");
                builder.setMessage("여행 제목을 입력해주세요.\n글자수는 최대 15자입니다.");
                View layout = inflater.inflate(R.layout.cal_dialog,null);

                dialogEt = layout.findViewById(R.id.dialog_et);

                builder.setView(layout);

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity activity = (MainActivity) getActivity();

                        frag1 = activity.frag1;
                        if(!isEdit) {
                            if(calendarView.getSelectionMode()==MaterialCalendarView.SELECTION_MODE_SINGLE)
                                activity.cals.add(new SaveCalVO(tvRange.getText().toString(), tvDays.getText().toString() + "간 여행", dialogEt.getText().toString(), makeDay, day1));
                            else
                                activity.cals.add(new SaveCalVO(d1 + " ~ " + d2, tvDays.getText().toString() + "간 여행", dialogEt.getText().toString(), makeDay, day1));
                        }else{
                            if(calendarView.getSelectionMode()==MaterialCalendarView.SELECTION_MODE_SINGLE)
                                activity.cals.add(activity.getIndex(),new SaveCalVO(tvRange.getText().toString(), tvDays.getText().toString() + "간 여행", dialogEt.getText().toString(), makeDay, day1));
                            else
                                activity.cals.add(activity.getIndex(),new SaveCalVO(d1 + " ~ " + d2, tvDays.getText().toString() + "간 여행", dialogEt.getText().toString(), makeDay, day1));
                            isEdit=false;
                        }
                        frag1.updateAdapter();
                        Toast.makeText(getContext(), "제목이 입력 되었습니다.", Toast.LENGTH_SHORT).show();

                        activity.startFragment1();

                        tvRange.setText("여행 기간");
                        tvDays.setText("여행 일수");

                        day1=null;
                        day2=null;
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();



            }
        });

        return view;
    }

    OnDateSelectedListener dateSelectedListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            if (calendarView.getSelectionMode() == MaterialCalendarView.SELECTION_MODE_RANGE) {
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                day1 = date;
                calDate(day1);
                return;
            }
            if (day1 == null) {
                day1 = date;
                calDate(day1);
            } else {
                day2 = date;
                if (day1.isBefore(day2)) {
                    calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
                    calendarView.setOnRangeSelectedListener(rangeSelectedListener);
                    calendarView.selectRange(day1, day2);
                } else {
                    calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
                    day1 = date;
                    calDate(day1);
                }
            }
        }

        OnRangeSelectedListener rangeSelectedListener = new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                int size = dates.size();
                CalendarDay date1 = dates.get(0);
                CalendarDay date2 = dates.get(size - 1);

                java.util.Calendar Calday1 = new java.util.GregorianCalendar(date1.getYear(),date1.getMonth(),date1.getDay());
                java.util.Calendar Calday2 = new java.util.GregorianCalendar(date2.getYear(),date2.getMonth(),date2.getDay());

                String day1 = CalDayOfWeek(Calday1);
                String day2 = CalDayOfWeek(Calday2);

                long diffRange = (Calday2.getTimeInMillis() - Calday1.getTimeInMillis())/1000;
                diffRange = diffRange/(24*60*60);

                d1=date1.getYear() + "-" + (date1.getMonth() + 1) + "-" + date1.getDay() +"("+day1+")";
                d2=date2.getYear() + "-" + (date2.getMonth() + 1) + "-" + date2.getDay() +"("+day2+")";

                tvRange.setText(d1+ "\n ~ \n" +d2);
                tvDays.setText( (diffRange+1) + " 일");

            }
        };
    };

    void calDate(CalendarDay date) {
        java.util.Calendar day = date.getCalendar();
        String day1 = CalDayOfWeek(day);
        tvRange.setText(date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay() +"("+day1+")");
        tvDays.setText("1 일");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            makeCal();
        }
        else{
            if(calendarView!=null) {
                calendarView.removeDecorators();
                calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
                calendarView = null;
            }
        }
    }

    void makeCal(){
        day1=null;
        day2=null;
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(makeDay.getYear(), makeDay.getMonth(), 1))
                .setMaximumDate(CalendarDay.from(makeDay.getYear() + 5, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("M월 yyyy");
        calendarView.setTitleFormatter(new DateFormatTitleFormatter(dateFormat));

        calendarView.addDecorators(new CalSundayDecorator(), new CalSaturdayDecorator(), new CalTodayDecorator());
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);

        calendarView.setOnDateChangedListener(dateSelectedListener);

    }
    void editMode(){
        isEdit=true;
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
