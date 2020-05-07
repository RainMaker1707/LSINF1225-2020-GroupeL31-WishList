package be.uclouvain.lsinf1225.groupel31.wishlist.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

import be.uclouvain.lsinf1225.groupel31.wishlist.BuildConfig;
import be.uclouvain.lsinf1225.groupel31.wishlist.Classes.User;
import be.uclouvain.lsinf1225.groupel31.wishlist.R;
import be.uclouvain.lsinf1225.groupel31.wishlist.singleton.CurrentUser;
import be.uclouvain.lsinf1225.groupel31.wishlist.tools.ImageToBlob;
import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyProfile extends AppCompatActivity {
    private User user = CurrentUser.getInstance();
    private Dialog popup;
    private Bitmap img;
    private String photoPath;
    private boolean flag = false;
    private final static int IMAGE_CAPTURED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        popup = new Dialog(this);

        // set image if set in db
        final CircleImageView photo = findViewById(R.id.picture_profile);
        if(user.getProfilePicture() != null){ photo.setImageBitmap(user.getProfilePicture());}
        //set image listener
        photo.setOnClickListener(new View.OnClickListener() {
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
                ImageView camera = popup.findViewById(R.id.take_picture);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[] {
                                    Manifest.permission.CAMERA
                            }, 2);
                        }else{
                            Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if(capture.resolveActivity(getPackageManager()) != null){
                                File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                File photoFile;
                                try {
                                    photoFile = File.createTempFile("temp", ".jpg", photoDir);
                                    photoPath = photoFile.getAbsolutePath();
                                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),
                                            BuildConfig.APPLICATION_ID +".provider",
                                            photoFile);
                                    capture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                    startActivityForResult(capture, IMAGE_CAPTURED);
                                }catch(IOException e){
                                    e.getMessage();
                                }
                            }
                        }
                    }
                });

                // save button
                TextView save = popup.findViewById(R.id.valid_btn);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CurrentUser.getInstance().updateProfilePicture(img);
                        flag = true;
                        photo.setImageBitmap(user.getProfilePicture());
                        popup.dismiss();
                    }
                });
                popup.show();
            }
        });

        //set change password text listener
        TextView pass_change = findViewById(R.id.change_password);
        pass_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setContentView(R.layout.change_password_popup);

                //quit button
                TextView quit = popup.findViewById(R.id.quit_btn);
                quit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                //cancel button
                TextView cancel = popup.findViewById(R.id.cancel_btn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Cancelled",
                                Toast.LENGTH_SHORT).show();
                        popup.dismiss();
                    }
                });

                //valid button
                TextView valid = popup.findViewById(R.id.valid_btn);
                valid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get the input references
                        EditText pass_in = popup.findViewById(R.id.old_pass);
                        EditText new_pass = popup.findViewById(R.id.new_pass);
                        EditText conf_pass = popup.findViewById(R.id.pass_conf);

                        //check old password match
                        if(user.getPassword().equals(pass_in.getText().toString())){
                            //check if the two new pass are same
                            if(new_pass.getText().toString().equals(conf_pass.getText().toString())){
                                String pass = new_pass.getText().toString();
                                //check pass length
                                if(pass.length() >= 8) {
                                    //update password in user
                                    user.updatePassword(pass);
                                    flag = true;
                                    Toast.makeText(getApplicationContext(),
                                            "Password updated",
                                            Toast.LENGTH_SHORT).show();
                                    popup.dismiss();
                                }else{
                                    Toast.makeText(getApplicationContext(),
                                            "New password must have at least 8 characters",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "The new passwords don't matches",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Old password don't match with yours",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                popup.show();
            }
        });

        //set save change listener
        final Button save = findViewById(R.id.save_info_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the input references
                EditText address = findViewById(R.id.address_input);
                EditText name = findViewById(R.id.name_input);
                EditText color = findViewById(R.id.fav_color_input);
                EditText sport = findViewById(R.id.sport_input);
                EditText meal = findViewById(R.id.fav_meal_input);
                EditText hobby = findViewById(R.id.hobby_input);

                //set address if input something
                if(address.getText().toString().length() != 0){
                    if(address.getText().toString().length() > 5){
                        user.updateAddress(address.getText().toString());
                        flag = true;
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Address must have length equal or grater than 6 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //set name if input something
                if(name.getText().toString().length() != 0) {
                    if (name.getText().toString().length() > 5) {
                        user.updatePseudo(name.getText().toString());
                        flag = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Name must have length equal or grater than 6 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //set color if input something
                if(color.getText().toString().length() != 0) {
                    if (color.getText().toString().length() > 2) {
                        user.updateColor(color.getText().toString());
                        flag = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Color must have length equal or grater than 3 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //set sport if input something
                if(sport.getText().toString().length() != 0) {
                    if (sport.getText().toString().length() > 3) {
                        user.updateSport(sport.getText().toString());
                        flag = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Sport must have length equal or grater than 4 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //set meal if input something
                if(meal.getText().toString().length() != 0) {
                    if (meal.getText().toString().length() > 5) {
                        user.updateMeal(meal.getText().toString());
                        flag = true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Meal must have length equal or grater than 6 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //set hobby if input something
                if(hobby.getText().toString().length() != 0) {
                    if (16 > hobby.getText().toString().length() &&
                            hobby.getText().toString().length() > 5) {
                        user.updateHobby(hobby.getText().toString());
                        flag = true;
                    } else if (!flag) {
                        Toast.makeText(getApplicationContext(),
                                "Hobby must have length equal or grater than 6 and " +
                                        "less than 16 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //if something has changed
                if(flag){
                    Toast.makeText(getApplicationContext(),
                            "Info successfully changed",
                            Toast.LENGTH_SHORT).show();
                    Intent next_layout = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(next_layout);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Change something or click cancel button",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //set cancel button
        TextView cancel = findViewById(R.id.pass_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next_layout = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(next_layout);
                finish();
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
            CircleImageView photo = popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }else if (requestCode==IMAGE_CAPTURED && resultCode==RESULT_OK){
            img = BitmapFactory.decodeFile(photoPath);
            CircleImageView photo = popup.findViewById(R.id.picture_popup);
            photo.setImageBitmap(img);
        }
    }
}
