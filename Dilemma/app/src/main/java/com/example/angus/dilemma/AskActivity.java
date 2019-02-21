package com.example.angus.dilemma;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AskActivity extends AppCompatActivity {

    EditText qstn, ans1, ans2;
    Button submit;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHandler(this).getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_a);
        setSupportActionBar(toolbar);

        qstn = (EditText) findViewById(R.id.question_input);
        ans1 = (EditText) findViewById(R.id.answer_input_1);
        ans2 = (EditText) findViewById(R.id.answer_input_2);

        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitQuestion();
            }
        });
    }

    private void submitQuestion(){
        //use these to pull text from form:
        String question = qstn.getText().toString();
        String answerText1 = ans1.getText().toString();
        String answerText2 = ans2.getText().toString();

        //add tags too later
        int categoryID = 0;

        //get Profile id somehow later
        int userID = 0;

        ContentValues qValues = new ContentValues();
        qValues.put("_UserID", userID);
        qValues.put("Question", question);
        qValues.put("_CategoryID", categoryID);

        // add ID, question, ans1, ans2, category (currently default)
        long questionID = db.insert("Question", null, qValues);

        ContentValues a1Values = new ContentValues();
        a1Values.put("AnswerText", answerText1);
        a1Values.put("_QuestionID", questionID);
        db.insert("Answer", null, a1Values);

        ContentValues a2Values = new ContentValues();
        a2Values.put("AnswerText", answerText2);
        a2Values.put("_QuestionID", questionID);
        db.insert("Answer", null, a2Values);

        //placeholder action below
        this.finish();
    }

    @Override
    public void onBackPressed(){
        if(!qstn.getText().toString().equals("")
                ||!ans1.getText().toString().equals("")
                ||!ans2.getText().toString().equals("")){
            //popup to confirm discard of question here:
            //atm cant go back with fields not empty, submit achieves same function

        }else super.onBackPressed();
    }
}
