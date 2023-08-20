package com.example.zombiegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText mailEt,passEt,nameEt;
    TextView dateTxt;
    Button Register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mailEt = findViewById(R.id.mailEt);
        passEt = findViewById(R.id.passEt);
        nameEt = findViewById(R.id.nameEt);
        dateTxt = findViewById(R.id.dateTxt);
        Register = findViewById(R.id.Register);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat formatul = new SimpleDateFormat("dd-MMM-yyyy");
        String StringFormat = formatul.format(date);
        dateTxt.setText(StringFormat);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mailEt.getText().toString();
                String password = passEt.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mailEt.setError("E-mail invalid");
                    mailEt.setFocusable(true);

                }else if (password.length()<5){
                    passEt.setError("Password must be at least 6 character long");
                    passEt.setFocusable(true);
                }else {
                    RegisterPlayer(email,password);
                }
            }
        });
    }

    private void RegisterPlayer(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            int count = 0;

                            assert user != null;
                            String uidString = user.getUid();
                            String mailString = mailEt.getText().toString();
                            String passString = passEt.getText().toString();
                            String nameString = nameEt.getText().toString();
                            String dateString = dateTxt.getText().toString();

                            HashMap<Object,Object> PlayerInfo = new HashMap<>();
                            PlayerInfo.put("Uid",uidString);
                            PlayerInfo.put("Email",mailString);
                            PlayerInfo.put("Password",passString);
                            PlayerInfo.put("Name",nameString);
                            PlayerInfo.put("Date",dateString);
                            PlayerInfo.put("Zombies",count);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Player Database");
                            reference.child(uidString).setValue(PlayerInfo);
                            startActivity(new Intent(Register.this,Menu.class));
                            Toast.makeText(Register.this,"Player created",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Register.this,"Error ocured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}