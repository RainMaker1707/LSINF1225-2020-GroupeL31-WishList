package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;

public class SignUp extends AppCompatActivity {

    private EditText mail_in;
    private EditText password;
    private EditText password_conf;
    private EditText pseudo_in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Text with action "sign in instead" -> go to sign in activity
        TextView sign_in = findViewById(R.id.go_sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), SignIn.class);
                startActivity(next_layout);
                finish();
            }
        });

        //get Views
        Button sign_up = findViewById(R.id.sign_up_button);
        this.mail_in = findViewById(R.id.mail_in);
        this.pseudo_in = findViewById(R.id.pseudo_in);
        this.password = findViewById(R.id.pass_in);
        this.password_conf = findViewById(R.id.pass_in_conf);

        //When click on sign up button
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set error text to null and size 0
                TextView uniqueError = findViewById(R.id.mail_error_text);
                uniqueError.setText(null);
                uniqueError.setHeight(0);
                TextView passError = findViewById(R.id.pass_error_text);
                passError.setText(null);
                TextView pseudoError = findViewById(R.id.pseudo_error);
                pseudoError.setHeight(0);
                pseudoError.setText(null);

                //get EditText inputs
                String mail = mail_in.getText().toString();
                String pseudo = pseudo_in.getText().toString();

                //check input data len (4 cond because different "else" to
                // set good error with good text at the right place)
                if (pseudo.length() >= 6) {
                    if (password.length() >= 8) {
                        if (password.getText().toString().equals(password_conf.getText().toString())) {
                            //create new empty user
                            User new_user = new User();
                            new_user.setDb(getApplicationContext());
                            if(!new_user.ExistingUSer(mail)){ // check unique email
                                //insert in db
                                new_user.signUp(mail, pseudo, password.getText().toString(), null, null);
                                //set all user's attributes with db ref and set singleton CurrentUser on this user
                                new_user.signIn(mail);
                                //go to next layout->more info activity
                                Toast.makeText(getApplicationContext(), "Log Up successfully as "
                                        + new_user.getPseudo(), Toast.LENGTH_SHORT).show();
                                Intent next_layout = new Intent(getApplicationContext(),
                                        SignUpMoreInfo.class);
                                startActivity(next_layout);
                                finish();
                            }else{
                                //Error in unique email registry
                                uniqueError.setText(R.string.mail_exist);
                                uniqueError.setHeight(50);
                                uniqueError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                            }
                        } else {
                            //Error password don't match
                            passError.setText(R.string.pass_error_dont_match);
                            passError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                        }
                    } else {
                        //Error on len password
                        passError.setText(R.string.pass_length_error);
                        passError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                    }
                }else{
                    //error on len pseudo
                    pseudoError.setHeight(50);
                    pseudoError.setText(R.string.pseudo_error);
                    pseudoError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                }
            }
        });


    }
}
