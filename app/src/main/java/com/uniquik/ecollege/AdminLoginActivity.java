package com.uniquik.ecollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText InputUserName , InputPassword;
    private Button login;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        InputUserName = findViewById(R.id.admin_username);
        InputPassword = findViewById(R.id.admin_password);
        login = findViewById(R.id.admin_login);
        loadingBar = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });




    }

    private void LoginUser() {

        String username = InputUserName.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please enter your username below", Toast.LENGTH_SHORT).show();
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

            AllowAccessToAccount(username,password);

        }

    }

    private void AllowAccessToAccount(final String username,final String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot)
            {
                if(datasnapshot.child("Admins").child(username).exists())

                {


                    Admins userData = datasnapshot.child ("Admins").child(username).getValue(Admins.class);
                    if(userData.getUsername().equals(username))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            Toast.makeText(AdminLoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                            Intent intent = new Intent(AdminLoginActivity.this , AdminMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(AdminLoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {
                    Toast.makeText(AdminLoginActivity.this, "Account with " + username + " doesn't exists,Please Contact to the Admin", Toast.LENGTH_SHORT).show();

                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

