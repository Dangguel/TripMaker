package kr.dangguel.domestictravel;

public class CheckVO {
    String item;
    boolean check=false;

    public CheckVO(String item) {
        this.item = item;
    }

    public void checkChange(){
        if(check)
            check=false;
        else
            check=true;
    }
}
