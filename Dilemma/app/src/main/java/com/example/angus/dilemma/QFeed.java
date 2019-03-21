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
    Cursor answerCursor;
    Cursor questionCursor;
    Cursor ansQueCursor;
    int qID = -1;
    String qText;
    boolean empty = false;
    int userID;

    //will take in user profile so that appropriate questions are shown
    //eg. dont show own q, don't show answered, filter by tags etc.
    public QFeed(SQLiteDatabase db, int userID){
        this.userID = userID;
        sFirst = new Stack();
        sSecond = new Stack();
        this.db = db;
        index = 0;


        questionCursor = db.query("Question", // a. table
                qColumns, // b. column names
                "_QuestionID NOT IN (SELECT _QuestionID FROM AnsQue WHERE _UserID = " + userID +")",// c. selections
                null,
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        questionCursor.moveToFirst();

        if(questionCursor.getCount()==0)empty = true;

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

        index++;

        if(!empty) {
            qID = questionCursor.getInt(0);
            qText = questionCursor.getString(1);
            Log.d("QUESTION PRINTED:", qID + qText);

        } else {
            return new Question(-1,null,null,null);
        }

        if (!questionCursor.isLast()) {
            questionCursor.moveToNext();
        } else empty = true;

        answerCursor = db.rawQuery("SELECT AnswerText FROM Answer WHERE _QuestionID = ?", new String[] {qID+ ""});

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
                qText,

                //Answer 1
                answer1,

                //Answer 2
                answer2
        );

    }

}
