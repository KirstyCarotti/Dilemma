package com.example.angus.dilemma;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        qstn.getText().toString();
        ans1.getText().toString();
        ans2.getText().toString();

        //add tags too later
        //get Profile id somehow later

        //update database

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
