package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.FriendAdapter;

public class FriendsList extends AppCompatActivity {
    boolean showed = false;
    private Dialog popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        final User user = CurrentUser.getInstance();

        //Menu and Menu buttons
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
        wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        //button friend list
        final Button friend_list_btn = findViewById(R.id.friend_list_btn);
        friend_list_btn.setEnabled(false);

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

        Button add_friend = findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), AddFriend.class);
                startActivity(next_layout);
                finish();
            }
        });

        ListView list = findViewById(R.id.friend_list);
        list.setAdapter(new FriendAdapter(getApplicationContext(), user.getFriendList()));

        popup = new Dialog(this);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User current = user.getFriendList().get(position);
                popup.setContentView(R.layout.friend_popup);

                //TODO picture set

                TextView name = popup.findViewById(R.id.friend_name_popup);
                name.setText(current.getPseudo());

                TextView mail = popup.findViewById(R.id.mail_friend_popup);
                mail.setText(current.getEmail());

                TextView wishlist_nbr = popup.findViewById(R.id.wishlist_nbr_popup);
                wishlist_nbr.setText(String.format("Has %s WishList", current.getWishlist_list().size()));
                TextView delete = popup.findViewById(R.id.add_friend__popup);
                if(current.isFriend()) {
                    delete.setText(R.string.delete_friend);
                    delete.setBackgroundColor(getColor(R.color.Red));
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.deleteFriend(current.getEmail());
                            popup.dismiss();
                            Toast.makeText(getApplicationContext(), "Delete succesfully",
                                    Toast.LENGTH_SHORT).show();
                            Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                            startActivity(refresh);
                            finish();
                        }
                    });
                }else if(!current.isFriend() && !current.isRequested()){
                    delete.setText(R.string.pending);
                    delete.setBackgroundColor(getColor(R.color.Gray));
                }else if (!current.isFriend() && current.isRequested()){
                    delete.setText(R.string.accept);
                    delete.setWidth(150);
                    delete.setBackgroundColor(getColor(R.color.Green));
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.acceptRequest(current.getEmail());
                            popup.dismiss();
                            Toast.makeText(getApplicationContext(), "Accepted invite",
                                    Toast.LENGTH_SHORT).show();
                            Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                            startActivity(refresh);
                            finish();
                        }
                    });

                    TextView refuse = popup.findViewById(R.id.refuse_popup);
                    refuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.refuseRequest(current.getEmail());
                            popup.dismiss();
                            Toast.makeText(getApplicationContext(), "Refused invite",
                                    Toast.LENGTH_SHORT).show();
                            Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                            startActivity(refresh);
                            finish();

                        }
                    });
                }

                TextView quit = popup.findViewById(R.id.quit_popup);
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                popup.show();

            }
        });

    }
}
