package be.uclouvain.lsinf1225.groupel31.wishlist.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class ImageToBlob {

    // convert bitmap to byte[]
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert byte[] to bitmap
    public static Bitmap getBytePhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}

