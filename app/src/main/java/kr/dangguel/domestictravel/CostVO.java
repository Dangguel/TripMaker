package kr.dangguel.domestictravel;

import java.io.Serializable;

public class CostVO implements Comparable<CostVO> , Serializable {
    String placeToDo;
    String time;
    String detailPlan;
    String cost;
    String costType;

    public CostVO(String placeToDo, String time, String detailPlan, String cost, String costType) {
        this.placeToDo = placeToDo;
        this.time = time;
        this.detailPlan = detailPlan;
        this.cost = cost;
        this.costType = costType;
    }

    @Override
    public int compareTo(CostVO o) {
        int a = Integer.parseInt(this.time.replace(":",""));
        int b = Integer.parseInt(o.time.replace(":",""));
        if(a<b){
            return -1;
        }else if(a>b){
            return 1;
        }else return 0;
    }
}
