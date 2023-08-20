package com.example.zombiegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference PLAYERS;

    Button CreateSesion, PlayBtn, ScoresBtn;
    TextView MyScore, Zombies, uid, mail, name, Menutxt, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        PLAYERS = firebaseDatabase.getReference("Player Database");

        CreateSesion = findViewById(R.id.CreateSesion);
        ScoresBtn = findViewById(R.id.ScoresBtn);
        PlayBtn = findViewById(R.id.PlayBtn);

        MyScore = findViewById(R.id.MyScore);
        uid = findViewById(R.id.uid);
        mail = findViewById(R.id.mail);
        name = findViewById(R.id.name);
        Menutxt = findViewById(R.id.Menutxt);
        date = findViewById(R.id.date);
        Zombies = findViewById(R.id.Zombies);

        PlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "Play", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Menu.this,GamePlay.class);

                String UidS = uid.getText().toString();
                String NameS = name.getText().toString();
                String ZombieS = Zombies.getText().toString();

                intent.putExtra("UID",UidS);
                intent.putExtra("NAME",NameS);
                intent.putExtra("ZOMBIE",ZombieS);

                startActivity(intent);
                Toast.makeText(Menu.this, "Sent Parameters", Toast.LENGTH_SHORT).show();
            }
        });

        ScoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,Scores.class);
                startActivity(intent);
                Toast.makeText(Menu.this, "Scores", Toast.LENGTH_SHORT).show();
            }
        });


        CreateSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseSesion();
            }
        });
    }

    protected void onStart() {
        UserLoggedIn();
        super.onStart();
    }

    private void  UserLoggedIn(){
        if (user != null){
            QuerryDB();
            Toast.makeText(this, "Player online", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }
    }

    private void CloseSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
    }

    private void QuerryDB(){
        Query  query = PLAYERS.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    String zombiesString = ""+ds.child("Zombies").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    String nameString = ""+ds.child("Name").getValue();
                    String dateString = ""+ds.child("Date").getValue();

                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    mail.setText(emailString);
                    name.setText(nameString);
                    date.setText(dateString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}