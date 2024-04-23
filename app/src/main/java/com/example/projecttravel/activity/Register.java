package com.example.projecttravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private TextView txtDangNhap, txtLoiMk;
    private EditText edtHo, edtTen, edtEmail, edtSDT, edtMK, edtXNMK;
    private RadioButton radNam, radNu;
    private Button btnDangKy;
    private String email, password, confirmPass;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

        txtDangNhap = findViewById(R.id.txtDangNhap);
        edtHo = findViewById(R.id.edtHo);
        edtTen = findViewById(R.id.edtTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        edtMK = findViewById(R.id.edtMK);
        edtXNMK = findViewById(R.id.edtXNMK);
        txtLoiMk = findViewById(R.id.txtLoiMK);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnDangKy = findViewById(R.id.btnDangKy);

        progressDialog = new ProgressDialog(this);

        txtDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

        edtMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoiMk.setText("");
            }
        });

        edtXNMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtLoiMk.setText("");
            }
        });
    }
    public void CreateAccount() {
        email = edtEmail.getText().toString().trim();
        password = edtMK.getText().toString().trim();
        confirmPass = edtXNMK.getText().toString().trim();
        if (Objects.equals(password, confirmPass)) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "Đăng ký tài khoản thành công! \n Vui lòng chuyển đến trang đăng nhập", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Register.this, "Đăng ký tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            txtLoiMk.setText("Xác nhận mật khẩu không trùng khớp");
        }
    }
}