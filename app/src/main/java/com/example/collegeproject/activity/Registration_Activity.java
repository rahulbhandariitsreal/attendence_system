package com.example.collegeproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import com.example.collegeproject.model.Student;
import com.example.collegeproject.databinding.ActivityRegistrationBinding;
import com.example.collegeproject.model.birt_date;
import com.example.collegeproject.videmodel.sViewmodel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Registration_Activity extends AppCompatActivity {


    private Student student;
    public static ActivityRegistrationBinding binding;
    private Uri selectedimageuri;
    private String email;
    private String password;
    private String mnumber;
    private sViewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        student = new Student();
        viewModel = new ViewModelProvider(this).get(sViewmodel.class);
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        mnumber=getIntent().getStringExtra("pnumber");
        student.setEmailID(email);
        binding.dataStudent.pnumber.setText(mnumber);
        binding.dataStudent.studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        binding.dataStudent.sdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

// Create a new DatePickerDialog instance and set the initial date
                DatePickerDialog datePickerDialog = new DatePickerDialog(Registration_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date here
                        birt_date date = new birt_date(selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        student.setDob(date.toString());
                        binding.dataStudent.sdob.setText(student.getDob());
                    }
                }, year, month, dayOfMonth);

// Show the DatePickerDialog
                datePickerDialog.show();


            }
        });

        binding.dataStudent.ssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.dataStudent.sname.getText().toString();
                String fathername = binding.dataStudent.sfather.getText().toString();
                String branch = binding.dataStudent.sbarnch.getText().toString();
                String rollno = binding.dataStudent.srollno.getText().toString();
                String number=binding.dataStudent.pnumber.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    binding.dataStudent.sname.setError("Invalid field");
                } else if (TextUtils.isEmpty(fathername)) {
                    binding.dataStudent.sfather.setError("Invalid field");
                } else if (TextUtils.isEmpty(branch)) {
                    binding.dataStudent.sbarnch.setError("Invalid field");
                } else if (TextUtils.isEmpty(rollno)) {
                    binding.dataStudent.srollno.setError("Invalid field");
                } else if (TextUtils.isEmpty(binding.dataStudent.sdob.getText().toString())) {
                    binding.dataStudent.sname.setError("Invalid field");
                }else if (number.length()!=10) {
                    binding.dataStudent.pnumber.setError("Invalid number");
                }
                    else {
                    binding.progressbar.setVisibility(View.VISIBLE);
                   int ranomfirsttwo= (int) (Math.random() * 1000);
                    int ranomlasttwo= (int) (Math.random() * 1000);
                    String uniID=ranomfirsttwo+"PTU"+ranomlasttwo;
                    student.setUniqueID(uniID);
                    student.setName(name);
                    student.setfName(fathername);
                    student.setBranch_name(branch);
                    student.setRoll_no(rollno);
                    student.setPhone_number(number);
                    savedstudentsharedpre(name,rollno);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.upload_student_data(student, selectedimageuri,email,password);

                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    },6000);
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            selectedimageuri = uri;
            // Get the URI of the image
          //  Uri imageUri = Uri.parse("content://mycontentprovider/images/image1.jpg");

// Get a reference to the content resolver
            ContentResolver resolver = getContentResolver();

// Open an input stream to read the image data
            InputStream inputStream = null;
            try {
                inputStream = resolver.openInputStream(uri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

// Decode the input stream into a bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

// Close the input stream
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            binding.dataStudent.studentImage.setImageBitmap(bitmap);
        }
    }
    public void savedstudentsharedpre(String Ename,String Eroll) {
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("name",Ename );
        myEdit.putString("rollno",Eroll);
        myEdit.apply();
    }
}