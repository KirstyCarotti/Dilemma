package com.example.angus.dilemma;

class Question {
    String qstn, ans1, ans2;
    int qstnID;
    int[] tags;

    public Question(int qID, String q, String a1, String a2 /*int[] t*/){
        qstn = q; ans1 = a1; ans2 = a2;
        qstnID = qID;
        //tags = t;
    }
}
