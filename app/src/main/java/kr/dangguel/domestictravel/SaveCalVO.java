package kr.dangguel.domestictravel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

public class SaveCalVO {

    String range;
    String days;
    String title;

    int untilday;

    CalendarDay makeDay;
    CalendarDay day1;
    CalendarDay today;

    public SaveCalVO(String range, String days, String title, CalendarDay makeDay, CalendarDay day1) {
        this.range = range;
        this.days = days;
        this.title = title;
        this.makeDay = makeDay;
        this.day1 = day1;
        today = CalendarDay.today();

        java.util.Calendar CalMakeday = new java.util.GregorianCalendar(this.makeDay.getYear(),this.makeDay.getMonth(),this.makeDay.getDay());
        java.util.Calendar Calday1 = new java.util.GregorianCalendar(this.day1.getYear(),this.day1.getMonth(),this.day1.getDay());


        long diffToDays = (Calday1.getTimeInMillis() - CalMakeday.getTimeInMillis())/1000;
        diffToDays = diffToDays/(24*60*60);
        this.untilday = (int) diffToDays;

    }

    public void changeToday(){
        today = CalendarDay.today();
    }
}
