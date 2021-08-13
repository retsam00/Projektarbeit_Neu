package com.example.projektarbeit_neu;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity{
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File photoFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postfile);

        dispatchTakePictureIntent();
        loadImage();
        TextView tvResult = (TextView)findViewById(R.id.txtResult);
        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend.setEnabled(false);
                new Thread() {
                    public boolean running = true;
                    public synchronized void run() {
                        if(running) {
                            JSONObject inputJSON = new JSONObject();
                            try {
                                inputJSON.put("image", "testimage");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            NetworkManager nm = new NetworkManager("http://192.168.178.107:8000/receive/");
                            inputJSON = nm.bitmapToJSON(createImageBitmap(photoFile));
                            JSONObject result = nm.postFile(inputJSON);
                            if(result != null) {
                                tvResult.append(result.toString());
                            }


                        }
                    }
                }.start();
                btnSend.setEnabled(true);
            }
        });

    }


    //öffne Kamera App
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //ComponentName test = takePictureIntent.resolveActivity(getPackageManager());
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView ivPreview = (ImageView)findViewById(R.id.ivPreview);
            ivPreview.setImageBitmap(createImageBitmap(photoFile));
        }
    }

    private Bitmap createImageBitmap(File image)
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap,500,500,true);
        return bitmap;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void loadImage()
    {
        if(photoFile.exists()){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            try {
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bmOptions);
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageBitmap(myBitmap);
            }
            catch(Exception x)
            {
                x.printStackTrace();
            }
        }
    }

    private void galleryAddPic() {
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
