package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.FriendAdapter;

public class AddPerm extends AppCompatActivity {
    private User user = CurrentUser.getInstance();
    private WishList current = CurrentWishList.getInstance();
    private boolean showed = false;
    private Dialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        popup = new Dialog(this);

        //menu show or not
        final de.hdodenhof.circleimageview.CircleImageView profile_picture = findViewById(R.id.picture_profile);
        if (user.getProfilePicture() != null) {
            profile_picture.setImageBitmap(user.getProfilePicture());
        }
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // parameters margins setter
                RelativeLayout menu = findViewById(R.id.menu);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout
                        .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (!showed) {
                    params.setMargins(0, 70, 0, 0);
                    showed = true;
                } else {
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
                user.logOut();
                Intent next_layout = new Intent(getApplicationContext(), LogInUp.class);
                startActivity(next_layout);
                finish();
            }
        });
        //**** Menu buttons END ****

        final GridView list_view = findViewById(R.id.list_view_t);

        //set page title
        TextView title = findViewById(R.id.page_title);
        title.setText(getString(R.string.add_perm));


        //on text change listener to search friend
        EditText text = findViewById(R.id.input_search);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, final int start, int before, int count) {
                if(count > 0) {
                    //make the list of all user with the name is like the pattern 's' + '*char'
                    final List<User> users = new ArrayList<>();
                    //check all user corresponding to the pattern
                    String req = "SELECT mail_host, mail_requested FROM Friend WHERE (mail_host "
                            + "LIKE \"" + s + "%\" AND relation=1 AND mail_requested=\""
                            + user.getEmail() + "\") OR (mail_host=\"" + user.getEmail() + "\" AND "
                            + "relation=1 AND mail_requested LIKE \"" + s + "%\");";

                    AccessDataBase db = new AccessDataBase(getApplicationContext());
                    Cursor cursor = db.select(req);
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()){
                        String temp_mail = cursor.getString(0);
                        boolean found = false;
                        //set the good mail in temp mail
                        if(temp_mail.equals(user.getEmail())){
                            temp_mail = cursor.getString(1);
                        }
                        //check if user is already in permission list;
                        for(int i = 0; i < current.getPermitted().size(); i++){
                            if(current.getPermitted().get(i).getEmail().equals(temp_mail)){
                                found = true;
                                break;
                            }
                        }

                        //if not already in permitted list add user to the list to display
                        if(!found) {
                            User toAdd = new User();
                            toAdd.setDb(getApplicationContext());
                            toAdd.setRefFromDb(temp_mail);
                            users.add(toAdd);
                        }
                        cursor.moveToNext();
                    }


                    //now we have the list we can build the adapter grid view with it
                    list_view.setAdapter(new FriendAdapter(getApplicationContext(), users));
                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // retrieve the user clicked on
                            final User currentUser = users.get(position);

                            // set popup args
                            popup.setContentView(R.layout.perm_popup);

                            //set good name
                            TextView name = popup.findViewById(R.id.name_popup);
                            name.setText(currentUser.getPseudo());

                            //set good mail
                            TextView mail = popup.findViewById(R.id.mail_popup);
                            mail.setText(currentUser.getEmail());

                            //set quit button listener
                            TextView quit = popup.findViewById(R.id.quit_btn);
                            quit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popup.dismiss();
                                }
                            });

                            //set button hide permission listener
                            TextView hide = popup.findViewById(R.id.read_btn);
                            hide.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.addPerm(currentUser.getEmail(), current.getId(), 0);
                                    current.updatePermitted();
                                    Toast.makeText(getApplicationContext(),
                                            current.getName() + " hided for "
                                                    + currentUser.getPseudo(),
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                    Intent refresh = new Intent(getApplicationContext(), Permission.class);
                                    startActivity(refresh);
                                    finish();

                                }
                            });

                            //set button write permission listener
                            TextView write = popup.findViewById(R.id.write_btn);
                            write.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.addPerm(currentUser.getEmail(), current.getId(), 1);
                                    current.updatePermitted();
                                    Toast.makeText(getApplicationContext(),
                                            currentUser.getPseudo() + " can now modify "
                                                    + current.getName(),
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                    Intent refresh = new Intent(getApplicationContext(), Permission.class);
                                    startActivity(refresh);
                                    finish();

                                }
                            });
                            popup.show();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
