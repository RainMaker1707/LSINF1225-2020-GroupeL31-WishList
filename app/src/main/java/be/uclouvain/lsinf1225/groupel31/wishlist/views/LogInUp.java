package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import be.uclouvain.lsinf1225.groupel31.wishlist.R;


public class LogInUp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_up);

        //Button sign in action
        Button sign_in = findViewById(R.id.sign_in_button);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), SignIn.class);
                startActivity(next_layout);
                finish();
            }
        });


        //Button sign up action
        Button sign_up = findViewById(R.id.sign_up_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), SignUp.class);
                startActivity(next_layout);
                finish();
            }
        });
    }
}
