package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.activity.Register;
import com.example.projecttravel.model.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDB {

    private DatabaseReference myRef;
    public AccountDB() {
        myRef = FirebaseDatabase.getInstance().getReference("Account");
    }

    public void addAccount(Context context, Account account, String account_id) {
        myRef.child(account_id).setValue(account, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Đăng ký tài khoản thành công! \n Vui lòng chuyển đến trang đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
