package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.projecttravel.R;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.model.Account;

public class UpdateAccount extends AppCompatActivity {
    private EditText edtFirstName, edtLastName, edtPhone;
    private RadioButton radNam, radNu;
    private Button btnUpdate;
    AccountDB accountDB = new AccountDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initUI();
        setVariable();
        initListener();
    }

    private void initListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = edtFirstName.getText().toString().trim();
                String lastName = edtLastName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                int gender;
                if (radNam.isChecked())
                    gender = 0;
                else gender = 1;
                if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(UpdateAccount.this, "Vui lòng không bỏ trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    accountDB.updateAccount(firstName, lastName, phone, gender, new AccountDB.UpdateAccountCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(UpdateAccount.this, "Cập nhật thông tin tài khoản thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onUpdateFailure(String errorMessage) {
                            Toast.makeText(UpdateAccount.this, "Lỗi khi cập nhật thông tin tài khoản: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setVariable() {
        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                edtFirstName.setText(currentAccount.getFirstName().trim());
                edtLastName.setText(currentAccount.getLastName().trim());
                edtPhone.setText(currentAccount.getPhone().trim());
                if (currentAccount.getGender() == 0) {
                    radNam.setChecked(true);
                } else
                    radNu.setChecked(true);
            }
        });
    }


    private void initUI() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtPhone = findViewById(R.id.edtPhone);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnUpdate = findViewById(R.id.btnUpdate);
    }
}