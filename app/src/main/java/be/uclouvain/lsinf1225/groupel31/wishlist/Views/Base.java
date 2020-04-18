package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        List<WishList> wishlist_widgets = new ArrayList<>();
        for(int i = 0; i < 10; i++) wishlist_widgets.add(new WishList("TITLE example",
                null, i, "mail@gmail.com"));

        GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);
        grid.setAdapter(new WishListAdapter(getApplicationContext(), wishlist_widgets));
        //TODO fill grid View
        //TODO modify profile and add more info in SignUP
        //TODO set picture profile
    }
}
