package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projecttravel.R;

public class Start extends AppCompatActivity {
    private TextView txtTieuDe;
    private Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        txtTieuDe = findViewById(R.id.txtTieuDe);
        String fullText = "Chào mừng đến với\n VieTravel";
        // Tạo một SpannableString từ văn bản đầy đủ
        SpannableString spannableString = new SpannableString(fullText);
        // Tìm vị trí của từ cần thay đổi màu
        int startIndex = fullText.indexOf("VieTravel");
        int endIndex = startIndex + "VieTravel".length();
        // Áp dụng màu cho phần văn bản cần thay đổi
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#015C92")), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Gán SpannableString cho TextView
        txtTieuDe.setText(spannableString);

        btnStart = findViewById(R.id.btnBatDau);

        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Intro.class);
                startActivity(intent);
            }
        });

    }
}