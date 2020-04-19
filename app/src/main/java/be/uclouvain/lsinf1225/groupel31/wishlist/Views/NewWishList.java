package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;

public class NewWishList extends AppCompatActivity {
    private Button create;
    private EditText name_in;
    private User user = User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish_list);
        name_in = findViewById(R.id.name_in);
        create = findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_in.getText().toString();
                user.createWishList(name, null);
                Intent next_layout = new Intent(getApplicationContext(), LayoutWishList.class);
                startActivity(next_layout);
                finish();
            }
        });
    }
}
