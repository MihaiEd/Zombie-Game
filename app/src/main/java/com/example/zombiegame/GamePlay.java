package com.example.zombiegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class GamePlay extends AppCompatActivity {

    String UIDS, NAMES, ZOMBIES;
    TextView TvCounter, TvName, TvTmp;
    ImageView IvZombie;

    TextView WidthTv, HeightTv;

    int ScreenWidth, ScreenHeight;
    Random random;

    boolean GameOver = false;
    Dialog dialog;

    int counter = 0;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference PLAYERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TvCounter = findViewById(R.id.TvCounter);
        TvName = findViewById(R.id.TvName);
        TvTmp = findViewById(R.id.TvTmp);
        IvZombie = findViewById(R.id.IvZombie);

        Bundle intent = getIntent().getExtras();

        dialog = new Dialog(GamePlay.this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        PLAYERS = database.getReference("Player Database");

        UIDS = intent.getString("UID");
        NAMES = intent.getString("NAME");
        ZOMBIES = intent.getString("ZOMBIE");

        WidthTv = findViewById(R.id.WidthTv);
        HeightTv = findViewById(R.id.HeightTv);

        TvCounter.setText(ZOMBIES);
        TvName.setText(NAMES);

        ScreenRes();
        CountDown();

        IvZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GameOver) {
                    counter++;
                    TvCounter.setText(String.valueOf(counter));
                    IvZombie.setImageResource(R.drawable.zombieshoot);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            IvZombie.setImageResource(R.drawable.zombie);
                            Move();
                        }
                    }, 500);
                }
            }
        });
    }

    private void ScreenRes(){
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        ScreenWidth = point.x;
        ScreenHeight = point.y;

        String WIDTH = String.valueOf(ScreenWidth);
        String HEIGHT = String.valueOf(ScreenHeight);

        WidthTv.setText(WIDTH);
        HeightTv.setText(HEIGHT);

        random = new Random();
    }

    private void Move(){
        int min = 0;
        int maxX = ScreenWidth - IvZombie.getWidth();
        int maxY = ScreenHeight - IvZombie.getHeight();
        int randomX = random.nextInt(((maxX - min)+1)+min);
        int randomY = random.nextInt(((maxY - min)+1)+min);

        IvZombie.setX(randomX);
        IvZombie.setY(randomY);
    }

    private void CountDown(){
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsReaming = millisUntilFinished/1000;
                TvTmp.setText(secondsReaming+"S");
            }

            public void onFinish() {
                TvTmp.setText("0S");
                GameOver = true;
                MesageGameOver();
                SaveResult("Zombies",counter);
            }
        }.start();
    }

    private void MesageGameOver() {
        TextView ItsOver, Killed, Number;
        Button PlayAgain, ReturnMenu, Scores;

        dialog.setContentView(R.layout.gameover);

        ItsOver = dialog.findViewById(R.id.ItsOver);
        Killed = dialog.findViewById(R.id.Killed);
        Number = dialog.findViewById(R.id.Number);

        PlayAgain = dialog.findViewById(R.id.PlayAgain);
        ReturnMenu = dialog.findViewById(R.id.ReturnMenu);
        Scores = dialog.findViewById(R.id.Scores);

        String zombies = String.valueOf(counter);
        Number.setText(zombies);

        PlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                dialog.dismiss();
                TvCounter.setText("0");
                GameOver = false;
                CountDown();
                Move();
            }
        });

        ReturnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamePlay.this, Menu.class));
            }
        });

        Scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GamePlay.this,Scores.class));
                //Toast.makeText(GamePlay.this, "Scores", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
        dialog.setCancelable(false);
    }

    private void SaveResult(String key, int zombies){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key,zombies);
        PLAYERS.child(user.getUid()).updateChildren(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(GamePlay.this, "Scores has been updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}