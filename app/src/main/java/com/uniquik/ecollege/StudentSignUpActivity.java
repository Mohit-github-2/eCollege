package com.uniquik.ecollege;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentSignUpActivity extends AppCompatActivity{
    private Button CreateAccountButton;
    private Spinner InputDept;
    private Spinner InputSection;
    private EditText InputStudentName ,InputStudentPhoneNumber , InputPassword , InputEmail , InputUSN, InputParentPhoneNumber , InputParentName;
    private ProgressDialog loadingBar;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);
        InputStudentName = findViewById(R.id.student_signUp_name);
        InputStudentPhoneNumber = findViewById(R.id.student_signUp_phone);
        InputPassword = findViewById(R.id.student_signUp_password);
        InputEmail = findViewById(R.id.student_signUp_email);
        InputUSN = findViewById(R.id.student_signUp_USN);
        InputParentPhoneNumber = findViewById(R.id.student_signUp_parent_phone);
        InputParentName = findViewById(R.id.student_signUp_parent_name);
        CreateAccountButton = findViewById(R.id.student_signUp_btn_create_account);
        loadingBar = new ProgressDialog(this);
        InputDept = findViewById(R.id.student_signUp_dept_spinner);
        InputSection= findViewById(R.id.student_signUp_section_spinner);



        final Spinner InputDept  = findViewById(R.id.student_signUp_dept_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InputDept.setAdapter(adapter);

        Spinner InputSection  = findViewById(R.id.student_signUp_section_spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.section, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InputSection.setAdapter(adapter2);
        InputDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(InputDept.getItemAtPosition(position).equals("select department")){

                }else{
                    String item = InputDept.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }



    private void CreateAccount() {

        String email = InputEmail.getText().toString();
        String password = InputPassword.getText().toString();
        String studentName = InputStudentName.getText().toString();
        String Usn = InputUSN.getText().toString();
        String studentPhone = InputStudentPhoneNumber.getText().toString();
        String parentPhone = InputParentPhoneNumber.getText().toString();
        String parentName = InputParentName.getText().toString();
        String section = InputSection.getSelectedItem().toString();
        String dept = InputDept.getSelectedItem().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter your email ...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Set Some Password ...", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(studentName))
        {
            Toast.makeText(this, "Please Enter your name ...", Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(Usn))
        {
            Toast.makeText(this, "Please Enter your USN ...", Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(studentPhone))
        {
            Toast.makeText(this, "Please Enter your Phone Number ...", Toast.LENGTH_SHORT).show();
        }

      else  if(TextUtils.isEmpty(parentName))
        {
            Toast.makeText(this, "Please Enter your Father's or Mother's name ...", Toast.LENGTH_SHORT).show();
        }

      else  if(TextUtils.isEmpty(parentPhone))
        {
            Toast.makeText(this, "Please Enter your parent's Number ...", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(dept))
        {
            Toast.makeText(this, "Please Select your Department ...", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(section))
        {
            Toast.makeText(this, "Please Select your Section and year ...", Toast.LENGTH_SHORT).show();
        }
       else if(!email.matches(emailPattern))
       {
            Toast.makeText(this, "Please Enter a valid email", Toast.LENGTH_SHORT).show();
        }
            else if(studentPhone.matches(parentPhone))
            {
            Toast.makeText(this, "Please Use Different Numbers in both fields", Toast.LENGTH_SHORT).show();
            }
            else if(password.length() < 6)
        {

            Toast.makeText(this, "Min. 6 Characters Required", Toast.LENGTH_SHORT).show();

        }
            else if (studentPhone.length()<10){
            Toast.makeText(this, "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (parentPhone.length()<10) {
            Toast.makeText(this, "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (studentPhone.length()>10){
            Toast.makeText(this, "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (parentPhone.length()>10) {
            Toast.makeText(this, "Please Enter a valid Phone Number", Toast.LENGTH_SHORT).show();
        }


        else
        {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Kindly have patience while we are creating your account..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidatephoneNumber(studentName,email,password,Usn,studentPhone,parentName,parentPhone,dept,section);
        }





    }

    private void ValidatephoneNumber(final String studentName, final String email, final String password, final String Usn, final String studentPhone, final String parentName, final String parentPhone, final String dept ,final String section) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(!(datasnapshot.child("Students").child(Usn).exists()))
                {
                    HashMap<String,Object>userDataMap = new HashMap<>();
                    userDataMap.put("studentName",studentName);
                    userDataMap.put("email",email);
                    userDataMap.put("password",password);
                    userDataMap.put("Usn",Usn);
                    userDataMap.put("studentPhoneNumber",studentPhone);
                    userDataMap.put("parentName",parentName);
                    userDataMap.put("parentPhoneNumber",parentPhone);
                    userDataMap.put("Department",dept);
                    userDataMap.put("Section",section);
                    userDataMap.put("ApprovalState" , "Not Approved");
                    RootRef.child("Students").child(Usn).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(StudentSignUpActivity.this, "Congrats, your account is created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent i = new Intent(StudentSignUpActivity.this,StudentDashboardActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(StudentSignUpActivity.this, "We are having trouble to reach the servers,We request you to check your network connectivity too.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(StudentSignUpActivity.this, "User with the USN-" + Usn + "already exists,Please login",Toast.LENGTH_LONG).show();

                    loadingBar.dismiss();
                    Intent intent  = new Intent (StudentSignUpActivity.this , StudentLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });



    }


}
