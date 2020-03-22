package be.uclouvain.lsinf1225.groupel31.wishlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {
    private TextView sign_up;
    private Button sign_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
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
                String pseudo = ((EditText)findViewById(R.id.pseudo_in)).getText().toString();
                String password = ((EditText)findViewById(R.id.pass_in)).getText().toString();
                //TODO connection with data base verification
            }
        });

    }
}
