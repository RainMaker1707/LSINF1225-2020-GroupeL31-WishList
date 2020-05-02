package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.Wish;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.AccessDataBase;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;

public class SearchWish extends AppCompatActivity {
    private Dialog popup;
    private boolean showed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        final User user = CurrentUser.getInstance();
        popup = new Dialog(this);

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

        Button button = findViewById(R.id.button_new);
        button.setText("Add new Wish");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), NewWish.class);
                startActivity(next_layout);
                finish();
            }
        });

        final GridView list_view = findViewById(R.id.list_view_t);
        list_view.setNumColumns(2);

        EditText input = findViewById(R.id.input_search);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0) {
                    final List<Wish> wishes = new ArrayList<>();
                    AccessDataBase db = new AccessDataBase(getApplicationContext());
                    Cursor cursor = db.select("SELECT * FROM Wish WHERE name LIKE \"" + s + "%\";");
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()) {
                        Wish toAdd = new Wish(cursor.getInt(0),
                                cursor.getString(1),
                                null,
                                cursor.getString(4),
                                cursor.getDouble(5),
                                cursor.getString(6));
                        wishes.add(toAdd);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    list_view.setAdapter(new WishAdapter(getApplicationContext(), wishes));
                    list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            popup.setContentView(R.layout.add_wish_popup);

                            TextView quit = popup.findViewById(R.id.quit_popup);
                            quit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popup.dismiss();
                                }
                            });

                            TextView cancel = popup.findViewById(R.id.cancel_btn);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(getApplicationContext(), "Cancelled",
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                }
                            });
                            final Wish currentWish = (Wish) parent.getItemAtPosition(position);
                            TextView add = popup.findViewById(R.id.add_btn);
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CurrentWishList.getInstance().linkWish(currentWish.getId());
                                    popup.dismiss();
                                    Intent refresh = new Intent(getApplicationContext(), Base.class);
                                    startActivity(refresh);
                                    finish();
                                }
                            });

                            TextView name = popup.findViewById(R.id.name_popup);
                            name.setText(currentWish.getName());

                            TextView price = popup.findViewById(R.id.price_popup);
                            price.setText(String.format("Price : %.2f", currentWish.getPrice()));

                            TextView description = popup.findViewById(R.id.desc_popup);
                            description.setText(currentWish.getDescription());

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
