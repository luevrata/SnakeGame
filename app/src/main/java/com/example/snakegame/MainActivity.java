package com.example.snakegame;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import android.graphics.Color;


public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //list of snake points (length)
    private final List<SnakePoints> snakePointsList = new ArrayList<>();


    private SurfaceView surfaceView;
    private TextView scoreTv;

    //surface holder to draw snake on surface's canvas
    private SurfaceHolder surfaceHolder;

    //snake moving position vales, by default moves right
    private String movingPosition = "right";

    private int score = 0;

    //snake/point size, change this value to make bigger snake size
    private static final int  pointSize = 28;

    private static final  int defaultTotalPoints = 3;

    private static final int snakeColor = Color.YELLOW;

    //speed 1-1000
    private static final int snakeMovingImgSpeed = 800;

    //random point position coordinates on canvas
    private int positionX, positionY;

    //timer to move snake/ change position after specific time
    private Timer timer;

    //canvas to draw snake and show on surfaceview
    private Canvas canvas = null;


    //point color of snake
    private Paint pointColor = null;

    private Paint snakeColorPaint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting surface view and score text view from xml file
        surfaceView=findViewById(R.id.surfaceView);
        scoreTv = findViewById(R.id.scoreTV);

        //getting image buttons from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        //adding callback to surface view
        surfaceView.getHolder().addCallback(this);

        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("bottom")) {
                    movingPosition = "top";
                }

            }
        });

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("right")) {
                    movingPosition = "left";
                }
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("left")) {
                    movingPosition = "right";
                }

            }
        });

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movingPosition.equals("top")) {
                    movingPosition = "bottom";
                }

            }
        });

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //when surface is created get surface holder from it and assign it to surfaceHolder
        this.surfaceHolder = surfaceHolder;

        //init data for snake and surface view
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init() {
        //clear snake points (length)
        snakePointsList.clear();

        canvas = surfaceHolder.lockCanvas();
        Paint back =new Paint();
        back.setColor(Color.GREEN);
        back.setStyle(Paint.Style.FILL);
        back.setAntiAlias(true);//smooth
        Rect r = new Rect(0,0,canvas.getWidth(), canvas.getHeight());
        canvas.drawRect(r, back);
        surfaceHolder.unlockCanvasAndPost(canvas);

        //cet default score to zero
        scoreTv.setText("0");

        score = 0;

        movingPosition = "right";

        //default snake start pos at the screen
        int startPositionX =(pointSize)* defaultTotalPoints;

        //make snake default length point
        for (int i =0; i<defaultTotalPoints; i++) {
            //add points to snake
            SnakePoints snakePoints = new SnakePoints(startPositionX,pointSize);
            snakePointsList.add(snakePoints);

            //inc value for next point as snake's tale
            startPositionX =startPositionX - (pointSize*2);

        }
        //add random point on canvas
        addPoint();

        //start moving snake
        moveSnake();
    }

    //add random point on canvas
    private void addPoint(){

        int surfaceWidth = surfaceView.getWidth() - (pointSize*2);
        int surfaceHeight = surfaceView.getWidth() - (pointSize*2);

        int randomXPosition = new Random().nextInt(surfaceWidth/pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight/pointSize);

        //check if random pos is even or odd cuz we need even

        if (randomXPosition%2 !=0) {
            randomXPosition++;
        }

        if (randomYPosition%2 !=0) {
            randomYPosition++;
        }

        positionX = (pointSize*randomXPosition)+pointSize;
        positionY = (pointSize*randomYPosition)+pointSize;

    }

    private void moveSnake() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // getting position
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                //check if snake have eaten the point
                if (headPositionX == positionX && positionY == headPositionY) {
                    //grow snake after point is eaten
                    growSnake();

                    //add another random point
                    addPoint();
                }

                //check where snake is moving
                switch(movingPosition) {
                    case "right": {
                        //move snake's head to right
                        //other points follow snake's head
                        snakePointsList.get(0).setPositionX(headPositionX+(pointSize*2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                    } break;
                    case "left": {
                        //move snake's head to right
                        //other points follow snake's head
                        snakePointsList.get(0).setPositionX(headPositionX-(pointSize*2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                    } break;
                    case "top": {
                        //move snake's head to right
                        //other points follow snake's head
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY-(pointSize*2));
                    } break;
                    case "bottom": {
                        //move snake's head to right
                        //other points follow snake's head
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY+(pointSize*2));
                    } break;

                }

                //check if game is over ( snake touches the edges or itself)
                if ( checkGameOver(headPositionX, headPositionY)) {

                    //stop timer and moving
                    timer.purge();
                    timer.cancel();

                    //game over dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your Score = "+ score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //restart
                            init();
                        }
                    });

                    //timer runs on background so need to show dialog on main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();

                        }
                    });
                } else {
                    //lock canvas on surface holder to draw on it
                    canvas = surfaceHolder.lockCanvas();


                    //clear canvas with white color
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    //back color
                    Paint back =new Paint();
                    Color c = new Color();
                    String colorString;
                    back.setColor(Color.parseColor("#FF3F9C17"));
                    back.setStyle(Paint.Style.FILL);
                    back.setAntiAlias(true);//smooth
                    Rect r = new Rect(0,0,canvas.getWidth(), canvas.getHeight());
                    canvas.drawRect(r, back);

                    //move snake's head position
                    //other points follow snake's head
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createSnakeColor());

                    //draw random point to be eaten
                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    // other points follow snake's head
                    for (int i =1; i<snakePointsList.size(); i++) {
                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        //move points across head
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createSnakeColor());

                        //change head position
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }

                    //unlock canvas to draw on surface view
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - snakeMovingImgSpeed, 1000-snakeMovingImgSpeed);
    }

    private void growSnake() {
        //create snake point
        SnakePoints snakePoints = new SnakePoints(0,0);

        // add the to tale
        snakePointsList.add(snakePoints);

        // inc score
        score++;

        //set score to textview
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTv.setText(String.valueOf(score));
            }
        });
    }

    private boolean checkGameOver(int headPositionX, int headPositionY) {
        boolean gameOver = false;

        //check edges
        if (snakePointsList.get(0).getPositionX()<0 ||
        snakePointsList.get(0).getPositionY() <0 ||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight() ) {
            gameOver = true;
        } else {
            //check intersections

            for ( int i = 1; i< snakePointsList.size(); i++) {
                //hardest buggggg && ||
                if ( headPositionX == snakePointsList.get(i).getPositionX()&&
                        headPositionY == snakePointsList.get(i).getPositionY()) {
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }

    //check if color is defined before
    private Paint createPointColor() {
        if (pointColor==null) {
            pointColor=new Paint();
            pointColor.setColor(Color.RED);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);//smooth
        }
        return pointColor;
    }

    private Paint createSnakeColor() {
        if (snakeColorPaint==null) {
            snakeColorPaint=new Paint();
            snakeColorPaint.setColor(snakeColor);
            snakeColorPaint.setStyle(Paint.Style.FILL);
            snakeColorPaint.setAntiAlias(true);//smooth
        }
        return snakeColorPaint;
    }

}