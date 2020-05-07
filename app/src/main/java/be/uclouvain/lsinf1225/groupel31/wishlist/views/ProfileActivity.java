package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.UserAdapter;

public class ProfileActivity extends AppCompatActivity {
    boolean showed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final User user = CurrentUser.getInstance();

        // Set profile picture
        ImageView picture = findViewById(R.id.picture_profile);
        if(user.getProfilePicture() != null){ picture.setImageBitmap(user.getProfilePicture());}

        // ListView adapter set
        ListView list = findViewById(R.id.list_view);
        list.setAdapter(new UserAdapter(getApplicationContext(), CurrentUser.getInstance()));

        // set modify profile button listener
        Button modify = findViewById(R.id.modify_profile);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), ModifyProfile.class);
                startActivity(next_layout);
                finish();
            }
        });

        // Menu and menu buttons
        Button menu = findViewById(R.id.menu_btn);
        menu.setOnClickListener(new View.OnClickListener() {
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
        profile_btn.setEnabled(false);

        //button wish list
        final Button wishlist_btn = findViewById(R.id.wishlist);
        wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

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
                user.logOut();
                Intent next_layout = new Intent(getApplicationContext(), LogInUp.class);
                startActivity(next_layout);
                finish();
            }
        });
        //**** Menu buttons END ****
    }
}
