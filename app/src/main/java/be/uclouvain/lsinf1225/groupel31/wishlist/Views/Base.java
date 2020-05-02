package be.uclouvain.lsinf1225.groupel31.wishlist.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWish;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;

public class Base extends AppCompatActivity {
    // menu flag
    boolean showed = false;

    //Dialog-popup global access
    private Dialog popup;
    private Dialog conf_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        //set popups with context
        popup = new Dialog(this);
        conf_popup = new Dialog(this);

        //set the page title to "List of your WishList"
        final TextView title = findViewById(R.id.page_title);
        title.setText(R.string.list_wishlist);

        //retrieve griedView and set column number at 1
        final GridView grid = findViewById(R.id.news_grid);
        grid.setNumColumns(1);

        //retrieve the logged in user
        final User user = CurrentUser.getInstance();

        //grid set adapter if something in user.list<WishList>
        if(user.getWishlist_list().size() != 0){
            grid.setAdapter(new WishListAdapter(getApplicationContext(), this, user.getWishlist_list()));
        }

        //menu show or not
        final de.hdodenhof.circleimageview.CircleImageView profile_picture = findViewById(R.id.picture_profile);
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
                //retrieve current wishlist (which one was clicked)
                final WishList current = user.getWishlist_list().get(position);

                //add a second button in bottom of the layout (set background from transparent to simple button)
                Button edit_btn = findViewById(R.id.edit_btn);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout
                        .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                params.setMarginStart(10);
                edit_btn.setLayoutParams(params);
                edit_btn.setBackground(getDrawable(R.drawable.simple_button));
                edit_btn.setText(R.string.edit_btn);
                //new button listener show popup with wishlist editing option
                edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.setContentView(R.layout.wishlist_popup);
                        //set popup title to wishlist name
                        TextView name = popup.findViewById(R.id.name_popup);
                        name.setText(current.getName());

                        //TODO change picture

                        //save change button listener
                        TextView modify_btn = popup.findViewById(R.id.modify_btn);
                        modify_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get the input text
                                EditText input_t = popup.findViewById(R.id.name_in_popup);
                                String input = input_t.getText().toString();
                                //check if length > 2
                                if(input.length() < 2){
                                    Toast.makeText(getApplicationContext(),
                                            "New name need a length greater than 2",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //check if new name is equal than old
                                else if(!input.equals(current.getName())){
                                    current.changeName(input);
                                    Toast.makeText(getApplicationContext(),
                                            "Name successfully changed",
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                    Intent refresh = new Intent(getApplicationContext(), Base.class);
                                    startActivity(refresh);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "New name is same as old",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //delete button listener open a confirmation popup
                        TextView delete_btn = popup.findViewById(R.id.delete_btn);
                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // set confirmation popup content
                                conf_popup.setContentView(R.layout.delete_confirmation);
                                // delete button
                                TextView delete = conf_popup.findViewById(R.id.delete_btn);
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        user.deleteWishList(current.getId());
                                        Toast.makeText(getApplicationContext(),
                                                "Successfully deleted",
                                                Toast.LENGTH_SHORT).show();
                                        conf_popup.dismiss();
                                        popup.dismiss();
                                        //refresh the wishlist layout
                                        Intent refresh = new Intent(getApplicationContext(), Base.class);
                                        startActivity(refresh);
                                        finish();
                                    }
                                });
                                // cancel button
                                TextView not = conf_popup.findViewById(R.id.cancel_btn);
                                not.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(),
                                                "Cancelled",
                                                Toast.LENGTH_SHORT).show();
                                        conf_popup.dismiss();
                                    }
                                });
                                conf_popup.show();
                            }
                        });
                        // popup cross quit button top right
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
                // main wishs in a wishlist grid set page title
                title.setText(current.getName());
                // set grid column number to 2
                grid.setNumColumns(2);
                // change adapter to a smaller one with Wish content
                grid.setAdapter(new WishAdapter(getApplicationContext(), current.getWishLst()));
                //set the current WishList singleton instance
                CurrentWishList.setInstance(current);
                //change the listener from "create wishlist" to "add wish"
                create_wishList.setText(R.string.add_wish);
                create_wishList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //change the layout
                        Intent next_layout = new Intent(getApplicationContext(), SearchWish.class);
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
                //on click on a wish in grid
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CurrentWish.setInstance(current.getWishLst().get(position));
                        Intent next_layout = new Intent(getApplicationContext(), WishActivity.class);
                        next_layout.putExtra("isFriendWish", false);
                        startActivity(next_layout);
                        finish();
                    }
                });
            }
        });
    }
}
