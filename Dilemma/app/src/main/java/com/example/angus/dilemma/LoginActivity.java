package com.example.angus.dilemma;

import android.content.Intent;
import android.support.annotation.Nullable;
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
    private static final int REGISTER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_l);
        setSupportActionBar(toolbar);

        //linking layout elements

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.redText);

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin()) {

                    Intent intent = new Intent();
                    intent.putExtra("username", username.getText().toString());
                    //userID = LOOKUP UserID AND RETURN HERE
                    intent.putExtra("userID", /*userID*/ 0 );
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
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

    private void openRegister(){
        startActivityForResult(new Intent(this, RegisterActivity.class), REGISTER_RESULT);
    }

    private boolean checkLogin(){
        //check login matches db

        //DB PUT QUERY IN IF STATEMENT BELOW
        if (!username.getText().toString().equals("") && !password.getText().toString().equals("")
                /*for now successful login = any input*/){
            return true;
        }else{
            error.setText("INVALID LOGIN DETAILS");
            password.setText("");
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //do we even let them press back? regardless userID if done so becomes -1 so can easily see when no logged in
        //un-comment below code to allow no login

        //setResult(RESULT_CANCELED, new Intent());
        //super.onBackPressed();

    }

    //again like in main, handles different activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case REGISTER_RESULT:
                if (resultCode == RESULT_OK){
                    username.setText(data.getStringExtra("user"));
                }
        }
    }
}
