package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import be.uclouvain.lsinf1225.groupel31.wishlist.BuildConfig;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.WishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWish;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishAdapter;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.WishListAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class Base extends AppCompatActivity {
    // menu flag
    boolean showed = false;

    //Dialog-popup global access
    private Dialog popup;
    private Dialog conf_popup;
    private Dialog picture_popup;
    private Bitmap img;
    private String photoPath = null;

    private static final int IMAGE_CAPTURED = 2;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        //set popups with context
        popup = new Dialog(this);
        conf_popup = new Dialog(this);
        picture_popup = new Dialog(this);
        context = this;

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

                        final CircleImageView picture = popup.findViewById(R.id.wishlist_picture_popup);
                        // set picture
                        if(current.getPicture() != null){
                            picture.setImageBitmap(current.getPicture());
                        }

                        Button change_picture = popup.findViewById(R.id.change_pct_btn);
                        change_picture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                picture_popup.setContentView(R.layout.picture_popup);

                                if(current.getPicture() != null){
                                    CircleImageView picture = picture_popup.findViewById(R.id.picture_popup);
                                    picture.setImageBitmap(current.getPicture());
                                }else {
                                    CircleImageView picture = picture_popup.findViewById(R.id.picture_popup);
                                    picture.setImageDrawable(getDrawable(R.drawable.picture_gift));
                                }

                                // quit button
                                TextView quit = picture_popup.findViewById(R.id.quit_popup);
                                quit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        picture_popup.dismiss();
                                    }
                                });

                                // cancel button
                                TextView cancel = picture_popup.findViewById(R.id.cancel_btn);
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(), "Cancelled",
                                                Toast.LENGTH_SHORT).show();
                                        picture_popup.dismiss();
                                    }
                                });

                                // album button
                                ImageView album = picture_popup.findViewById(R.id.saved_picture);
                                album.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED){
                                            requestPermissions(new String[] {
                                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                            }, 2);
                                        }else {
                                            Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                            startActivityForResult(pick, 1);
                                        }
                                    }
                                });

                                // camera button
                                ImageView camera = picture_popup.findViewById(R.id.take_picture);
                                camera.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                                                Manifest.permission.CAMERA)
                                                != PackageManager.PERMISSION_GRANTED){
                                            requestPermissions(new String[] {
                                                    Manifest.permission.CAMERA
                                            }, 2);
                                        }else {
                                            Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                            if (capture.resolveActivity(getPackageManager()) != null) {
                                                File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                File photoFile = null;
                                                try {
                                                    photoFile = File.createTempFile("temp", ".jpg", photoDir);
                                                    photoPath = photoFile.getAbsolutePath();
                                                    Uri photoUri = FileProvider.getUriForFile(context,
                                                            BuildConfig.APPLICATION_ID + ".provider",
                                                            photoFile);
                                                    capture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                                    startActivityForResult(capture, IMAGE_CAPTURED);
                                                } catch (IOException e) {
                                                    e.getMessage();
                                                }
                                            }
                                        }
                                    }
                                });

                                //save button
                                TextView save = picture_popup.findViewById(R.id.valid_btn);
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        picture.setImageBitmap(img);
                                        current.updatePicture(img);
                                        Toast.makeText(getApplicationContext(), "Picture changed",
                                                Toast.LENGTH_SHORT).show();
                                        picture_popup.dismiss();
                                    }
                                });


                                picture_popup.show();
                            }
                        });

                        // button manage permission
                        Button manage = popup.findViewById(R.id.permission_btn);
                        manage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popup.dismiss();
                                Intent next_layout = new Intent(getApplicationContext(), Permission.class);
                                startActivity(next_layout);
                                finish();
                            }
                        });

                        //save change button listener
                        TextView modify_btn = popup.findViewById(R.id.modify_btn);
                        modify_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // get the input text
                                EditText input_t = popup.findViewById(R.id.name_in_popup);
                                String input = input_t.getText().toString();
                                if(input.length() == 0){
                                    Toast.makeText(getApplicationContext(), "Name not changed",
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                }
                                //check if length > 2
                                else if(input.length() < 2){
                                    Toast.makeText(getApplicationContext(),
                                            "New name need a length greater than 2",
                                            Toast.LENGTH_SHORT).show();
                                }
                                //check if new name is equal than old
                                else if(!input.equals(current.getName())){
                                    current.changeName(input);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully changed",
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            // get image data path file
            Uri selected = data.getData();
            img = ImageToBlob.getBytePhoto(ImageToBlob.getBytes(selected, this));
            CircleImageView photo = picture_popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }else if (requestCode==IMAGE_CAPTURED && resultCode==RESULT_OK){
            img = BitmapFactory.decodeFile(photoPath);
            CircleImageView photo = picture_popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }
    }
}
