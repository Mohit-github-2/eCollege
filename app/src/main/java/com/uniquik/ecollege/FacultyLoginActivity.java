package com.uniquik.ecollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FacultyLoginActivity extends AppCompatActivity {
    private EditText InputEmail , InputPassword;
    private TextView LostPassword , CreateAccountTV;
    private Button Login;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        loadingBar = new ProgressDialog(this);
        InputEmail = findViewById(R.id.faculty_login_email);
        InputPassword = findViewById(R.id.faculty_login_password);
        LostPassword = findViewById(R.id.tv_login_forget_password);
        CreateAccountTV = findViewById(R.id.tv_faculty_login_new);
        Login = findViewById(R.id.faculty_login_btn);
        mAuth = FirebaseAuth.getInstance();

        CreateAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyLoginActivity.this , FacultySignUpActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginfaculty();
            }
        });
    }

    private void loginfaculty() {
        final String email = InputEmail.getText().toString();
        final String password = InputPassword.getText().toString();

        if(!email.equals("") && !password.equals("")) {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait You are almost there..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(FacultyLoginActivity.this , FacultyDashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Provide details then Proceed", Toast.LENGTH_SHORT).show();
        }


    }
}
