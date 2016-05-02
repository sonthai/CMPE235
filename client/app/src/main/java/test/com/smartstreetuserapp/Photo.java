package test.com.smartstreetuserapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA = 0;
    private static final int EXTERNAL_STORAGE_PERMISSION =1;
    private String imgFileLocation="";
    static final int REQUEST_VIDEO_CAPTURE = 2;
    private ImageView viewCapturePhoto;
    private ShareButton sharebutton;
    LoginManager loginmanager;// To avoid login dialog
    ImageButton imgbttn ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // intialize facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_photo);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);
        viewCapturePhoto = (ImageView)findViewById(R.id.photoView);
        imgbttn = (ImageButton) findViewById(R.id.shareMailButton);
        imgbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFile();
            }
        });
        sharebutton =(ShareButton) findViewById(R.id.share_btn);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // captureVideo will be called when take video button is clicked
    public void captureVideo (View view) throws IOException {
        Intent videoIntent = new Intent();
        videoIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        File videofile = null;
        videofile =createVideoFile();
        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videofile));
        startActivityForResult(videoIntent, ACTIVITY_START_CAMERA);

    }


    /*
      For API level 23 Marshmallow we need to get runtime permission, following is the code for Wrting to external Storage
   */
    public void capturePhoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callCameraApp();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "External Storage permission Required", Toast.LENGTH_SHORT).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
            }

        }

    }
    /*
    * CallCameraApp will call the device camera to take photo and store the image in external Storage
    *
    */
    private void  callCameraApp()
    {
        Intent photoIntent = new Intent();
        photoIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile =null;
        try{
            photoFile=createFile();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile)); // for additional information like path of file
        startActivityForResult(photoIntent, ACTIVITY_START_CAMERA);// start camera intent



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]grantedResults) {
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            }
            else{
                Toast.makeText(this,"External Storage permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
        else{
            super.onRequestPermissionsResult(requestCode,permissions,grantedResults);
        }

    }

    /**
     * onActivityResult method will be called after closing the camera
     *
     **/

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        if(requestCode==ACTIVITY_START_CAMERA && resultCode==RESULT_OK) {
            // setting the sharing button visible
            sharebutton.setVisibility(View.VISIBLE);
            imgbttn.setVisibility(View.VISIBLE);
            final Bitmap photoBitmap = BitmapFactory.decodeFile(imgFileLocation);
            viewCapturePhoto.setImageBitmap(photoBitmap); // set decode image to imageview
            viewCapturePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // share file to facebook will be called  using facebook sdk and registering app on facebook developer and using the sdk
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(photoBitmap)
                            .setCaption("First Post")
                            .build();
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    sharebutton.setShareContent(content);
                    sharebutton.performClick();
                }
            });
        }

        else if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap videoBitmap = BitmapFactory.decodeFile(imgFileLocation);
            }

        }
    }
    /*
        Method for creating file and sotring it in the Sd card
    */
    File createFile() throws IOException{
        /*
            creating timestamp, filename and storage path for each image
         */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFilename = "IMAGE_"  + timeStamp;
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imgFilename,".jpg",storageDirectory);
        imgFileLocation = image.getAbsolutePath();
        return image;


    }
    // for video file creation
    File createVideoFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFilename = "VIDEO_"  + timeStamp;
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File video = File.createTempFile(imgFilename,".mp4",storageDirectory);
        imgFileLocation = video.getAbsolutePath();
        return video;


    }

    /* share file by mail, passing sharing intent and setting the content type to image

     */
    // to do replace share button with audio button
    private void shareFile(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpg");
        File imagepath = new File(imgFileLocation);
        Uri uri = Uri.fromFile(imagepath);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share via!"));
    }





}
