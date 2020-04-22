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
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWish;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class Base extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        final TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        final GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);

        final User user = CurrentUser.getInstance();

        if(user.getWishlist_list().size() != 0){
            grid.setAdapter(new WishListAdapter(getApplicationContext(), user.getWishlist_list()));
        }

        final Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.LogOut();
                Intent next_layout = new Intent(getApplicationContext(), LogInUp.class);
                startActivity(next_layout);
                finish();
            }
        });

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
                final WishList current = user.getWishlist_list().get(position);
                title.setText(current.getName());
                grid.setNumColumns(2);
                grid.setAdapter(new WishAdapter(getApplicationContext(), current.getWishLst()));
                CurrentWishList.setInstance(current);
                create_wishList.setText(R.string.add_wish);
                create_wishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next_layout = new Intent(getApplicationContext(), NewWish.class);
                        startActivity(next_layout);
                        finish();
                    }
                });

                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CurrentWish.setInstance(current.getWishLst().get(position));
                        Intent next_layout = new Intent(getApplicationContext(), WishActivity.class);
                        startActivity(next_layout);
                        finish();
                    }
                });

                back.setText(R.string.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next_layout = new Intent(getApplicationContext(), Base.class);
                        startActivity(next_layout);
                        finish();
                    }
                });
            }
        });
        //TODO Wish Layout
        //TODO delete  & modify WishList
        //TODO delete & modify  Wish
        //TODO modify profile and add more info in SignUP & preferences
        //TODO set picture profile
        //TODO set picture WishList
        //TODO set picture Wish
        //TODO manage permissions
        //TODO logs table for wish
        //TODO Friends layout, request, add, delete
    }
}
