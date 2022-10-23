package com.example.idcardgenerator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import com.bumptech.glide.Glide;
import com.example.idcardgenerator.databinding.ActivityPrintBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class PrintActivity extends AppCompatActivity {

    private ActivityPrintBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ArrayList<CharSequence> details = intent.getCharSequenceArrayListExtra("details");
        Log.d("pritom", ""+details);
        Uri uri = intent.getParcelableExtra("imageUri");
        Log.d("pritom",""+uri);


        assignDetails(details,uri);


        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 1);

        Button button = binding.printBtn;
        button.setOnClickListener(view -> {
            LinearLayout layout = binding.idCardLayout;
            layout.setDrawingCacheEnabled(true);
            Bitmap b = layout.getDrawingCache();

//            ImageView img = findViewById(R.id.imageView);
//            img.setImageBitmap(b);
//            Glide.with(this).load(uri).into(img);

            doPhotoPrint(b);

        });

    }

    private void assignDetails(ArrayList<CharSequence> details, Uri imageUri) {
        binding.nameTv.setText(details.get(0));
        binding.enrolmentTv.setText(details.get(1));
        binding.addressTv.setText(details.get(2));
        binding.studentContactTv.setText("Student Contact: "+details.get(3));
        binding.emergencyContactTv.setText("Emergency Contact: "+details.get(4));
        binding.dobTv.setText("D.O.B. "+details.get(5));
        binding.courseTv.setText("Course: "+details.get(6));
        binding.bloodTv.setText("Blood Gr.:"+details.get(7));
        Glide.with(this).load(imageUri).into(binding.studentPicIv);
    }

    private void doPhotoPrint(Bitmap bitmap) {
        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }

    public static File bitmapToFile(Context context, Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file; // it will return null
        }
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(PrintActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(PrintActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(PrintActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(PrintActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PrintActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}