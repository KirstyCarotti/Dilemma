package com.example.angus.dilemma;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.view.View.OnTouchListener;

public class
MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    QFeed qf;
    Question currQ;
    Button left, right;
    TextView q;
    DBHandler db;
    boolean dbSuccess = false;
    public static final int LOGIN_RESULT = 1;
    public static final int ASK_RESULT = 2;
    int userID;
    String username = "";

    CardView card;
    RelativeLayout r_layout;
    private float xDelta;
    private float yDelta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------STARTUP STUFF---------

        db = new DBHandler(this);
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        //db.createDummyQuestions(sqLiteDatabase);
        //db.dummyDelete(sqLiteDatabase);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---------ASK QUESTION BUTTON----------

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AskActivity.class);
                intent.putExtra("userID", userID);
                startActivityForResult(intent, ASK_RESULT);
            }
        });

        //--------SIDE NAVIGATION DRAWER-----------

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //-------------login stuff here -------------

        //TextView sb_username = (TextView) findViewById(R.id.sidebar_un);

        if(true/*logged in */){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_RESULT);
        }



        //-----------UPDATES QUESTION AND ANSWER FIELDS BELOW-----------


        q = (TextView) findViewById(R.id.question);


        left = (Button) findViewById(R.id.button_l);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //record vote
                updateQ();

            }
        });

        right = (Button) findViewById(R.id.button_r);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ///record vote
                updateQ();
            }
        });


        r_layout = (RelativeLayout) findViewById(R.id.r_layout);
        card = (CardView) findViewById(R.id.cardView);
        card.setOnTouchListener(onTouchListener());

        dbSuccess = qfInit(sqLiteDatabase);
        if (!dbSuccess){
            dbSuccess = qfInit(sqLiteDatabase);
        }

        displayQ();
    }

    private boolean qfInit(SQLiteDatabase sqLiteDatabase){

        try{
            qf = new QFeed(sqLiteDatabase);
            currQ = qf.next();

            return true;
        }catch (SQLException e){
            db.onUpgrade(sqLiteDatabase, 0,0);

            return false;
        }
    }

    private void updateQ(){
        if (dbSuccess){
            currQ = qf.next();
            displayQ();
        }
        else currQ = new Question(-1,null,null,null);

    }

    private void displayQ(){
        if(dbSuccess&&currQ.qstnID!=-1){
            q.setText(currQ.qstn);
            left.setText(currQ.ans1);
            right.setText(currQ.ans2);
        }else if (dbSuccess){
            q.setText("No Available Questions");
            left.setText("Refresh");
            right.setText("Refresh");
            qf.refresh();
        } else{
            q.setText("SQLITE EXCEPTION");
            left.setText("KILL");
            right.setText("ME");
        }
    }

    //----------BACK BUTTON WHEN DRAWER OPEN----------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //----------IGNORE ME----------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    //-----------SIDE MENU CLICKS----------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            //
        } else if (id == R.id.nav_notif) {

        } else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_RESULT);

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (id != R.id.switch_comments) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        xDelta = view.getX() - event.getRawX();
                        yDelta = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + xDelta)
                                .y(event.getRawY() + yDelta)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        };
    }

    //switch code to handle any return values from opening pages
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case LOGIN_RESULT:
                if (resultCode == RESULT_OK){
                    //username=(data.getStringExtra("username"));
                    //userID = data.getIntExtra("userID", -1);
                }else{
                    Snackbar login_fail = Snackbar.make(findViewById(R.id.drawer_layout), R.string.login_fail, Snackbar.LENGTH_INDEFINITE);
                    login_fail.setAction("LOGIN", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), LoginActivity.class);
                            startActivityForResult(intent, LOGIN_RESULT);
                        }
                    });
                    login_fail.show();
                }
                break;
            case ASK_RESULT:
                if (resultCode == RESULT_OK){
                    Snackbar ask = Snackbar.make(findViewById(R.id.drawer_layout), R.string.confirmation, Snackbar.LENGTH_SHORT );
                    ask.setAction("SHOW", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //go to question
                        }
                    });
                    ask.show();
                }else {
                    Snackbar ask_fail = Snackbar.make(findViewById(R.id.drawer_layout), R.string.discard, Snackbar.LENGTH_SHORT );
                    ask_fail.show();
                }
        }
    }
}
