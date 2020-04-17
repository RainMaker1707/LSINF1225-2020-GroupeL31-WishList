package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;

import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.User;

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
                TextView uniqueError = findViewById(R.id.mail_error_text);
                uniqueError.setText(null);
                uniqueError.setHeight(0);
                TextView passError = findViewById(R.id.pass_error_text);
                passError.setText(null);
                TextView pseudoError = findViewById(R.id.pseudo_error);
                pseudoError.setHeight(0);
                pseudoError.setText(null);
                String mail = mail_in.getText().toString();
                String pseudo = pseudo_in.getText().toString();
                if (pseudo.length() >= 6) {
                    if (password.length() >= 8) {
                        if (password.getText().toString().equals(password_conf.getText().toString())) {
                            String pass = password.getText().toString();
                            User new_user = new User(mail, pass, getApplicationContext());
                            try {
                                new_user.signUp(mail, pseudo, pass, null, null);
                                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                                startActivity(next_layout);
                                finish();
                            } catch (SQLiteConstraintException e) {
                                uniqueError.setText(R.string.mail_exist);
                                uniqueError.setHeight(50);
                                uniqueError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                            }
                        } else {
                            passError.setText(R.string.pass_error_dont_match);
                            passError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                        }
                    } else {
                        passError.setText(R.string.pass_length_error);
                        passError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                    }
                }else{
                    pseudoError.setHeight(50);
                    pseudoError.setText(R.string.pseudo_error);
                    pseudoError.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                }
            }
        });


    }
}
