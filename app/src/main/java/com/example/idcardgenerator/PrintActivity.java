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

        Button button = binding.printBtn;
        button.setOnClickListener(view -> {
            LinearLayout layout = binding.idCardLayout;
            layout.setDrawingCacheEnabled(true);
            Bitmap b = layout.getDrawingCache();

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
}