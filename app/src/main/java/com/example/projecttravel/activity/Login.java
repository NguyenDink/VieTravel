package com.example.projecttravel.activity;

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

import com.example.projecttravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextView txtDangKy, txtLoi;
    private EditText edtEmail, edtMK;
    private Button btnDangNhap;
    String email, password;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        txtDangKy = findViewById(R.id.txtDangKy);
        edtEmail = findViewById(R.id.edtEmail);
        edtMK = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtLoi = findViewById(R.id.txtLoi);
        txtLoi.setText("");

        progressDialog = new ProgressDialog(this);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString().trim();
                password = edtMK.getText().toString().trim();
                if (email == null) {
                    txtLoi.setText("Vui lòng điền Email!");
                } else if (password == null) {
                    txtLoi.setText("VUi lòng điền mật khẩu");
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Đăng nhập thành công.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, Home.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(Login.this, "Thông tin tài khoản hoặc mật khẩu không chính xác!!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        edtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoi.setText("");
            }
        });

        edtMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoi.setText("");
            }
        });

        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });
    }
}