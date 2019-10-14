package com.fsc.cicerone.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {

    public static final int IMG_REQUEST = 1;
    private boolean imgSelected;
    private Bitmap bitmapImage;
    private static final String ERROR_TAG = "ERROR IN " + ImageManager.class.getName();
    private Activity activity;

    public ImageManager(Activity activity) {
        this.imgSelected = false;
        this.bitmapImage = null;
        this.activity = activity;
    }

    public boolean isSelected() {
        return this.imgSelected;
    }

    public void manageResult(int requestCode, int resultCode, Intent data, ImageView selectedImage) {
        if (requestCode == IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imgPath = data.getData();
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(this.activity.getContentResolver(), imgPath);
                selectedImage.setBackground(null);
                selectedImage.setImageBitmap(bitmapImage);
            } catch (IOException e) {
                Log.e(ERROR_TAG, e.toString());
            }
        }
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.activity.startActivityForResult(intent, IMG_REQUEST);
        imgSelected = true;
    }

    private String imgToString(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] imgByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByteArray, Base64.DEFAULT);
    }

    public void upload(@Nullable BooleanConnector.OnEndConnectionListener result) {
        Log.d(ImageManager.class.getName(), "Uploading image...");
        if (bitmapImage != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("image", imgToString(bitmapImage));
            new BooleanConnector.Builder(ConnectorConstants.IMAGE_UPLOADER)
                    .setObjectToSend(params)
                    .setOnEndConnectionListener(result)
                    .build()
                    .getData();
        }
    }
}
