package com.example.idcardgenerator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.idcardgenerator.databinding.ActivityFormBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityFormBinding binding;
    private ImageView cameraImage;
    private EditText enrolment, name, address, studentContact, emergencyContact, dobText;
    private ImageButton dobBtn;
    private Spinner course, bloodGroup;
    private Button submitBtn;
    private String dateString, courseString, bloodString;
    private Bitmap imageBitmap;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        setContentView(binding.getRoot());

        //Picture choose functionality
        cameraImage.setOnClickListener(v -> imagePicker());

        dobBtn.setOnClickListener(view -> dateDialogFunc());

        //Call spinner for course
        setupSpinner(course,R.array.courses_array,courseListener);

        //Call spinner for blood group
        setupSpinner(bloodGroup,R.array.blood_groups_array, bloodGroupListener);

        submitBtn.setOnClickListener(v -> {
            submitFunc();
        });
    }

    private void submitFunc() {
        ArrayList<CharSequence> details = new ArrayList<>();
        details.add(enrolment.getText().toString());
        details.add(name.getText().toString());
        details.add(address.getText().toString());
        details.add(studentContact.getText().toString());
        details.add(emergencyContact.getText().toString());
        details.add(dateString);
        details.add(courseString);
        details.add(bloodString);
        Log.d("pritom", ""+details);

        Intent intent = new Intent(this, PrintActivity.class);

        //Put parcelable data into intent
        intent.putCharSequenceArrayListExtra("details", details);
        intent.putExtra("imageUri",imageUri);

        startActivity(intent);
    }


    //Initialize all the variable
    private void init() {
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        cameraImage = binding.cameraImg;
        enrolment = binding.enrolmentEditText;
        name = binding.nameEditText;
        address = binding.addEditText;
        studentContact = binding.studentContactEditText;
        emergencyContact = binding.emergencyContactEditText;
        dobText = binding.dateEditText;
        dobBtn = binding.dobBtn;
        course = binding.coursesSpinner;
        bloodGroup = binding.bloodSpinner;
        submitBtn = binding.submitBtn;

    }


    private void imagePicker() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            assert data != null;
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(cameraImage);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Image choose error code: "+resultCode,Toast.LENGTH_SHORT).show();
        }
    }

    private void dateDialogFunc() {
        DatePicker datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(), "DATE PICK");
    }

    //Date change listener to get the date assign it to edittext
    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        //Make a separate instance of date to set the date temporary
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
        dateString = sdf.format(calendar.getTime());
        dobText.setText(dateString);
    }


    //Initialize spinner with corresponding spinner
    private void setupSpinner(Spinner spinner, int resourceArray,
                              AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                resourceArray,
                android.R.layout.simple_spinner_item
        );
        //specify spinner layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //apply adapter to spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(listener);
    }


    //Selected listener for course choice
    private AdapterView.OnItemSelectedListener courseListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            courseString = getResources().getStringArray(R.array.courses_array)[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            courseString = getResources().getStringArray(R.array.courses_array)[0];
        }
    };

    //Selected listener for blood group choice
    private AdapterView.OnItemSelectedListener bloodGroupListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            bloodString = getResources().getStringArray(R.array.blood_groups_array)[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            bloodString = getResources().getStringArray(R.array.blood_groups_array)[0];
        }
    };
}