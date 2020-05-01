package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
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
    boolean showed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        final TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        final GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);

        final User user = CurrentUser.getInstance();

        //grid set adapter if something in user.list<WishList>
        if(user.getWishlist_list().size() != 0){
            grid.setAdapter(new WishListAdapter(getApplicationContext(), user.getWishlist_list()));
        }

        //menu show or not
        final de.hdodenhof.circleimageview.CircleImageView profile_picture = findViewById(R.id.picture_profile);
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout menu = findViewById(R.id.menu);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                        .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (!showed){
                    params.setMargins(0, 70,0,0);
                    showed = true;
                }else {
                    params.setMarginStart(600);
                    showed = false;
                }
                menu.setLayoutParams(params);

            }
        });

        //**** Menu buttons START ****

        //button profile
        final Button profile_btn = findViewById(R.id.profile);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(next_layout);
                finish();
            }
        });

        //button wish list
        final Button wishlist_btn = findViewById(R.id.wishlist);
        wishlist_btn.setEnabled(false);

        //button friend list
        final Button friend_list_btn = findViewById(R.id.friend_list);
        friend_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), FriendsList.class);
                startActivity(next_layout);
                finish();
            }
        });

        //button parameters
        final Button param_btn = findViewById(R.id.param);
        param_btn.setEnabled(false);//TODO parameters*

        //button logout
        final Button logout_btn = findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.LogOut();
                Intent next_layout = new Intent(getApplicationContext(), LogInUp.class);
                startActivity(next_layout);
                finish();
            }
        });
        //**** Menu buttons END ****

        //button create wishlist under layout gridView
        final Button create_wishList = findViewById(R.id.create_wishlist);
        create_wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), NewWishList.class);
                startActivity(next_layout);
                finish();
            }
        });

        //Grid item on click listener
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

                //active wishlist button to back
                wishlist_btn.setEnabled(true);
                wishlist_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next_layout = new Intent(getApplicationContext(), Base.class);
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
            }
        });

        //TODO today
        //Friends request (accept or refuse) OK
        //TODO Wish Layout
        //TODO delete  & modify WishList
        //TODO delete & modify  Wish

        //TODO tomorrow
        //TODO modify profile and add more info in SignUP & preferences
        //TODO set picture profile
        //TODO set picture WishList
        //TODO set picture Wish

        //TODO in last
        //TODO manage permissions
        //TODO logs table for wish
        //TODO parameters
    }
}
