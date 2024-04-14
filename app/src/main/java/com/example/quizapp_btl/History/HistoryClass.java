package com.example.quizapp_btl.History;

public class HistoryClass {
    private String categoriesId;
    private String nameCate;
    private String correctAnsw;
    private String wrongAns;
    private String totalAns;

    public HistoryClass(String categoriesId,String nameCate, String correctAnsw, String wrongAns, String totalAns) {
        this.nameCate = nameCate;
        this.correctAnsw = correctAnsw;
        this.wrongAns = wrongAns;
        this.totalAns = totalAns;
        this.categoriesId = categoriesId;
    }

    public HistoryClass() {
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    public String getCorrectAnsw() {
        return correctAnsw;
    }

    public void setCorrectAnsw(String correctAnsw) {
        this.correctAnsw = correctAnsw;
    }

    public String getWrongAns() {
        return wrongAns;
    }

    public void setWrongAns(String wrongAns) {
        this.wrongAns = wrongAns;
    }

    public String getTotalAns() {
        return totalAns;
    }

    public void setTotalAns(String totalAns) {
        this.totalAns = totalAns;
    }

    public String getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(String categoriesId) {
        this.categoriesId = categoriesId;
    }
}
