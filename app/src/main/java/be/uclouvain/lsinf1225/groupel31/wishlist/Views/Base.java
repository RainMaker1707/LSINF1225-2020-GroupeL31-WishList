package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class LayoutWishList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        final TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        final GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);

        final User user = User.getInstance();

        if(user.getWishlist_list().size() != 0){
            grid.setAdapter(new WishListAdapter(getApplicationContext(), user.getWishlist_list()));
        }

        final Button create_wishList = findViewById(R.id.create_wishlist);
        create_wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), NewWishList.class);
                startActivity(next_layout);
                finish();
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WishList current = user.getWishlist_list().get(position);
                title.setText(current.getName());
                grid.setNumColumns(3);
                grid.setAdapter(new WishAdapter(getApplicationContext(), current.getWishLst()));
                create_wishList.setText(R.string.add_wish);
                create_wishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next_layout = new Intent(getApplicationContext(), NewWish.class);
                        startActivity(next_layout);
                        finish();
                    }
                });
            }
        });
        //TODO creation wish *
        //TODO delete  & modify WishList
        //TODO delete & modify  Wish
        //TODO modify profile and add more info in SignUP
        //TODO set picture profile
        //TODO set picture WishList
        //TODO set picture Wish
        //TODO Friends layout, request, add, delete
    }
}
