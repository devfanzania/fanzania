package com.yorker.fanzania.views.imagecapture;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yorker.fanzania.BuildConfig;
import com.yorker.fanzania.R;
import com.yorker.fanzania.constants.Constants;
import com.yorker.fanzania.databinding.ActivityImageCaptureBinding;
import com.yorker.fanzania.helper.CompressImage;

import java.io.File;
import java.util.Calendar;
import java.util.Objects;


public class ActivityImageTaker extends AppCompatActivity {

    private File finalFile = null;
    private ActivityImageCaptureBinding binding;
    private final String BITMAP_STORAGE_URL = "IMAGE_URL";
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static boolean SELECTED = false;
    private RequestOptions requestOptions;
    ;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_capture);

        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_boy);
        requestOptions.error(R.drawable.ic_boy);

        binding.inToolbar.toolbar.setNavigationIcon(R.drawable.ic_back_white);
        binding.inToolbar.toolbar.setTitle("");

        binding.inToolbar.toolbarTitle.setText(getString(R.string.text_imagePreview));

        setSupportActionBar(binding.inToolbar.toolbar);

        if (binding.inToolbar.toolbar != null)
            binding.inToolbar.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.inButton.btnButton.setText(getString(R.string.text_continue));

        binding.inButton.btnButton.setOnClickListener(v -> {
            SELECTED = true;
            onBackPressed();
        });

        if (Constants.CAMERA) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_MEDIA_IMAGES)) {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    try {
                        finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = FileProvider.getUriForFile(ActivityImageTaker.this,
                                BuildConfig.APPLICATION_ID + ".provider", finalFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 2000);
                        Constants.CAMERA = false;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            } else if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    try {
                        finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = FileProvider.getUriForFile(ActivityImageTaker.this,
                                BuildConfig.APPLICATION_ID + ".provider", finalFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 2000);
                        Constants.CAMERA = false;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                try {
                    finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoURI = FileProvider.getUriForFile(ActivityImageTaker.this,
                            BuildConfig.APPLICATION_ID + ".provider", finalFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 2000);
                    Constants.CAMERA = false;
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            if (finalFile != null || finalFile.exists()) {
                CompressImage compressImage = new CompressImage(ActivityImageTaker.this);
                finalFile = new File(compressImage.compressImage(finalFile.toString()));
                Glide.with(this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(finalFile).into(binding.imgImageView);
//                Picasso.with(ActivityImageTaker.this).load(finalFile).transform(new RoundTrans(ActivityImageTaker.this))
//                        .resize(470, 500).placeholder(R.drawable.ic_user).into(binding.imgImageView);
            } else {
                SELECTED = true;
                onBackPressed();
            }
        } else {
            if (finalFile != null) {
                if (finalFile.exists())
                    finalFile.delete();
            }
            finalFile = null;
            onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (finalFile != null)
            outState.putString(BITMAP_STORAGE_URL, finalFile.getAbsolutePath());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (finalFile == null) {
            finalFile = new File(Objects.requireNonNull(savedInstanceState.getString(BITMAP_STORAGE_URL)));
            CompressImage compressImage = new CompressImage(ActivityImageTaker.this);
            finalFile = new File(compressImage.compressImage(finalFile.toString()));
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(finalFile).into(binding.imgImageView);
//            Picasso.with(ActivityImageTaker.this).load(finalFile).transform(new RoundTrans(ActivityImageTaker.this))
//                    .resize(470, 500).placeholder(R.drawable.ic_user).into(binding.imgImageView);
        }
    }

    @Override
    public void onBackPressed() {
        if (finalFile != null) {
            if (SELECTED) {
                Intent i = new Intent();
                if (finalFile.exists()) {
                    i.putExtra("data", finalFile.getAbsolutePath());
                    setResult(1, i);
                } else {
                    i.putExtra("data", "");
                    setResult(0, i);
                }
            } else {
                Intent i = new Intent();
                i.putExtra("data", "");
                setResult(0, i);
            }
        }
        super.onBackPressed();
    }

    //---------Permission---------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (grantResults.length > 1
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri photoURI = FileProvider.getUriForFile(ActivityImageTaker.this,
                                BuildConfig.APPLICATION_ID + ".provider", finalFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 2000);
                        Constants.CAMERA = false;

                    } else {
                        Toast.makeText(ActivityImageTaker.this, "Unable to get images.", Toast.LENGTH_SHORT).show();
                    }
                } else if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    finalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/" + Calendar.getInstance().getTimeInMillis() + ".png");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoURI = FileProvider.getUriForFile(ActivityImageTaker.this,
                            BuildConfig.APPLICATION_ID + ".provider", finalFile);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 2000);
                    Constants.CAMERA = false;

                } else {
                    Toast.makeText(ActivityImageTaker.this, "Unable to get images.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
