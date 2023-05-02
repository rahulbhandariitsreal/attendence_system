package com.example.collegeproject.repository;

import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.collegeproject.activity.Admin_Activity;
import com.example.collegeproject.activity.Registration_Activity;
import com.example.collegeproject.model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {


    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;

     private MutableLiveData<ArrayList<Student>> mutableLiveDatastudent = new MutableLiveData<>();


    public Repository() {

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        mAuth=FirebaseAuth.getInstance();
    }


    public void deleteimage(Student student){
        storageReference=firebaseStorage.getReference().child(student.getUniqueID());
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }


    public void editstudentdetail(Student student){
databaseReference=firebaseDatabase.getReference().child("PTU").child(student.getUniqueID());
        databaseReference.setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Log.v("TAGGY","Updated  succesfully");

                }else{
                    Log.v("TAGGY","updated to upload");
                }
            }
        });

    }

    public void removestudentdetail(Student student){

        databaseReference=firebaseDatabase.getReference().child("PTU").child(student.getUniqueID());
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.v("TAG","deleted");
                    ExecutorService executorService=Executors.newSingleThreadExecutor();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            deleteimage(student);
                        }
                    });

                }else{
                    Log.v("TAG","falied to delete");
                }
            }
        });

    }


    public LiveData<ArrayList<Student>> getstudentlist(){
        ArrayList<Student> students=new ArrayList<>();
        databaseReference=firebaseDatabase.getReference().child("PTU");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                   Student student = snapshot1.getValue(Student.class);
                    students.add(student);
                }
                mutableLiveDatastudent.setValue(students);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return mutableLiveDatastudent;
    }

    public void register(String email,String password){
        ExecutorService executorService= Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.v("TAG","registration succesfull");
                            mAuth.signOut();
                        }else{
                            Log.v("TAG","failed to register");
                        }
                    }
                });
            }
        });


    }

    public  void uploadstudent_data(Student student, Uri image_uri,String email,String passwrod){
        if(image_uri == null){
            return;
        }
        databaseReference=firebaseDatabase.getReference().child("PTU").child(student.getUniqueID());
        storageReference=firebaseStorage.getReference().child(student.getUniqueID());
        storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

           storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   String image_path=uri.toString();
                   student.setImage_url(image_path);
                   databaseReference.setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               register(email,passwrod);
                               Log.v("TAGGY","uploaded succesfully");
                               Registration_Activity.binding.progressbar.setVisibility(View.GONE);
                           }else{
                               Log.v("TAGGY","Failed to upload");
                               Registration_Activity.binding.progressbar.setVisibility(View.GONE);
                           }
                       }
                   });
               }
           });
            }
        });

    }

}
