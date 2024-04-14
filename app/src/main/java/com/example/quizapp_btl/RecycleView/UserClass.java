package com.example.quizapp_btl.RecycleView;

public class UserClass {
    private String nameSettings;

    private String imgSettings;


    public UserClass(){
    }

    public UserClass(String nameSettings, String imgSettings) {
        this.nameSettings = nameSettings;
        this.imgSettings = imgSettings;
    }

    public String getNameSettings() {
        return nameSettings;
    }

    public void setNameSettings(String nameSettings) {
        this.nameSettings = nameSettings;
    }

    public String getImgSettings() {
        return imgSettings;
    }

    public void setImgSettings(String imgSettings) {
        this.imgSettings = imgSettings;
    }


}
