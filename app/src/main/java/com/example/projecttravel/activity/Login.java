package com.example.projecttravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projecttravel.R;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private TextView txtDangKy, txtLoi, txtQuenMk;
    private EditText edtEmail, edtMK;
    private Button btnDangNhap;
    String email, password;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);

        initUI();
        progressDialog = new ProgressDialog(this);
        initListener();
    }

    public void initListener() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString().trim();
                password = edtMK.getText().toString().trim();
                if (email.isEmpty()) {
                    txtLoi.setText("Vui lòng điền Email!");
                } else if (password.isEmpty()) {
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
                                        AccountDB accountDB = new AccountDB();
                                        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
                                            @Override
                                            public void onCurrentAccount(Account currentAccount) {
                                                if (!password.equals(currentAccount.getPassword())) {
                                                    accountDB.updatePassword(password, new AccountDB.UpdateAccountCallback() {
                                                        @Override
                                                        public void onUpdateSuccess() {

                                                        }

                                                        @Override
                                                        public void onUpdateFailure(String errorMessage) {

                                                        }
                                                    });
                                                }
                                            }
                                        });
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

        txtQuenMk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
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

    public void initUI() {
        txtDangKy = findViewById(R.id.txtDangKy);
        edtEmail = findViewById(R.id.edtEmail);
        edtMK = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtLoi = findViewById(R.id.txtLoi);
        txtLoi.setText("");
        txtQuenMk = findViewById(R.id.txtQuenMK);
    }

    public void resetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        EditText emailBox = dialogView.findViewById(R.id.edtEmail);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(Login.this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem email có tồn tại trong Firebase không
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Account");
                userRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Gửi email đặt lại mật khẩu
                            FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Login.this, "Kiểm tra email của bạn để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(Login.this, "Gửi không thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Login.this, "Email chưa được đăng ký", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Login.this, "Lỗi khi truy vấn dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        dialog.show();
    }
}