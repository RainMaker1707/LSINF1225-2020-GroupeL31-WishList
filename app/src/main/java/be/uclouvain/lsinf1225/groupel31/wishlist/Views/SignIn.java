package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;

public class SignIn extends AppCompatActivity {
    private TextView sign_up;
    private Button sign_in;
    private EditText mail_in;
    private EditText pass_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.mail_in = findViewById(R.id.mail_in);
        this.pass_in = findViewById(R.id.pass_in);
        this.sign_up = findViewById(R.id.go_sign_up);
        this.sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), SignUp.class);
                startActivity(next_layout);
                finish();
            }
        });
        this.sign_in = findViewById(R.id.sign_in_button);
        this.sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mail_in.getText().toString();
                String password = pass_in.getText().toString();
                TextView error = findViewById(R.id.not_register_error);
                error.setText(null);
                error.setHeight(0);
                User user = User.getInstance();
                user.signIn(mail, password, getApplicationContext());
                Cursor select = user.ExistingUSer(mail);
                try {
                    select.moveToLast();
                    String pass_db = select.getString(1);
                    select.close();
                    if(password.equals(pass_db)){
                        Intent next_layout = new Intent(getApplicationContext(), Base.class);
                        startActivity(next_layout);
                        finish();
                    }else{
                        error.setText(R.string.pass_error);
                        error.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                        error.setHeight(50);
                    }
                }catch(CursorIndexOutOfBoundsException e){
                    error.setTextColor(getResources().getColor(R.color.Red, getTheme()));
                    error.setText(R.string.not_register_error);
                    error.setHeight(50);
                    select.close();
                }
            }
        });

    }
}
