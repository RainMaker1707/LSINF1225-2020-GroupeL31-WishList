package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import be.uclouvain.lsinf1225.groupel31.wishlist.R;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);
        TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);
        //TODO fill grid View
        //TODO modify profile and add more info in SignUP
        //TODO set picture profile
    }
}
