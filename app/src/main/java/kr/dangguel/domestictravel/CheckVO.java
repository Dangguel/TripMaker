package kr.dangguel.domestictravel;

public class CheckVO {
    String item;
    boolean check=false;

    public CheckVO(String item) {
        this.item = item;
    }

    public CheckVO(String item, boolean check) {
        this.item = item;
        this.check = check;
    }

    public void checkChange(){
        if(check)
            check=false;
        else
            check=true;
    }
}
