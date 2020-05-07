package be.uclouvain.lsinf1225.groupel31.wishlist.views;

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
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.PermAdapter;

public class Permission extends AppCompatActivity {
    private User user = CurrentUser.getInstance();
    private boolean showed = false;
    private Dialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        popup = new Dialog(this);

        //menu show or not
        final de.hdodenhof.circleimageview.CircleImageView profile_picture = findViewById(R.id.picture_profile);
        if(user.getProfilePicture() != null){profile_picture.setImageBitmap(user.getProfilePicture());}
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // parameters margins setter
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

        // set title text
        TextView page_title = findViewById(R.id.title_fl);
        page_title.setText(String.format("%s permissions manager",
                CurrentWishList.getInstance().getName()));

        //make list of permitted user


        // set list view adapter
        ListView list = findViewById(R.id.friend_list);
        if(CurrentWishList.getInstance().getPermitted().size() > 0){
            list.setAdapter(new PermAdapter(getApplicationContext(),
                    CurrentWishList.getInstance().getPermitted()));}
        // list Item listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final User currentUser = (User) parent.getAdapter().getItem(position);
                popup.setContentView(R.layout.perm_popup);

                //delete perm button
                TextView delete = popup.findViewById(R.id.write_btn);
                delete.setText(getString(R.string.del_perm));
                delete.setBackgroundColor(getColor(R.color.Red_alpha));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO confirmation popup
                        user.removePerm(currentUser.getEmail(), CurrentWishList.getInstance().getId());
                        CurrentWishList.getInstance().updatePermitted();
                        Toast.makeText(getApplicationContext(), "Successfully deleted",
                                Toast.LENGTH_SHORT).show();
                        popup.dismiss();
                    }
                });

                //quit button
                TextView quit = popup.findViewById(R.id.quit_btn);
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                //permute perm button
                TextView permute = popup.findViewById(R.id.read_btn);
                if(currentUser.getPerm() == 0){permute.setText(getString(R.string.add_write_perm));}
                else{permute.setText(getString(R.string.add_read_perm));}
                permute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentUser.getPerm() == 0){
                            user.updatePerm(currentUser.getEmail(), 1,
                                    CurrentWishList.getInstance().getId());
                            CurrentWishList.getInstance().updatePermitted();
                            Toast.makeText(getApplicationContext(),
                                    currentUser.getPseudo() + " can now write in "
                                            + CurrentWishList.getInstance().getName(),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            user.updatePerm(currentUser.getEmail(), 0,
                                    CurrentWishList.getInstance().getId());
                            CurrentWishList.getInstance().updatePermitted();
                            Toast.makeText(getApplicationContext(),
                                    CurrentWishList.getInstance().getName()
                                            + " is now hided for " + currentUser.getPseudo(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        popup.dismiss();
                        Intent refresh = new Intent(getApplicationContext(), Permission.class);
                        startActivity(refresh);
                        finish();
                    }
                });
                popup.show();
            }
        });

        // set button text and listener
        Button add_perm = findViewById(R.id.add_friend);
        add_perm.setText(getString(R.string.add_perm2));
        add_perm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), AddPerm.class);
                startActivity(next_layout);
                finish();
            }
        });


    }
}
