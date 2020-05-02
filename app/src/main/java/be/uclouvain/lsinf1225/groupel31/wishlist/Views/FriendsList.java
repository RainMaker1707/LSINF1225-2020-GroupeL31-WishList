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
    private Dialog conf_popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        final User user = CurrentUser.getInstance();

        // Menu and Menu buttons
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
        // **** Menu buttons START ****

        // button profile
        final Button profile_btn = findViewById(R.id.profile);
        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(next_layout);
                finish();
            }
        });

        // button wish list
        final Button wishlist_btn = findViewById(R.id.wishlist);
        wishlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        // button friend list
        final Button friend_list_btn = findViewById(R.id.friend_list_btn);
        friend_list_btn.setEnabled(false);

        // button parameters
        final Button param_btn = findViewById(R.id.param);
        param_btn.setEnabled(false);//TODO parameters*

        // button logout
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
        // **** Menu buttons END ****

        // button to go on the layout with the search users tool
        Button add_friend = findViewById(R.id.add_friend);
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), AddFriend.class);
                startActivity(next_layout);
                finish();
            }
        });

        // feed the list view with the adapter
        ListView list = findViewById(R.id.friend_list);
        list.setAdapter(new FriendAdapter(getApplicationContext(), user.getFriendList()));
        // set the popup and the confirmation pop_up content with the context
        popup = new Dialog(this);
        conf_popup = new Dialog(this);
        // list item listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the user which one was clicked
                final User current = user.getFriendList().get(position);
                // set the popup view with the suitable layout
                popup.setContentView(R.layout.friend_popup);

                //TODO picture set

                // set the good line with the user's pseudo/name
                TextView name = popup.findViewById(R.id.friend_name_popup);
                name.setText(current.getPseudo());

                // set the good line with the user's email
                TextView mail = popup.findViewById(R.id.mail_friend_popup);
                mail.setText(current.getEmail());

                // set the number of wishlist in the string and the listener to display them
                TextView wishlist_nbr = popup.findViewById(R.id.wishlist_nbr_popup);
                wishlist_nbr.setText(String.format("Has %s WishList", current.getWishlist_list().size()));
                wishlist_nbr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent next_layout = new Intent(getApplicationContext(), FriendsWishList.class);
                        next_layout.putExtra("currentFriend", current.getEmail());
                        startActivity(next_layout);
                        finish();
                    }
                });

                // set delete friend button
                TextView delete = popup.findViewById(R.id.add_friend__popup);
                if(current.isFriend()) {
                    delete.setText(R.string.delete_friend);
                    delete.setBackgroundColor(getColor(R.color.Red));
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            conf_popup.setContentView(R.layout.delete_confirmation);
                            //get text view to replace wishlist confirmation by friend delete confirmation
                            TextView delete_txt = conf_popup.findViewById(R.id.txt_conf_popup);
                            delete_txt.setText(String.format(
                                    "Are you sure you want to delete %s",
                                    current.getPseudo()));

                            // set the yes button listener
                            TextView yes = conf_popup.findViewById(R.id.delete_btn);
                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.deleteFriend(current.getEmail());
                                    conf_popup.dismiss();
                                    popup.dismiss();
                                    Toast.makeText(getApplicationContext(), "Delete succesfully",
                                            Toast.LENGTH_SHORT).show();
                                    Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                                    startActivity(refresh);
                                    finish();
                                }
                            });

                            // set the cancel button listener
                            TextView no = conf_popup.findViewById(R.id.cancel_btn);
                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    conf_popup.dismiss();
                                }
                            });
                            conf_popup.show();
                        }
                    });
                // if the user is requested by the logged in user but not yet accepted
                }else if(!current.isFriend() && !current.isRequested()){
                    delete.setText(R.string.pending);
                    delete.setBackgroundColor(getColor(R.color.Gray));
                //if the logged in user is requested by another user
                }else if (!current.isFriend() && current.isRequested()){
                    // set the delete button to accept invite and resize it
                    delete.setText(R.string.accept);
                    delete.setWidth(150);
                    delete.setBackgroundColor(getColor(R.color.Green));
                    // reset button listener to accept invite
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.acceptRequest(current.getEmail());
                            popup.dismiss();
                            Toast.makeText(getApplicationContext(), "Accepted invite",
                                    Toast.LENGTH_SHORT).show();
                            //refresh the friend list layout
                            Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                            startActivity(refresh);
                            finish();
                        }
                    });

                    // set button refuse listener
                    TextView refuse = popup.findViewById(R.id.refuse_popup);
                    refuse.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user.refuseRequest(current.getEmail());
                            popup.dismiss();
                            Toast.makeText(getApplicationContext(), "Refused invite",
                                    Toast.LENGTH_SHORT).show();
                            //refresh the friend list layout
                            Intent refresh = new Intent(getApplicationContext(), FriendsList.class);
                            startActivity(refresh);
                            finish();

                        }
                    });
                }

                // Quit popup button in corner top right
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
