package com.kdu.busboristudent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kdu.busbori.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences preferences = getSharedPreferences("ChooseActivity", MODE_PRIVATE);
        String choose = preferences.getString("Choose", "");
        /*if ("Student".equals(choose)) {
            finish();
            startActivity(new Intent(this, StudentActivity.class));
        }*/
        Button Studentbutton = findViewById(R.id.button_student);
        Studentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("ChooseActivity", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Choose", "");
                editor.putString("Choose","Student");
                editor.apply();
                Intent intent_student = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent_student);
                overridePendingTransition(R.anim.slide_right, R.anim.none);
                finish();
            }
        });
    }
}