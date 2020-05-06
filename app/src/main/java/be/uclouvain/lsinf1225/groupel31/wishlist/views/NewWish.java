package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentWishList;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;
import de.hdodenhof.circleimageview.CircleImageView;


public class NewWish extends AppCompatActivity {
    private Dialog popup;
    private Bitmap img = null;
    private Bitmap imgChoose = null;
    boolean showed = false;
    private String photoPath;
    private static final int IMAGE_CAPTURED = 2;
    private User user = CurrentUser.getInstance();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);
        final WishList current_list = CurrentWishList.getInstance();
        final EditText name_in = findViewById(R.id.name_in);
        final EditText price_in = findViewById(R.id.price_in);
        final EditText market_in = findViewById(R.id.market_in);
        final EditText description_in = findViewById(R.id.description_in);
        popup = new Dialog(this);
        context = this;
        //Button add wish action
        Button button = findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get editText inputs
                String name = name_in.getText().toString();
                double price = Double.parseDouble(price_in.getText().toString());
                String market = market_in.getText().toString();
                String description = description_in.getText().toString();

                //insert in db
                current_list.createWish(name, imgChoose, description, price, market);

                //go to next layout -> go to precedent activity
                boolean isFriend = getIntent().getBooleanExtra("isFriend", false);
                if(!isFriend){
                    Intent next_layout = new Intent(getApplicationContext(), Base.class);
                    startActivity(next_layout);
                }else{
                    Intent next_layout = new Intent(getApplicationContext(), FriendsWishList.class);
                    next_layout.putExtra("currentFriend",
                            getIntent().getStringExtra("currentFriend"));
                }
                finish();
            }
        });

        // Profile picture listener
        final CircleImageView picture = findViewById(R.id.picture_wishlist);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setContentView(R.layout.picture_popup);

                // quit button
                TextView quit = popup.findViewById(R.id.quit_popup);
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                // cancel button
                TextView cancel = popup.findViewById(R.id.cancel_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancelled",
                                Toast.LENGTH_SHORT).show();
                        popup.dismiss();
                    }
                });

                // album button
                ImageView album = popup.findViewById(R.id.saved_picture);
                album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pick, 1);
                    }
                });

                // camera button
                ImageView camera = popup.findViewById(R.id.take_picture);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if(capture.resolveActivity(getPackageManager()) != null){
                            File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            File photoFile = null;
                            try {
                                photoFile = File.createTempFile("temp", ".jpg", photoDir);
                                photoPath = photoFile.getAbsolutePath();
                                Uri photoUri = FileProvider.getUriForFile(context,
                                        BuildConfig.APPLICATION_ID +".provider",
                                        photoFile);
                                capture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(capture, IMAGE_CAPTURED);
                            }catch(IOException e){
                                e.getMessage();
                            }
                        }
                    }
                });

                // save button
                TextView save = popup.findViewById(R.id.valid_btn);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgChoose = img;
                        if(imgChoose != null) {picture.setImageBitmap(imgChoose);}
                        popup.dismiss();
                    }
                });

                popup.show();
            }
        });

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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            // get image data path file
            Uri selected = data.getData();
            img = ImageToBlob.getBytePhoto(ImageToBlob.getBytes(selected, this));
            CircleImageView photo = popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }else if (requestCode==IMAGE_CAPTURED && resultCode==RESULT_OK){
            img = BitmapFactory.decodeFile(photoPath);
            CircleImageView photo = popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }
    }
}
