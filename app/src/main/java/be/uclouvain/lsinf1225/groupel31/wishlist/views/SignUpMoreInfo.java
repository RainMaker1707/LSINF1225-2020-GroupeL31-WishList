package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpMoreInfo extends AppCompatActivity {

    private Dialog popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_more_info);
        popup = new Dialog(this);
        // get current user
        final User user = CurrentUser.getInstance();

        // pass button listener
        TextView pass = findViewById(R.id.pass_btn);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        // save button listener
        Button save = findViewById(R.id.save_info_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the inputs
                EditText address_in = findViewById(R.id.address_input);
                EditText color_in = findViewById(R.id.fav_color_input);
                EditText meal_in = findViewById(R.id.fav_meal_input);
                EditText sport_in = findViewById(R.id.sport_input);
                EditText hobby_in = findViewById(R.id.hobby_input);

                // init String to retrieve input
                String address = address_in.getText().toString();
                String color = color_in.getText().toString();
                String meal = meal_in.getText().toString();
                String sport = sport_in.getText().toString();
                String hobby = hobby_in.getText().toString();

                //TODO picture

                //insert info in db
                user.addMoreInfo(address, color, meal, sport, hobby);

                //make toast
                Toast.makeText(getApplicationContext(), "Correctly updated",
                        Toast.LENGTH_SHORT).show();

                //go to base layout
                Intent next_layout = new Intent(getApplicationContext(), Base.class);
                startActivity(next_layout);
                finish();
            }
        });

        // Profile picture listener
        final CircleImageView profile_picture = findViewById(R.id.picture_profile);
        profile_picture.setOnClickListener(new View.OnClickListener() {
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

                        CircleImageView photo = popup.findViewById(R.id.picture_popup);
                        photo.setImageBitmap(user.getProfilePicture());
                    }
                });

                popup.show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            // get image data path file
            Uri selected = data.getData();
            Bitmap image = ImageToBlob.getBytePhoto(ImageToBlob.getBytes(selected, this));
            CurrentUser.getInstance().updateProfilePicture(image);
        }
    }
}
