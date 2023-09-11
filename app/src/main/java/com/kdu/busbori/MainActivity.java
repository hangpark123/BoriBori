package com.kdu.busbori;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordEditText = new EditText(this);
        SharedPreferences preferences = getSharedPreferences("ChooseActivity", MODE_PRIVATE);
        String choose = preferences.getString("Choose", "");
        if ("Driver".equals(choose)) {
            finish();
            startActivity(new Intent(this, DriverActivity.class));
        } else if ("Student".equals(choose)) {
            finish();
            startActivity(new Intent(this, StudentActivity.class));
        }

        Button Driverbutton = findViewById(R.id.button_driver);
        Driverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });

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

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("관리자 코드");
        builder.setView(passwordEditText);
        builder.setPositiveButton("확인", (dialog, which) -> {
            String enteredPassword = passwordEditText.getText().toString();
            if (checkPassword(enteredPassword)) {
                SharedPreferences preferences = getSharedPreferences("ChooseActivity", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Choose", "");
                editor.putString("Choose","Driver");
                editor.apply();
                Intent intent = new Intent(MainActivity.this, DriverActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.none);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "관리자 코드가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            }
        });
        builder.setNegativeButton("취소", (dialog, which) -> {
            finish();
            startActivity(getIntent());
        });
        builder.setCancelable(false);
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });
        builder.show();
    }

    private boolean checkPassword(String enteredPassword) {
        SharedPreferences preferences = getSharedPreferences("password", Context.MODE_PRIVATE);
        String savedPassword = preferences.getString("password", "1234");

        return savedPassword.equals(enteredPassword);
    }
}