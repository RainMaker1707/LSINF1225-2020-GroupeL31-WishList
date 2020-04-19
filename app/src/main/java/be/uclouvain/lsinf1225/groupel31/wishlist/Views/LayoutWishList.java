package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class LayoutWishList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);

        User user = User.getInstance();

        if(user.getWishlist_list().size() != 0){
            grid.setAdapter(new WishListAdapter(getApplicationContext(), user.getWishlist_list()));
        }

        Button create_wishList = findViewById(R.id.create_wishlist);
        create_wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), NewWishList.class);
                startActivity(next_layout);
                finish();
            }
        });
        //TODO creation wishlist
        //TODO modify profile and add more info in SignUP
        //TODO set picture profile
    }
}
