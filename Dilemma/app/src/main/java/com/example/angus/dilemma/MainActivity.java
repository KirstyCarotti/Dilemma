package com.example.angus.dilemma;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
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
import android.widget.LinearLayout;
import android.widget.CheckBox;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class
MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Stuff for shared preferences
    private SharedPreferences sharedpref;
    private SharedPreferences.Editor prefEdit;

    private CheckBox saveDetails;


    QFeed qf;
    Question currQ;
    TextView q,left,right;
    DBHandler db;
    boolean dbSuccess = false;
    public static final int LOGIN_RESULT = 1;
    public static final int ASK_RESULT = 2;
    int userID;
    String username = "";

    View bin = null;
    Boolean openBin = false;
    View card;
    View cardBot;
    ConstraintLayout cardSpace;
    LayoutInflater l;
    private float xDelta, yDelta;
    private float xCard, yCard;
    private float xDestination = 0f, yDestination = 0f;


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

        dbSuccess = qfInit(sqLiteDatabase);
        if (!dbSuccess){
            dbSuccess = qfInit(sqLiteDatabase);
        }

        l = getLayoutInflater();
        cardSpace = findViewById(R.id.cardSpace);

        cardInit();

    }

    private void cardInit(){
        card = newCard(currQ);
        updateQ();

        cardBot = newCard(currQ);
        orderCards();
        updateQ();

    }

    private View newCard(Question question){

        View card = l.inflate(R.layout.card,  cardSpace, false);

        q = card.findViewById(R.id.question);
        left = card.findViewById(R.id.left);
        right = card.findViewById(R.id.right);

        if(question.qstnID!=-1){
            q.setText(question.qstn);
            left.setText(question.ans1);
            right.setText(question.ans2);
        }else {
            q.setText("No Available Questions");
            left.setText("Refresh");
            right.setText("Refresh");
        }

        cardSpace.addView(card);

        card.setOnTouchListener(onTouchListener());
        return card;
    }

    private void nextCard(){
        if(openBin){
            //recast vote
            openBin= false;
        }else{
            cardSpace.removeView(bin);
            bin = card;
            card = cardBot;
            cardBot = newCard(currQ);
            orderCards();
            updateQ();
        }
    }

    private void orderCards(){
        card.bringToFront();
        if (bin != null)
        bin.bringToFront();
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
        }
        else currQ = new Question(-1,null,null,null);

    }

    //----------BACK BUTTON WHEN DRAWER OPEN----------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!openBin){
                resetDestination();
                animateCard(bin);
                openBin=true;
            }
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

    private void snap(View view){
        resetDestination();
        if(xCard > 300){
            xDestination = 4*xCard;
            //Toast.makeText(MainActivity.this,
                    //"RIGHT", Toast.LENGTH_SHORT)
                    //.show();

        } else if(xCard < -300){
            xDestination = (4*xCard);
           // Toast.makeText(MainActivity.this,
                    //"LEFT", Toast.LENGTH_SHORT)
                    //.show();
        }
        if (xDestination!=0){
            yDestination = (float)4*yCard;
            nextCard();
        }

        animateCard(view);


    }

    private void animateCard(View view){
        ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", xDestination);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(view, "translationY", yDestination);
        animationX.setDuration(500);
        animationY.setDuration(500);
        animationX.start();
        animationY.start();
        resetDestination();
    }


    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                xCard = view.getX()-40;
                yCard = view.getY()-40;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        xDelta = view.getX() - event.getRawX();
                        yDelta = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        snap(view);
                        Toast.makeText(MainActivity.this,
                                "x="+xCard+"  y="+yCard, Toast.LENGTH_SHORT)
                                .show();
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

    private void resetDestination(){
        xDestination = 0f;
        yDestination = 0f;
    }


    //switch code to handle any return values from opening pages
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case LOGIN_RESULT:
                if (resultCode == RESULT_OK){
                    username= data.getStringExtra("username");
                    userID = data.getIntExtra("userID", -1);
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
