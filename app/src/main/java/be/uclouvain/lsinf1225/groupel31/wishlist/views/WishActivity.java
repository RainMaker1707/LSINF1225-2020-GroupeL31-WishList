package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWish;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import de.hdodenhof.circleimageview.CircleImageView;

public class WishActivity extends AppCompatActivity {

    private Dialog popup;
    private boolean showed = false;
    private User user = CurrentUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        final boolean isFriendWish = getIntent().getBooleanExtra("isFriendWish", false);
        final boolean canWrite = getIntent().getBooleanExtra("canWrite", false);
        popup = new Dialog(this);

        //Circle profile picture action -> go to profile activity
        de.hdodenhof.circleimageview.CircleImageView profile_picture = findViewById(R.id.picture_profile);
        if(user.getProfilePicture() != null){profile_picture.setImageBitmap(user.getProfilePicture());}
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
                user.LogOut();
                Intent next_layout = new Intent(getApplicationContext(), LogInUp.class);
                startActivity(next_layout);
                finish();
            }
        });
        //**** Menu buttons END ****

        Wish currentWish = CurrentWish.getInstance();

        //Set title page to wish name
        TextView name = findViewById(R.id.page_title);
        name.setText(currentWish.getName());

        //Set price
        TextView price = findViewById(R.id.price);
        price.setText(String.format("%s €", currentWish.getPrice()));

        //Set market if one is saved
        TextView market = findViewById(R.id.market);
        if(currentWish.getMarket().length() > 1){ market.setText(currentWish.getMarket());}
        else{market.setText(R.string.no_market);}

        //Set description if one is saved
        TextView description = findViewById(R.id.description);
        if(currentWish.getDescription().length() > 1){ description.setText(currentWish.getDescription());}
        else{description.setText(R.string.no_desc);}

        //button, delete
        Button modify = findViewById(R.id.delete_btn);
        if (isFriendWish && !canWrite){ modify.setEnabled(false);}
        else{
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.setContentView(R.layout.delete_confirmation);

                    //set Text
                    TextView text = popup.findViewById(R.id.txt_conf_popup);
                    text.setText(getString(R.string.conf_del_wish));

                    // set valid button listener
                    TextView accept = popup.findViewById(R.id.delete_btn);
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CurrentWishList.getInstance().unlinkWish(CurrentWish.getInstance().getId());
                            Toast.makeText(getApplicationContext(), "Successfully unlink",
                                    Toast.LENGTH_SHORT).show();
                            popup.dismiss();
                            Intent next_layout = new Intent(getApplicationContext(), Base.class);
                            startActivity(next_layout);
                            finish();
                        }
                    });

                    //set cancel button listener
                    TextView cancel = popup.findViewById(R.id.cancel_btn);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popup.dismiss();
                        }
                    });

                    popup.show();

                }
            });
        }


        //set picture
        CircleImageView photo = findViewById(R.id.pict_wish);
        if(currentWish.getPicture() != null){photo.setImageBitmap(currentWish.getPicture());}

    }
}
