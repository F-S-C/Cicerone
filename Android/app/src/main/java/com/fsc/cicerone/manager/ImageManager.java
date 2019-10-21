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

/**
 * A <i>control</i> class that manages the images.
 */
public class ImageManager {

    public static final int IMG_REQUEST = 1;
    private boolean imgSelected;
    private Bitmap bitmapImage;
    private static final String ERROR_TAG = "ERROR IN " + ImageManager.class.getName();
    private Activity activity;

    /**
     * ImageManager's constructor.
     *
     * @param activity The activity.
     */
    public ImageManager(Activity activity) {
        this.imgSelected = false;
        this.bitmapImage = null;
        this.activity = activity;
    }

    /**
     * Return a boolean that informs if the image has been selected or not.
     *
     * @return Boolean value true if selected, false otherwise.
     */
    public boolean isSelected() {
        return this.imgSelected;
    }

    /**
     * Manage the subsequent activity returns.
     *
     * @param requestCode   The activity request code.
     * @param resultCode    The result code specified by the second activity.
     * @param data          An Intent that carries the result data.
     * @param selectedImage The ImageView where the selected image will set.
     */
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

    /**
     * Start a second activity to allow the user to select an image.
     */
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.activity.startActivityForResult(intent, IMG_REQUEST);
        imgSelected = true;
    }

    /**
     * Convert an image to a string hash.
     *
     * @param image The image to be converted.
     * @return The image string hash.
     */
    private String imgToString(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] imgByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByteArray, Base64.DEFAULT);
    }

    /**
     * Send a request to the remote connector with the image to upload to the server.
     *
     * @param result A listener that contains the result of the operation at the end of the
     *               request.
     */
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
