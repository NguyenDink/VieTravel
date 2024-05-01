package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.activity.Register;
import com.example.projecttravel.activity.UpdateAccount;
import com.example.projecttravel.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AccountDB {

    private DatabaseReference myRef;
    FirebaseUser currentUser;
    private String account_id;
    public AccountDB() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            account_id = currentUser.getUid();
        }
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

    public void updateAccount(String firstName, String lastName, String phone, int gender, UpdateAccountCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("phone", phone);
        updates.put("gender", gender);
        myRef.child(account_id).updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    callback.onUpdateSuccess();
                } else {
                    callback.onUpdateFailure(error.getMessage());
                }
            }
        });

    }

    public void updatePassword(String password, UpdateAccountCallback callback) {
        myRef.child(account_id).child("password").setValue(password, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    callback.onUpdateSuccess();
                } else {
                    callback.onUpdateFailure(error.getMessage());
                }
            }
        });
    }

    public void updateRole(int role, UpdateAccountCallback callback) {
        myRef.child(account_id).child("role").setValue(role, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    callback.onUpdateSuccess();
                } else {
                    callback.onUpdateFailure(error.getMessage());
                }
            }
        });
    }

    public interface UpdateAccountCallback {
        void onUpdateSuccess();
        void onUpdateFailure(String errorMessage);
    }


    public void getCurrentAccount(CurrentAccountCallBack callBack) {
        myRef.orderByKey().equalTo(account_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Account account = dataSnapshot.getValue(Account.class);
                        if (account != null) {
                            callBack.onCurrentAccount(account);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface CurrentAccountCallBack {
        void onCurrentAccount(Account currentAccount);
    }
}
