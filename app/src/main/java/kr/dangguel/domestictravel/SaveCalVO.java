package kr.dangguel.domestictravel;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SaveCalVO {

    String range;
    String days;
    String title;

    int untilday;

    int makeDayY;
    int makeDayM;
    int makeDayD;

    int day1Y;
    int day1M;
    int day1D;

    int todayY;
    int todayM;
    int todayD;

    public SaveCalVO(String range, String days, String title, int makeDayY, int makeDayM, int makeDayD, int day1Y, int day1M, int day1D, int todayY, int todayM, int todayD) {
        this.range = range;
        this.days = days;
        this.title = title;

        this.makeDayY = makeDayY;
        this.makeDayM = makeDayM;
        this.makeDayD = makeDayD;

        this.day1Y = day1Y;
        this.day1M = day1M;
        this.day1D = day1D;

        this.todayY = todayY;
        this.todayM = todayM;
        this.todayD = todayD;

        Calendar Calday1 = new GregorianCalendar(day1Y, day1M, day1D);
        Calendar CalToday = new GregorianCalendar(todayY, todayM, todayD);

        long diffToDays1 = (Calday1.getTimeInMillis() - CalToday.getTimeInMillis()) / 1000;
        diffToDays1 = diffToDays1 / (24 * 60 * 60);
        untilday = (int) diffToDays1;


    }

    public SaveCalVO(String range, String days, String title, int makeDayY, int makeDayM, int makeDayD, int day1Y, int day1M, int day1D) {

        this.range = range;
        this.days = days;
        this.title = title;

        this.makeDayY = makeDayY;
        this.makeDayM = makeDayM;
        this.makeDayD = makeDayD;

        this.day1Y = day1Y;
        this.day1M = day1M;

        this.day1D = day1D;

        Calendar CalMakeday = new GregorianCalendar(makeDayY, makeDayM, makeDayD);
        Calendar Calday1 = new GregorianCalendar(day1Y, day1M, day1D);

        long diffToDays = (Calday1.getTimeInMillis() - CalMakeday.getTimeInMillis()) / 1000;
        diffToDays = diffToDays / (24 * 60 * 60);
        untilday = (int) diffToDays;

    }
}
