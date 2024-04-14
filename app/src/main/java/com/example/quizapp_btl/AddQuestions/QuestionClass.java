package com.example.quizapp_btl.AddQuestions;

public class QuestionClass {
    private String idCate;
    private String idOWn;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String correctOpt;
    private String TitleQues;

    public QuestionClass() {
    }

    public QuestionClass(String idOWn, String idCate,String optA, String optB, String optC, String optD, String correctOpt, String titleQues) {
        this.idOWn = idOWn;
        this.idCate = idCate;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.correctOpt = correctOpt;
        TitleQues = titleQues;
    }

    public String getOptA() {
        return optA;
    }

    public void setOptA(String optA) {
        this.optA = optA;
    }

    public String getOptB() {
        return optB;
    }

    public void setOptB(String optB) {
        this.optB = optB;
    }

    public String getOptC() {
        return optC;
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getCorrectOpt() {
        return correctOpt;
    }

    public void setCorrectOpt(String correctOpt) {
        this.correctOpt = correctOpt;
    }

    public String getTitleQues() {
        return TitleQues;
    }

    public void setTitleQues(String titleQues) {
        TitleQues = titleQues;
    }

    public String getIdOWn() {
        return idOWn;
    }

    public void setIdOWn(String id) {
        this.idOWn = id;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

}
