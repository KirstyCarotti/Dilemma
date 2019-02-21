package com.example.angus.dilemma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_l);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.redText);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin()) finish();
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open register page
            }
        });

        CheckBox showpass = (CheckBox) findViewById(R.id.showpass);
        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    password.setTransformationMethod(null);
                }else password.setTransformationMethod(new PasswordTransformationMethod());

                password.setSelection(password.getText().length());
            }
        });
    }

    private boolean checkLogin(){
        //check login matches db

        if (!username.getText().toString().equals("") && !password.getText().toString().equals("")
                /*for now successful login = any input*/){

            return true;
            //figure out how to pass successful login to main activity

        }else{
            error.setText("INVALID LOGIN DETAILS");
            password.setText("");
            return false;
        }
    }
}
