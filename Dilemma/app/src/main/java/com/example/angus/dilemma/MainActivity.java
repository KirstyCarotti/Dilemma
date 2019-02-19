package com.example.angus.dilemma;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    QFeed qf;
    Question currQ;
    Button left, right;
    String buttonText;
    TextView q;
    DBHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //---------STARTUP STUFF---------

        db = new DBHandler(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //---------ASK QUESTION BUTTON----------

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AskActivity.class));
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

        //-----------UPDATES QUESTION AND ANSWER FIELDS BELOW-----------

        qf = new QFeed();
        currQ = qf.next();

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

        q.setText(currQ.qstn);
        left.setText(currQ.ans1);
        right.setText(currQ.ans2);

    }

    private void updateQ(){
        currQ = qf.next();
        q.setText(currQ.qstn);
        left.setText(currQ.ans1);
        right.setText(currQ.ans2);
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

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (id != R.id.switch_comments) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }



}
