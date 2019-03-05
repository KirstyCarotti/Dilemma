package com.example.angus.dilemma;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;

class QFeed {

    Stack sFirst, sSecond;
    //profile / userID
    static int index;
    SQLiteDatabase db;
    String[] qColumns = {"_QuestionID", "Question"};
    String[] aColumns = {"AnswerText"};

    //will take in user profile so that appropriate questions are shown
    //eg. dont show own q, don't show answered, filter by tags etc.
    public QFeed(SQLiteDatabase db){

        sFirst = new Stack();
        sSecond = new Stack();
        this.db = db;
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

    public void refresh(){
        populate();
        populate();
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

        boolean empty = true;


        if (empty) return new Question(-1, null, null, null);

        index++;

        Cursor questionCursor = db.query("Question", // a. table
                 qColumns, // b. column names
                "_QuestionID = " + index, // c. selections
                null,
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (!questionCursor.isAfterLast()) {
            questionCursor.moveToFirst();
            Log.d("QUESTION PRINTED:", questionCursor.getInt(0) + questionCursor.getString(1));
        } else {
            return new Question(-1, null, null, null);
        }

        int qID = questionCursor.getInt(0);

        Cursor answerCursor = db.query("Answer", // a. table
                 aColumns, // b. column names
                "_QuestionID = " + qID, // c. selections
                null,
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        answerCursor.moveToFirst();
        String answer1 = answerCursor.getString(0);
        Log.d("ANSWER PRINTED:", answer1);

        answerCursor.moveToNext();
        String answer2 = answerCursor.getString(0);
        Log.d("ANSWER PRINTED:", answer2);


        //replace code with query on each line after comment
        return new Question(

                //Question ID
                index,

                //Question Text
                questionCursor.getString(1),

                //Answer 1
                answer1,

                //Answer 2
                answer2
        );

    }

}
