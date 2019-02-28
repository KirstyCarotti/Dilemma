package com.example.angus.dilemma;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

class QFeed {

    Stack sFirst, sSecond;
    //profile / userID
    static int index;
    SQLiteDatabase db;

    //will take in user profile so that appropriate questions are shown
    //eg. dont show own q, don't show answered, filter by tags etc.
    public QFeed(SQLiteDatabase db){
        sFirst = new Stack();
        sSecond = new Stack();
        db = db;
        index = 0;
        populate();
        populate();
    }

    private void populate(){

        sFirst = sSecond;
        sSecond = new Stack();
        while(!sSecond.isFull())
            sSecond.push(pull());
        //sFirst.pointer=0;

    }

    public Question next(){

        Question q = sFirst.pop();
        if (q != null) return q;
        else {
            populate();
            return next();
        }
    }

    private Question pull(){
        //creates Question object from DB

        //filler code until db created
        index++;
        int count = 1;
        String newQuestion = "SELECT Question.Question, Answer.AnswerText FROM Question INNER JOIN Answer ON Answer._QuestionID = Question._QuestionID WHERE Question._QuestionID = " + count;
        db.execSQL(newQuestion);

        count++;

        Log.d("PRINTED:", newQuestion);

        //replace code with query on each line after comment
        return new Question(


                //Question ID
                index,

                //Question Text
                "Question no."+index,

                //Left text
                "Left no."+index,

                //Right Text
                "Right no."+index
        );

    }

}
