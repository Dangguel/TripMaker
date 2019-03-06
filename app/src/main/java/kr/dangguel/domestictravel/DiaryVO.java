package kr.dangguel.domestictravel;

import android.net.Uri;

import java.io.Serializable;

public class DiaryVO implements Serializable {
    String picPath;
    String memo;

    public DiaryVO(String picPath, String memo) {
        this.picPath = picPath;
        this.memo = memo;
    }

    public DiaryVO(String picPath) {
        this.picPath = picPath;
        this.memo = "";
    }

    public DiaryVO() {

    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
