package com.example.angus.dilemma;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.util.Log;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Dilemma";

    /* DB Constructor */
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    /* Creates all the tables */
    public void onCreate(SQLiteDatabase db) {
        String sqlUsers = "CREATE TABLE IF NOT EXISTS User(_UserID INTEGER, Username TEXT, Email TEXT, Password TEXT, _CategoryID INTEGER, PRIMARY KEY(_UserID));";
        String sqlQuestions = "CREATE TABLE IF NOT EXISTS Question(_QuestionID INTEGER, _UserID INTEGER, Question TEXT, Answer1 TEXT, Answer2 TEXT, _CategoryID INTEGER, PRIMARY KEY(_QuestionID));";
        String sqlPreferences = "CREATE TABLE IF NOT EXISTS Preferences(_UserID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, ShowComment INTEGER, Anonymous INTEGER);";
        String sqlFriend = "CREATE TABLE IF NOT EXISTS Friend(_UserID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, _FriendID INTEGER, FOREIGN KEY(_FriendID) REFERENCES User);";
        String sqlComment = "CREATE TABLE IF NOT EXISTS Comment(_CommentID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, _UserID INTEGER, _AnswerID INTEGER, CommentDescription TEXT, FOREIGN KEY(_UserID) REFERENCES User(_UserID), FOREIGN KEY(_AnswerID) REFERENCES Answer(_AnswerID));";
        String sqlCategory = "CREATE TABLE IF NOT EXISTS Category(_CategoryID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, CategoryName TEXT);";
        String sqlAnswer = "CREATE TABLE IF NOT EXISTS Answer(_AnswerID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, _QuestionID INTEGER, AnswerText TEXT, NoOfClicks INTEGER, Colour INTEGER, FOREIGN KEY(_QuestionID) REFERENCES Question(_QuestionID));";

        db.execSQL(sqlUsers);
        db.execSQL(sqlQuestions);
        db.execSQL(sqlPreferences);
        db.execSQL(sqlFriend);
        db.execSQL(sqlComment);
        db.execSQL(sqlCategory);
        db.execSQL(sqlAnswer);
        Log.d("Create Database:", "Successful");
    }

    // stuff below needs tweaking, no longer need feedreader ? ----------------

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Question");
        db.execSQL("DROP TABLE IF EXISTS Preferences");
        db.execSQL("DROP TABLE IF EXISTS Friend");
        db.execSQL("DROP TABLE IF EXISTS Comment");
        db.execSQL("DROP TABLE IF EXISTS Category");
        db.execSQL("DROP TABLE IF EXISTS Answer");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}