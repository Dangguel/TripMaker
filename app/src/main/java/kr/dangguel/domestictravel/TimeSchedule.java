package kr.dangguel.domestictravel;

import java.io.Serializable;

public class TimeSchedule implements Comparable<TimeSchedule> , Serializable {
    String placeTodo;
    double mapLat,mapLng;
    String time;
    String cost;
    String detailplan;
    String spinselect;

    public TimeSchedule(String placeTodo, double mapLat, double mapLng, String time, String cost, String detailplan ,String spinselect) {
        this.placeTodo = placeTodo;
        this.mapLat = mapLat;
        this.mapLng = mapLng;
        this.time = time;
        this.cost = cost;
        this.detailplan = detailplan;
        this.spinselect = spinselect;
    }

    @Override
    public int compareTo(TimeSchedule o) {
        int a = Integer.parseInt(this.time.replace(":",""));
        int b = Integer.parseInt(o.time.replace(":",""));

        if(a<b){
            return -1;
        }else if(a>b){
            return 1;
        }else return 0;
    }
}
