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
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewWishList extends AppCompatActivity {

    private User user = CurrentUser.getInstance();
    private boolean showed = false;
    private EditText name_in;
    private Dialog popup;
    private Bitmap img;
    private String photoPath;
    private Context context;
    private static final int IMAGE_CAPTURED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish_list);

        popup = new Dialog(this);
        context = this;

        CircleImageView photo = findViewById(R.id.picture_wishlist);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setContentView(R.layout.picture_popup);

                CircleImageView picture = popup.findViewById(R.id.picture_popup);
                picture.setImageDrawable(getDrawable(R.drawable.picture_gift));

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
                            File photoFile;
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
                        CircleImageView new_pict = findViewById(R.id.picture_wishlist);
                        new_pict.setImageBitmap(img);
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        });

        //Create wishlist button action
        name_in = findViewById(R.id.name_in);
        Button create = findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get editText input
                String name = name_in.getText().toString();
                //insert in db
                user.createWishList(name);
                WishList created = user.getWishlist_list().get(user.getWishlist_list().size() - 1);
                created.updatePicture(img);
                //go to next layout -> base activity
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        // menu showed or not
        CircleImageView profile_picture = findViewById(R.id.picture_profile);
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

        // Title with action back -> go to base activity
        TextView title = findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
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
