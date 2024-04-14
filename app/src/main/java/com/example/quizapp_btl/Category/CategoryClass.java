package com.example.quizapp_btl.Category;

public class CategoryClass {
    private String keys;
    private String nameCate;

    private String ImageCate;

    private int setNums;

    public CategoryClass(String keys,String nameCate, String imageCate, int setNums) {
        this.keys = keys;
        this.nameCate = nameCate;
        ImageCate = imageCate;
        this.setNums = setNums;
    }

    public CategoryClass() {
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    public String getImageCate() {
        return ImageCate;
    }

    public void setImageCate(String imageCate) {
        ImageCate = imageCate;
    }

    public int getSetNums() {
        return setNums;
    }

    public void setSetNums(int setNums) {
        this.setNums = setNums;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}
