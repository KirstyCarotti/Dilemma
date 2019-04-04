package com.example.mydilemmaswipe;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ViewGroup mainLayout;
    private ImageView image;
    private Button leftAnswer, rightAnswer, reset;

    private float xDelta, yDelta;
    private float xCard, yCard;
    private float xDestination = 0f, yDestination = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        //comment section class code
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{"Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6"}));
        //

        mainLayout = (RelativeLayout) findViewById(R.id.main);
        image = (ImageView) findViewById(R.id.image);

        image.setOnTouchListener(onTouchListener());

        reset = (Button) findViewById(R.id.reset);  //reset card
        leftAnswer = (Button) findViewById(R.id.leftAnswer);
        rightAnswer = (Button) findViewById(R.id.rightAnswer);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animationX = ObjectAnimator.ofFloat(image, "translationX", 0f);
                ObjectAnimator animationY = ObjectAnimator.ofFloat(image, "translationY", 0f);
                animationX.setDuration(200);
                animationY.setDuration(200);
                animationX.start();
                animationY.start();
            }
        });

        leftAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDestination();
                ObjectAnimator animationX = ObjectAnimator.ofFloat(image, "translationX", -800f);
                ObjectAnimator animationY = ObjectAnimator.ofFloat(image, "translationY", yDestination);
                animationX.setDuration(200);
                animationY.setDuration(200);
                animationX.start();
                animationY.start();
            }
        });

        rightAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDestination();
                ObjectAnimator animationX = ObjectAnimator.ofFloat(image, "translationX", 800f);
                ObjectAnimator animationY = ObjectAnimator.ofFloat(image, "translationY", yDestination);
                animationX.setDuration(200);
                animationY.setDuration(200);
                animationX.start();
                animationY.start();
            }
        });
    }

    private void snap(View view){
        resetDestination();
        if(xCard > 300){
            xDestination = 2*xCard;
            Toast.makeText(MainActivity.this,
                    "RIGHT", Toast.LENGTH_SHORT)
                    .show();

        } else if(xCard < -300){
            xDestination = (2*xCard);
            Toast.makeText(MainActivity.this,
                    "LEFT", Toast.LENGTH_SHORT)
                    .show();
        }
        if (xDestination!=0){
            yDestination = (float)2*yCard;
        }

        ObjectAnimator animationX = ObjectAnimator.ofFloat(view, "translationX", xDestination);
        ObjectAnimator animationY = ObjectAnimator.ofFloat(view, "translationY", yDestination);
        animationX.setDuration(200);
        animationY.setDuration(200);
        animationX.start();
        animationY.start();
    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                xCard = view.getX()-110;
                yCard = view.getY()-336;

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
}
