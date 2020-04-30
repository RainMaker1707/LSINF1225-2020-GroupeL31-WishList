package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.FriendAdapter;

public class AddFriend extends AppCompatActivity {
    private boolean showed = false;
    private Activity activity;
    private Dialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        this.activity = this;
        popup = new Dialog(activity);

        final User user = CurrentUser.getInstance();

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

        final ListView list_view = findViewById(R.id.list_view);


        EditText text = findViewById(R.id.input_search);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0) {
                    final List <User> users = new ArrayList<>();
                    AccessDataBase db = new AccessDataBase(getApplicationContext());
                    Cursor cursor = db.select("SELECT pseudo, mail FROM User WHERE pseudo LIKE \""
                            + s + "%\" OR mail LIKE \"" + s + "%\";");
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        if(!cursor.getString(1).equals(user.getEmail())) {
                            User toAdd = new User();
                            toAdd.setDb(getApplicationContext());
                            toAdd.setRefFromDb(cursor.getString(1));
                            users.add(toAdd);

                        }
                        cursor.moveToNext();
                    }
                    cursor.close();

                    list_view.setAdapter(new FriendAdapter(getApplicationContext(), users));
                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final User current = users.get(position);

                            // set popup args
                            popup.setContentView(R.layout.friend_popup);

                            // set friend name
                            TextView name = popup.findViewById(R.id.friend_name_popup);
                            name.setText(current.getPseudo());

                            // set friend mail
                            TextView mail = popup.findViewById(R.id.mail_friend_popup);
                            mail.setText(current.getEmail());

                            // set friend wishlist nbr
                            TextView nbr = popup.findViewById(R.id.wishlist_nbr_popup);
                            nbr.setText("Has " + current.getWishlist_list().size() + " WishList");

                            //TODO set pictures

                            //TODO button add
                            Button add = popup.findViewById(R.id.add_friend__popup);
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AccessDataBase db = new AccessDataBase(getApplicationContext());
                                    db.insert("INSERT INTO Friend (mail_host, relation, mail_requested)"
                                            + "VALUES (\""+ user.getEmail() + "\", 1, \""
                                            + current.getEmail() + "\");");
                                    Toast.makeText(getApplicationContext(), "Correctly added "
                                            + current.getPseudo(),
                                            Toast.LENGTH_SHORT).show();

                                }
                            });

                            // listener for quit button
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
