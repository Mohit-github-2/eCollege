package com.uniquik.ecollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uniquik.ecollege.Model.Students;
import com.uniquik.ecollege.Prevalent.Prevalent;

import java.util.HashMap;

import io.paperdb.Paper;

public class StudentLoginActivity extends AppCompatActivity {
    private EditText usnInput,passwordInput;
    private TextView tvForgotPassword,tvSignUp;
    private Button btnLogin;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        usnInput = findViewById(R.id.student_login_USN);
        passwordInput = findViewById(R.id.student_login_password);
        tvForgotPassword = findViewById(R.id.student_login_tv_forgot_password);
        tvSignUp = findViewById(R.id.student_login_tv_signUp);
        btnLogin = findViewById(R.id.student_login_btn);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentLoginActivity.this,ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

    tvSignUp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent j = new Intent (StudentLoginActivity.this,StudentSignUpActivity.class);
            startActivity(j);
        }
    });

    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginUser();
        }
    });




    }

    private void LoginUser() {

        String Usn = usnInput.getText().toString();
        String password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(Usn))
        {
            Toast.makeText(this, "Please enter your USN/Roll No. below", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your Password", Toast.LENGTH_SHORT).show();
        }
        else
        {
           loadingBar.setTitle("Logging In");
           loadingBar.setMessage("Please Wait , You are almost there..");
           loadingBar.setCanceledOnTouchOutside(false);
           loadingBar.show();

           AllowAccessToAccount(Usn,password);

        }

    }

    private void AllowAccessToAccount(final String Usn, final String password ) {

        Paper.book().write(Prevalent.StudentUsnKey , Usn);
        Paper.book().write(Prevalent.StudentPasswordKey , password);
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                if(datasnapshot.child("Students").child(Usn).exists())

                {


                    Students userData = datasnapshot.child ("Students").child(Usn).getValue(Students.class);
                    if(userData.getUsn().equals(Usn))
                    {
                        if(userData.getPassword().equals(password))
                        {

                            Toast.makeText(StudentLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(StudentLoginActivity.this , StudentDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            {
                            loadingBar.dismiss();
                            Toast.makeText(StudentLoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(StudentLoginActivity.this, "Account with " + Usn + " doesn't exists,Please go to SignUp Page to create Account.", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
