package com.example.collegeproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.collegeproject.R;
import com.example.collegeproject.adapter.Adapter_Admin_Studentlist;
import com.example.collegeproject.databinding.ActivityAdminBinding;
import com.example.collegeproject.databinding.RegistrationLayoutBinding;
import com.example.collegeproject.databinding.StudentDataLayoutBinding;
import com.example.collegeproject.model.Student;
import com.example.collegeproject.model.birt_date;
import com.example.collegeproject.videmodel.sViewmodel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Admin_Activity extends AppCompatActivity implements Adapter_Admin_Studentlist.ItemClcikListener {

    private String admin_name;
    private String admin_password;
    private ActivityAdminBinding activityAdminBinding;
    private sViewmodel  viewmodel;

    private ArrayList<Student> studentslist=new ArrayList<>();
    private ArrayList<Student>  filteredData=new ArrayList<>();

    private  FirebaseDatabase firebaseDatabase;
    private RecyclerView recyclerView;

    private Adapter_Admin_Studentlist adapter_admin_studentlist;
    private SearchView sear_bar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAdminBinding=ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(activityAdminBinding.getRoot());
        activityAdminBinding.adminProgressbar.setVisibility(View.VISIBLE);

        firebaseDatabase=FirebaseDatabase.getInstance();

        viewmodel=new ViewModelProvider(this).get(sViewmodel.class);
        recyclerView=findViewById(R.id.admin_recyclerview);
        sear_bar_text=findViewById(R.id.search_view);




        activityAdminBinding.addstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showalterdialogueforadmin_registerstudent();
            }
        });
        recyclerView.setHasFixedSize(true);
        adapter_admin_studentlist=new Adapter_Admin_Studentlist(this,studentslist);
        adapter_admin_studentlist.setClcikListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter_admin_studentlist);


        loaddata();

        sear_bar_text.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filter(newText);
               return true;
            }
        });



//
//        SharedPreferences sh = getSharedPreferences("MyadminPref", MODE_PRIVATE);
//        String sname = sh.getString("admin_name", "");
//        String rollmo=sh.getString("admin_pass","0");

        activityAdminBinding.logadminout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showadminlogdialogue();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Student deltask = studentslist.get(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);
// Set any other properties for the AlertDialog as needed
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete record of"+deltask.getName());
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityAdminBinding.adminProgressbar.setVisibility(View.VISIBLE);
                        viewmodel.deletestudentdetails(deltask);
                                loaddata();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activityAdminBinding.adminProgressbar.setVisibility(View.VISIBLE);
                       loaddata();
                   dialog.dismiss();
                    }
                });
builder.show();
            }
        }).attachToRecyclerView(recyclerView);




    }


    public  void loaddata(){

                DatabaseReference databaseReference = firebaseDatabase.getReference().child("PTU");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        studentslist.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Student student = snapshot1.getValue(Student.class);
                            studentslist.add(student);
                        }
                        if(studentslist.size()==0 ){
                            Toast.makeText(Admin_Activity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }else {
                            adapter_admin_studentlist.setstudent(studentslist);

                        }
                        activityAdminBinding.adminProgressbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Admin_Activity.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });






    }





    private void showalterdialogueforadmin_registerstudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);

// Inflate the custom layout for the AlertDialog
        View view = LayoutInflater.from(Admin_Activity.this).inflate(R.layout.registration_layout, null);
// Set the custom layout for the AlertDialog

        RegistrationLayoutBinding binding = RegistrationLayoutBinding.bind(view);


        builder.setView(view);

// Set any other properties for the AlertDialog as needed
        builder.setTitle("Welcome");
// Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        binding.rl2.rregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=binding.rl2.remil.getText().toString();
                String password=binding.rl2.rpassword.getText().toString();
                // Handle the OK button click here
                if(TextUtils.isEmpty(email)){
                    binding.rl2.remil.setError("Invalid field");
                }else if(TextUtils.isEmpty(password)){
                    binding.rl2.rpassword.setError("Invalid field");
                }else if(password.length()<8){
                    binding.rl2.rpassword.setError("minimum 8 character");
                }else if(!isValidEmailAddress(email)) {
                    binding.rl2.remil.setError("Invalid email");
                }else{
                    alertDialog.dismiss();
                    Intent intent=new Intent(Admin_Activity.this,Registration_Activity.class);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                   startActivityForResult(intent,10);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10) {
            activityAdminBinding.adminProgressbar.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                }
            },6000);
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private void showadminlogdialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);
        builder.setMessage("Are you sure you want to log out?")
                .setTitle("Log out")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform logout action
                        savedstudentsharedpre("","");
                        startActivity(new Intent(Admin_Activity.this,MainActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the logout action
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void savedstudentsharedpre(String Ename,String Eroll) {
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("MyadminPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("admin_name",Ename );
        myEdit.putString("admin_pass",Eroll);
        myEdit.apply();
    }

    @Override
    public void onClick(View view, int postion) {
        Toast.makeText(this, ""+studentslist.get(postion).getName(), Toast.LENGTH_SHORT).show();
        showadmineditdetailsalterdialogue(studentslist.get(postion));
    }


    private void showadmineditdetailsalterdialogue(Student student){
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_Activity.this);

// Inflate the custom layout for the AlertDialog
        View view = LayoutInflater.from(Admin_Activity.this).inflate(R.layout.student_data_layout, null);
// Set the custom layout for the AlertDialog

        StudentDataLayoutBinding binding = StudentDataLayoutBinding.bind(view);
        Glide.with(this).load(student.getImage_url()).into(binding.studentImage);
        binding.sdob.setText(student.getDob());
        binding.sname.setText(student.getName());
        binding.srollno.setText(student.getRoll_no());
        binding.sfather.setText(student.getfName());
        binding.sbarnch.setText(student.getBranch_name());

        builder.setView(view);
        binding.sdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

// Create a new DatePickerDialog instance and set the initial date
                DatePickerDialog datePickerDialog = new DatePickerDialog(Admin_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        // Handle the selected date here
                        birt_date date = new birt_date(selectedDayOfMonth, selectedMonth + 1, selectedYear);
                        student.setDob(date.toString());
                        binding.sdob.setText(student.getDob());
                    }
                }, year, month, dayOfMonth);

// Show the DatePickerDialog
                datePickerDialog.show();


            }
        });
// Set any other properties for the AlertDialog as needed
        builder.setTitle("Edit Details");
        AlertDialog alertDialog = builder.create();
        binding.ssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.sname.getText().toString();
                String fathername = binding.sfather.getText().toString();
                String branch = binding.sbarnch.getText().toString();
                String rollno = binding.srollno.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    binding.sname.setError("Invalid field");
                } else if (TextUtils.isEmpty(fathername)) {
                    binding.sfather.setError("Invalid field");
                } else if (TextUtils.isEmpty(branch)) {
                    binding.sbarnch.setError("Invalid field");
                } else if (TextUtils.isEmpty(rollno)) {
                    binding.srollno.setError("Invalid field");
                } else if (TextUtils.isEmpty(binding.sdob.getText().toString())) {
                    binding.sname.setError("Invalid field");
                } else {
                    student.setName(name);
                    student.setfName(fathername);
                    student.setBranch_name(branch);
                    student.setRoll_no(rollno);
                    ExecutorService executorService=Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewmodel.editstudentdetails(student);
                        }
                    });
                    activityAdminBinding.adminProgressbar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loaddata();
                        }
                    },6000);

                    alertDialog.dismiss();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
// Create and show the AlertDialog
        alertDialog.show();
    }


    public void filter(String query) {
        filteredData.clear();
        if (TextUtils.isEmpty(query)) {
            filteredData.addAll(studentslist);

        } else {
            query = query.toLowerCase();
            for (Student item : studentslist) {
                if (item.getName().toLowerCase().contains(query)) {
                    filteredData.add(item);
                }
            }
        }
        adapter_admin_studentlist.setstudent(filteredData);
    }







}