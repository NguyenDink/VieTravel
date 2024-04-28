package com.example.projecttravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projecttravel.R;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailAccount extends AppCompatActivity {

    private TextView txtFirstName, txtLastName, txtGender, txtEmail, txtPhone, btnUpdate;
    private ImageView imgBack, imgAvatar;
    AccountDB accountDB = new AccountDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_account);
        initUI();
        setVariable();
        initListener();
    }

    private void setVariable() {
        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                txtFirstName.setText(currentAccount.getFirstName().trim());
                txtLastName.setText(currentAccount.getLastName().trim());
                txtEmail.setText(currentAccount.getEmail().trim());
                txtPhone.setText(currentAccount.getPhone().trim());
                if (currentAccount.getGender() == 0) {
                    txtGender.setText("Nam");
                } else
                    txtGender.setText("Nữ");
            }
        });
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAccount.this, UpdateAccount.class);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtGender = findViewById(R.id.txtGender);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBack = findViewById(R.id.imgBack);
        imgAvatar = findViewById(R.id.imgAvatar);
    }
}