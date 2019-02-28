package com.example.angus.dilemma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RegisterActivity extends AppCompatActivity {

    EditText email, usern, pass1, pass2;
    TextView user_err, pass_err, email_err;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_r);
        setSupportActionBar(toolbar);

        email = (EditText) findViewById(R.id.reg_email);
        usern = (EditText) findViewById(R.id.reg_user);
        pass1 = (EditText) findViewById(R.id.reg_pass);
        pass2 = (EditText) findViewById(R.id.reg_pass_confirm);

        email_err = (TextView) findViewById(R.id.redText_email);
        user_err = (TextView) findViewById(R.id.redText_user);
        pass_err = (TextView) findViewById(R.id.redText_pass);

        register = (Button) findViewById(R.id.reg_acc);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    registerUser();

                    Intent intent = new Intent();
                    intent.putExtra("user", usern.getText().toString());
                    setResult(RESULT_OK, intent);

                    finish();
                }

            }
        });
    }

    //cancels return value expected by previous activity
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, new Intent());
        super.onBackPressed();
    }

    //checks all text fields in register page to ensure they are valid before they can be sent to the DB
    private boolean checkFields(){

        boolean result = true;

       //-----EMAIL CHECKS----------

        if(false /*email taken*/){
            email_err.setText("EMAIL ADDRESS ALREADY IN USE");
            result = false;
        }else if (false/*invalid email address entered*/){
            email_err.setText("PLEASE ENTER A VALID EMAIL");
        }
        else email_err.setText("");

       //-----USERNAME CHECKS--------

        if(false//username contains invalid Chars, check for invalid chars here
        ) {
            user_err.setText("USERNAME CONTAINS INVALID CHARACTERS");
            result = false;
        }
        else if(false//username taken, query to see if username in in use here
        ){
            user_err.setText("USERNAME TAKEN");
            result = false;
        }
        else user_err.setText("");

        //------PASSWORD CHECKS-------

        if(false//password contains invalid chars, check here, might change to check each char as typed/
            //only check for pass1, check pass2 matches
        ){
            pass_err.setText("PASSWORD CONTAINS INVALID CHARACTERS");
            result = false;
        }
        else if(!pass1.getText().toString().equals(pass2.getText().toString())) {//passwords don't match
            pass_err.setText("PASSWORDS DO NOT MATCH");
            result = false;
        }
        else pass_err.setText("");

        return result;
    }

    //Puts valid details into DB
    private void registerUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        //get stuff into DB
        email.getText().toString();
        usern.getText().toString();


        Hashing hashed = new Hashing( pass1.getText().toString());
        String salt =hashed.getSalt().toString();
        String hashPass=hashed.getHash().toString();






    }
}
