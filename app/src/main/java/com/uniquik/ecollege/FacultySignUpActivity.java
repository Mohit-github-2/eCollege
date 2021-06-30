package com.uniquik.ecollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FacultySignUpActivity extends AppCompatActivity {
    private Spinner InputDept;
    private EditText InputEmail;
    private EditText InputPassword;
    private EditText InputName;
    private EditText InputPhone;
    private Button signUpBtn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_sign_up);
        InputDept  = findViewById(R.id.faculty_signUp_dept_spinner);
        InputEmail = findViewById(R.id.faculty_signUp_email);
        InputPassword = findViewById(R.id.faculty_signUp_password);
        InputName = findViewById(R.id.faculty_signUp_name);
        InputPhone = findViewById(R.id.faculty_signUp_phone);
        signUpBtn = findViewById(R.id.faculty_signUp_btn_create_account);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);







        final Spinner InputDept  = findViewById(R.id.faculty_signUp_dept_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.department, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InputDept.setAdapter(adapter);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFaculty();
            }
        });
    }

    private void registerFaculty() {
        final String name = InputName.getText().toString();
        final String email = InputEmail.getText().toString();
        final String password = InputPassword.getText().toString();
        final String dept  = InputDept.getSelectedItem().toString();
        final String phone = InputPhone.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !dept.equals(""))
        {
            loadingBar.setTitle("Creating Account");
            loadingBar.setMessage("Kindly have patience while we are creating your account..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        final DatabaseReference rootRef ;
                        rootRef = FirebaseDatabase.getInstance().getReference();
                        String id = mAuth.getCurrentUser().getUid();
                        HashMap<String , Object> facultyMap = new HashMap<>();

                        facultyMap.put("id" , id);
                        facultyMap.put("phone" ,phone);
                        facultyMap.put("Email" ,email);
                        facultyMap.put("Name" , name);
                        facultyMap.put("password" ,password);
                        facultyMap.put("Department" , dept);
                        rootRef.child("Faculty").child(id).updateChildren(facultyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                loadingBar.dismiss();

                                Toast.makeText(FacultySignUpActivity.this, "You are registered Successfully", Toast.LENGTH_SHORT).show();

                                Intent intent= new Intent(FacultySignUpActivity.this , FacultyDashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();


                            }
                        });

                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Provide all the details", Toast.LENGTH_SHORT).show();
        }
    }
}
