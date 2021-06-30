package com.uniquik.ecollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uniquik.ecollege.Model.Students;
import com.uniquik.ecollege.Prevalent.Prevalent;

import io.paperdb.Paper;

public class UserActivity extends AppCompatActivity {

    Button studentBtn,facultyBtn, adminBtn;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        studentBtn = findViewById(R.id.user_student_btn);
        facultyBtn = findViewById(R.id.user_faculty_btn);
        Paper.init(this);
        loadingBar= new ProgressDialog(this);
        adminBtn = findViewById(R.id.user_admin_btn);
        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this,StudentLoginActivity.class);
                startActivity(i);
            }
        });
        facultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(UserActivity.this, FacultyLoginActivity.class);
                startActivity(j);
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(UserActivity.this , AdminLoginActivity.class);
                startActivity(k);
                finish();
            }
        });

        String StudentUsnKey = Paper.book().read(Prevalent.StudentUsnKey);
        String StudentPasswordKey = Paper.book().read(Prevalent.StudentPasswordKey);

        if (StudentUsnKey!="" && StudentPasswordKey!="") {
            if(!TextUtils.isEmpty(StudentUsnKey) && !TextUtils.isEmpty(StudentPasswordKey))
            {
                AllowAccess(StudentUsnKey ,StudentPasswordKey);

                loadingBar.setTitle("Ready");
                loadingBar.setMessage("Please Wait , Getting Everything Ready For you....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!= null)
        {
            Intent intent= new Intent(UserActivity.this , FacultyDashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String  Usn, final String password) {

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
                            Toast.makeText(UserActivity.this, "Your Dashboard is Ready Now", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(UserActivity.this , StudentDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(UserActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(UserActivity.this, "Account with " + Usn + " doesn't exists,Please go to SignUp Page to create Account.", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
