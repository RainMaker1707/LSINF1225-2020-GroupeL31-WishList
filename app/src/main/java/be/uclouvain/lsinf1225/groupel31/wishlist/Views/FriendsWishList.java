package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWish;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class FriendsWishList extends AppCompatActivity {

    private boolean showed = false;
    private User currentFriend = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        final User user = CurrentUser.getInstance();
        currentFriend.setDb(getApplicationContext());
        currentFriend.setRefFromDb(getIntent().getStringExtra("currentFriend"));
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
        TextView title = findViewById(R.id.page_title);
        title.setText(String.format("%s 's WishLists", currentFriend.getPseudo()));

        //button create wishlist under layout gridView
        final Button create_wishList = findViewById(R.id.create_wishlist);
        create_wishList.setEnabled(false);

        //grid view
        final GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);
        grid.setAdapter(new WishListAdapter(getApplicationContext(), currentFriend.getWishlist_list()));

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WishList current = currentFriend.getWishlist_list().get(position);
                grid.setNumColumns(2);
                grid.setAdapter(new WishAdapter(getApplicationContext(), current.getWishLst()));
                CurrentWishList.setInstance(current);
                create_wishList.setText(R.string.add_wish);
                create_wishList.setEnabled(false);
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
                        next_layout.putExtra("isFriendWish", true);
                        startActivity(next_layout);
                        finish();
                    }
                });
            }
        });
    }
}
