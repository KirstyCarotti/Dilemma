package com.example.angus.dilemma;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
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
import android.util.Log;
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
    SQLiteDatabase sqLiteDatabase;
    View top, bot;

    View bin = null;
    Boolean openBin = false;
    View card;
    View cardBot;
    ConstraintLayout cardSpace;
    LayoutInflater l;
    private float xDelta, yDelta;
    private float xCard, yCard;
    private float xDestination = 0f, yDestination = 0f;
    LinearLayout res_lin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = new DBHandler(this);
        sqLiteDatabase = db.getWritableDatabase();
        //db.onUpgrade(sqLiteDatabase,0,0);
        //db.createDummyQuestions(sqLiteDatabase);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabInit(); // fab = ask question button
        drawInit();// draw = side menu and top bar

        if (true/*logged in */) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_RESULT);
        }

        dbSuccess = qfInit(sqLiteDatabase);
        if (!dbSuccess) dbSuccess = qfInit(sqLiteDatabase);

        cardInit(true); // spawns cards
        res_lin = findViewById(R.id.results_main);
    }

    private void fabInit() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AskActivity.class);
                intent.putExtra("userID", userID);
                startActivityForResult(intent, ASK_RESULT);
            }
        });
    }

    private void drawInit() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void cardInit(boolean onStart) {
        if (onStart){
            l = getLayoutInflater();
            cardSpace = findViewById(R.id.cardSpace);
        }else {
            cardSpace.removeView(cardBot);
            cardSpace.removeView(card);
            cardSpace.removeView(bin);
        }

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

        if (question.qstnID!=-1) {
            q.setText(question.qstn);
            left.setText(question.ans1);
            right.setText(question.ans2);
            card.setTag(R.id.question_id, question.qstnID);
        } else {
            q.setText("No Available Questions");
            left.setText("Refresh");
            right.setText("Refresh");
            card.setTag(R.id.question_id, -1);
        }

        card.setTag(R.id.voted_state, 0);
        cardSpace.addView(card);
        card.setOnTouchListener(onTouchListener());
        return card;
    }

    private void updateQ(){
        if (dbSuccess){
            currQ = qf.next();
        }
        else currQ = new Question(-1,null,null,null);

    }

    private void orderCards(){
        card.bringToFront();
        card.setElevation(8);
        if (bin != null){
            bin.bringToFront();
            bin.setElevation(12);
        }
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

    private boolean qfInit(SQLiteDatabase sqLiteDatabase){

        try{
            qf = new QFeed(sqLiteDatabase,userID);
            currQ = qf.next();

            return true;
        }catch (SQLException e){
            //db.onUpgrade(sqLiteDatabase, 0,0);

            return false;
        }
    }

    private void getResults(View v, String vote){

        String results = "SELECT _AnswerID, AnswerText, NoOfClicks FROM Answer WHERE _QuestionID = ?";
        Cursor cursor = sqLiteDatabase.rawQuery(results, new String[] {v.getTag(R.id.question_id)+ ""});

        cursor.moveToFirst();
        Integer clicksLeft = cursor.getInt(2);
        String left_text = cursor.getString(1);

        cursor.moveToNext();
        Integer clicksRight = cursor.getInt(2);
        String right_text = cursor.getString(1);

        float total = clicksLeft + clicksRight;
        String leftPercent = (int)((clicksLeft / total)*100)+"%";
        String rightPercent = (int)((clicksRight / total)*100)+"%";

        View card = l.inflate(R.layout.result_card, res_lin, false);

        TextView question = card.findViewById(R.id.result_question);
        TextView left = card.findViewById(R.id.result_left);
        TextView right = card.findViewById(R.id.result_right);
        TextView left_p = card.findViewById(R.id.left_percent);
        TextView right_p = card.findViewById(R.id.right_percent);

        Cursor c = sqLiteDatabase.rawQuery("SELECT Question FROM Question WHERE _QuestionID = ?",
                new String[] {v.getTag(R.id.question_id)+""});
        c.moveToFirst();
        Log.d("cursor", c.getString(0));

        question.setText(c.getString(0));

        left.setText(left_text);
        right.setText(right_text);
        left_p.setText(leftPercent);
        right_p.setText(rightPercent);

        int total_click = clicksRight + clicksLeft;

        if (total_click!=0){

            LinearLayout.LayoutParams leftparams = (LinearLayout.LayoutParams) left.getLayoutParams();
            float left_weight = (float) clicksLeft / (float) total_click;
            leftparams.weight = left_weight;
            Log.d("leftWeight", left_weight+"");
            left.setLayoutParams(leftparams);

            LinearLayout.LayoutParams rightparams = (LinearLayout.LayoutParams) right.getLayoutParams();
            float right_weight = (float) clicksRight / (float) total_click;
            rightparams.weight = right_weight;
            Log.d("rightWeight", right_weight+"");
            right.setLayoutParams(rightparams);
        }

        if( vote == "left"){
            left.setTypeface(null, Typeface.BOLD);
            left_p.setTypeface(null, Typeface.BOLD);
        }
        else {
            right.setTypeface(null, Typeface.BOLD);
            right_p.setTypeface(null, Typeface.BOLD);
        }

        if(top==null){
            top = card;
            res_lin.addView(card);
        }else {
            if(bot!=null) res_lin.removeView(bot);
            res_lin.addView(card);
            top.bringToFront();
            bot = top;
            top = card;
        }
    }

    private void voteLeft(View v){
        if(!v.getTag(R.id.question_id).equals(-1)) {
            Log.d("msg","left vote: " +v.getTag(R.id.question_id).toString());
            String vote = "INSERT INTO AnsQue(_UserID,_QuestionID,AnswerType) VALUES (" + userID + "," + v.getTag(R.id.question_id) + ",0)";
            String click = "UPDATE Answer SET NoOfClicks = NoOfClicks + 1 WHERE _QuestionID = " + v.getTag(R.id.question_id) + " AND _AnswerID % 2 = 1";
            sqLiteDatabase.execSQL(click);
            sqLiteDatabase.execSQL(vote);
            getResults(v, "left");

        }
    }

    private void voteRight(View v){
        if(!v.getTag(R.id.question_id).equals(-1)) {
            Log.d("msg", "right vote: " + v.getTag(R.id.question_id).toString());
            String vote = "INSERT INTO AnsQue(_UserID,_QuestionID,AnswerType) VALUES (" + userID + ", " + v.getTag(R.id.question_id) + ", 1)";
            String click = "UPDATE Answer SET NoOfClicks = NoOfClicks + 1 WHERE _QuestionID = " + v.getTag(R.id.question_id) + " AND _AnswerID % 2 = 0";
            sqLiteDatabase.execSQL(click);
            sqLiteDatabase.execSQL(vote);
            getResults(v, "right");
        }
    }

    private void voteSkip(View v){
        if(v.getTag()!=null) {
            String vote = "INSERT INTO AnsQue(_UserID,_QuestionID,AnswerType) VALUES (" + userID + "," + v.getTag() + ",2)";
            sqLiteDatabase.execSQL(vote);
        }
    }

    //----------BACK BUTTON WHEN DRAWER OPEN----------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);

        } else if (id == R.id.nav_notif) {

        } else if (id == R.id.logout) {
            userID = -1;
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
            voteRight(view);
        }
        else if(xCard < -300){
            xDestination = (4*xCard);
           // Toast.makeText(MainActivity.this,
                    //"LEFT", Toast.LENGTH_SHORT)
                    //.show();
            voteLeft(view);
        }
        if (xDestination!=0){
            yDestination = (float)4*yCard;
            view.setTag(R.id.voted_state, 1);
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

                if (view.getTag(R.id.voted_state).equals(1)  || view.equals(cardBot))  return false;

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        xDelta = view.getX() - event.getRawX();
                        yDelta = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:
                        snap(view);
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
                    sqLiteDatabase = db.getWritableDatabase();
                    qfInit(sqLiteDatabase);
                    cardInit(false);
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
