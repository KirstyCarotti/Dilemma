package com.example.angus.dilemma;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText usern, pass1, pass2;
    TextView user_err, pass_err;
    Button register;
    SQLiteDatabase db;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        dbHandler = new DBHandler(this);
        db = dbHandler.getWritableDatabase();

        //dbHandler.onUpgrade(db, 0, 0);
        //db = dbHandler.getWritableDatabase();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_r);
        setSupportActionBar(toolbar);

        usern = (EditText) findViewById(R.id.reg_user);
        pass1 = (EditText) findViewById(R.id.reg_pass);
        pass2 = (EditText) findViewById(R.id.reg_pass_confirm);

        user_err = (TextView) findViewById(R.id.redText_user);
        pass_err = (TextView) findViewById(R.id.redText_pass);

        register = (Button) findViewById(R.id.reg_acc);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    try {
                        registerUser(); //CHECK RETURN IF TRUE CONTINUE IF FALSE DON'T?
                    }
                    catch(NoSuchAlgorithmException e){
                        e.printStackTrace();

                    }catch(java.security.spec.InvalidKeySpecException i) {
                        i.printStackTrace();
                    }

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

    private boolean usernameTaken(){
        String username = usern.getText().toString();
        String Query = "SELECT * FROM User WHERE Username =?";
        Cursor cursor = db.rawQuery(Query, new String[] {username+ ""});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        return false;
    }

    private boolean containsInvalidChars(EditText str) {
        String toCheck = str.getText().toString();
        Pattern allowedCharacters = Pattern.compile("[A-Za-z0-9!\"#$%&'()*+,.\\/:;<=>?@[\\]^_`{|}~-]+$]+");

        if (toCheck.matches("[A-Za-z0-9!\"?!]+")) {
            return false;
        } else {
            return true;
        }
    }

    //checks all text fields in register page to ensure they are valid before they can be sent to the DB
    private boolean checkFields(){

        boolean result = true;

       //-----USERNAME CHECKS--------

        if(containsInvalidChars(usern)) {
            user_err.setText("USERNAME CONTAINS INVALID CHARACTERS");
            result = false;
        } else if (usernameTaken()) {
            user_err.setText("USERNAME TAKEN");
            result = false;
        } else user_err.setText("");

        //------PASSWORD CHECKS-------

        if (containsInvalidChars(pass1)){
            pass_err.setText("PASSWORD CONTAINS INVALID CHARACTERS");
            result = false;
        } else if(!pass1.getText().toString().equals(pass2.getText().toString())) {//passwords don't match
            pass_err.setText("PASSWORDS DO NOT MATCH");
            result = false;
        } else if(pass1.getText().length() <= 6) {//passwords don't match
            pass_err.setText("PASSWORDS TOO SHORT");
            result = false;
        } else pass_err.setText("");

        return result;
    }

    //Puts valid details into DB
    private void registerUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String username = usern.getText().toString();

        Hashing hashed = new Hashing(pass1.getText().toString());
        String salt = hashed.getSalt();
        String hashPass = hashed.getHash();

        ContentValues qValues = new ContentValues();
        qValues.put("Salt", salt);
        qValues.put("Username", username);
        qValues.put("Password", hashPass);



        try{
            long UserID = db.insert("User", null, qValues); //CHECK IF WORKS
            Log.d("USER ID PRINTED:", String.valueOf(UserID));

        }catch (SQLiteException e){
            //try on upgrade
            //dbHandler.onUpgrade(db, 0, 0);

            //end activity in nice way
            Intent intent = new Intent();
            intent.putExtra("error", "SQLiteException");
            setResult(RESULT_CANCELED, intent);

            finish();
        }


    }
}