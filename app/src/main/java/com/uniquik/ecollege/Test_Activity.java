package com.uniquik.ecollege;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Test_Activity extends AppCompatActivity {
    private Button click_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        click_button = findViewById(R.id.btn_test);

        click_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Test_Activity.this,Test_Activity2.class);
                startActivity(intent);
            }
        });
    }


}