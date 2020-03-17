package com.coo_project.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;

public class SignUp extends AppCompatActivity {

    private EditText mail_in;
    private EditText password;
    private EditText password_conf;
    private EditText pseudo_in;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView sign_in = findViewById(R.id.go_sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), SignIn.class);
                startActivity(next_layout);
                finish();
            }
        });

        Button sign_up = findViewById(R.id.sign_up_button);
        this.mail_in = findViewById(R.id.mail_in);
        this.pseudo_in = findViewById(R.id.pseudo_in);
        this.password = findViewById(R.id.pass_in);
        this.password_conf = findViewById(R.id.pass_in_conf);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mail_in.getText().toString();
                String pseudo = pseudo_in.getText().toString();
                String pass = password.getText().toString();
                String pass_conf = password_conf.getText().toString();
                String password = null;
                if(pass.equals(pass_conf)){
                    password = pass;
                    try{
                        System.out.println("--------CONNECTION TEST--------");
                        //TODO connection to data base

                    }catch(Exception e){
                        connection = null;
                        System.out.println("--------ERROR--------");
                    }
                }else{
                    //TODO error passwords didn't match
                }

                if (connection != null){
                    System.out.println("--------CONNECTION CHECK--------");

                    //TODO insert user in data base

                }else{
                    //TODO connection error
                }
            }
        });


    }
}
