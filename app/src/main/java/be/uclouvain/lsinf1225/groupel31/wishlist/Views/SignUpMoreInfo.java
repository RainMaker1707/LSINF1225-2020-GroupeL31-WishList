package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;

public class SignUpMoreInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_more_info);

        // get current user
        final User user = CurrentUser.getInstance();

        // pass button listener
        TextView pass = findViewById(R.id.pass_btn);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        // save button listener
        Button save = findViewById(R.id.save_info_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the inputs
                EditText address_in = findViewById(R.id.address_input);
                EditText color_in = findViewById(R.id.fav_color_input);
                EditText meal_in = findViewById(R.id.fav_meal_input);
                EditText sport_in = findViewById(R.id.sport_input);
                EditText hobby_in = findViewById(R.id.hobby_input);

                // init String to retrieve input
                String address = address_in.getText().toString();
                String color = color_in.getText().toString();
                String meal = meal_in.getText().toString();
                String sport = sport_in.getText().toString();
                String hobby = hobby_in.getText().toString();

                //TODO picture

                //insert info in db
                user.addMoreInfo(address, color, meal, sport, hobby);

                //make toast
                Toast.makeText(getApplicationContext(), "Correctly updated",
                        Toast.LENGTH_SHORT).show();

                //go to base layout
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

    }
}
