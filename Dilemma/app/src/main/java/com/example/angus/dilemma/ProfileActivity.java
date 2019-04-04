package com.example.angus.dilemma;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.*;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    LayoutInflater l;
    LinearLayout linear;
    int userID, qID;
    SQLiteDatabase db;
    Cursor qc, ac;
    String q_text, left_text, right_text;
    int left_click, right_click, total_click;
    float left_weight, right_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_p);
        setSupportActionBar(toolbar);

        l = getLayoutInflater();
        linear = (LinearLayout) findViewById(R.id.profile_linear);
        Log.d("PROFILE", "inflated");


        loadResults();

    }

    private void loadResults(){
        Log.d("PROFILE", "loadRes");

        db = new DBHandler(this).getWritableDatabase();

        userID = getIntent().getExtras().getInt("userID");

        String[] qColumns = {"_QuestionID", "Question"};
        String[] args = {userID+""};

        qc = db.query("Question", // a. table
                qColumns, // b. column names
                "_UserID = ?",// c. selections
                args, // selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        Log.d("PROFILE", "qc");

        String results = "SELECT _QuestionID, Question FROM Question WHERE _UserID = ?";
        qc = db.rawQuery(results, new String[] {userID+""});

        qc.moveToLast();
        Log.d("PROFILE", "mtf");


        while (!qc.isBeforeFirst()){
            qID = qc.getInt(0);
            Log.d("PROFILE", qID+" id");

            results = "SELECT AnswerText, NoOfClicks FROM Answer WHERE _QuestionID = ?";
            ac = db.rawQuery(results, new String[] {qID+""});


            ac.moveToFirst();

            q_text = qc.getString(1);
            left_text = ac.getString(0);
            left_click = ac.getInt(1);

            ac.moveToNext();

            right_text = ac.getString(0);
            right_click = ac.getInt(1);

            Log.d("PROFILE", left_click+" l");
            Log.d("PROFILE", right_click+" r");


            View card = l.inflate(R.layout.result_card, linear, false);

            TextView question = card.findViewById(R.id.result_question);
            TextView left = card.findViewById(R.id.result_left);
            TextView right = card.findViewById(R.id.result_right);

            question.setText(q_text);
            left.setText(left_text);
            right.setText(right_text);
            TextView left_p = card.findViewById(R.id.left_percent);
            TextView right_p = card.findViewById(R.id.right_percent);

            total_click = right_click + left_click;
            if (total_click!=0){

                LinearLayout.LayoutParams leftparams = (LinearLayout.LayoutParams) left.getLayoutParams();
                left_weight = (float)left_click / (float) total_click;
                leftparams.weight = left_weight;
                Log.d("leftWeight", left_weight+"");
                left.setLayoutParams(leftparams);

                LinearLayout.LayoutParams rightparams = (LinearLayout.LayoutParams) right.getLayoutParams();
                right_weight = (float) right_click / (float) total_click;
                rightparams.weight = right_weight;
                Log.d("rightWeight", right_weight+"");
                right.setLayoutParams(rightparams);
            }
            int leftPercent = (int) (left_weight*100);
            int rightPercent = (int) (right_weight*100);
            left_p.setText(leftPercent+"%");
            right_p.setText(rightPercent+"%");



            linear.addView(card);
            qc.moveToPrevious();
        }
    }
}
