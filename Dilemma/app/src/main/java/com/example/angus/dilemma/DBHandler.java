package com.example.angus.dilemma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
        String sqlUsers = "CREATE TABLE IF NOT EXISTS User(UserID INTEGER, Username TEXT, Email TEXT, Password TEXT, CategoryID	INTEGER, PRIMARY KEY(UserID));";
        String sqlQuestions = "CREATE TABLE IF NOT EXISTS Question(QuestionID INTEGER, UserID INTEGER, Question TEXT, Answer1 TEXT, Answer2 TEXT, CategoryID INTEGER, PRIMARY KEY(QuestionID));";
        String sqlPreferences = "CREATE TABLE IF NOT EXISTS Preferences(UserID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, ShowComment INTEGER, Anonymous INTEGER);";
        String sqlFriend = "CREATE TABLE IF NOT EXISTS Friend(UserID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, FriendID INTEGER, FOREIGN KEY(FriendID) REFERENCES User);";
        String sqlComment = "CREATE TABLE IF NOT EXISTS Comment(CommentID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, UserID INTEGER, AnswerID INTEGER, CommentDescription TEXT, FOREIGN KEY(UserID) REFERENCES User(UserID), FOREIGN KEY(AnswerID) REFERENCES Answer(AnswerID));";
        String sqlCategory = "CREATE TABLE IF NOT EXISTS Category(CategoryID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, CategoryName TEXT);";
        String sqlAnswer = "CREATE TABLE IF NOT EXISTS Answer(AnswerID INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, QuestionID INTEGER, AnswerText TEXT, NoOfClicks INTEGER, Colour INTEGER, FOREIGN KEY(QuestionID) REFERENCES Question(QuestionID));";

        db.execSQL(sqlUsers);
        db.execSQL(sqlQuestions);
        db.execSQL(sqlPreferences);
        db.execSQL(sqlFriend);
        db.execSQL(sqlComment);
        db.execSQL(sqlCategory);
        db.execSQL(sqlAnswer);
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