package com.gc.kingka;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.modules.common.CustomHandler;
import com.gc.modules.common.Custom_SharedPreferences;
import com.gc.modules.common.Globar;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText id;
    private EditText pw;
    private TextView loginBt;
    private ImageView closeBt;

    //handler
    private CustomHandler customHandler;
    //share
    private Custom_SharedPreferences csp;
    private Globar g;

    private void viewReSet() {

        this.customHandler = new CustomHandler(this);
        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);

        this.id = findViewById(R.id.login_id_et);
        this.pw = findViewById(R.id.login_pw_et);
        this.loginBt = findViewById(R.id.lgoin_loginBt);
        this.closeBt = findViewById(R.id.login_closeBt);
    }

    private void listenerRegister() {
        this.loginBt.setOnClickListener(this);
        this.closeBt.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.viewReSet();
        this.listenerRegister();
    }

    private void login() {

        if (this.id.getText().toString().equals("test") && this.pw.getText().toString().equals("test")) {
            this.g.msg("로그인 되었습니다.");
            this.csp.put("name",this.id.getText().toString());
            this.csp.put("isLogin",true);
            finish();
            overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
        } else {
            this.g.baseAlertMessage("로그인","아이디와 비밀번호를 확인해 주세요");
            return;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (id.isFocused()) {
                Rect outRect = new Rect();
                id.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    id.clearFocus();
                    //
                    // Hide keyboard
                    //
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else if (pw.isFocused()) {
                Rect outRect = new Rect();
                pw.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    pw.clearFocus();
                    //
                    // Hide keyboard
                    //
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lgoin_loginBt:
                this.login();
                break;
            case R.id.login_closeBt:
                finish();
                overridePendingTransition(0,R.anim.anim_slide_out_bottom_login);
                break;
        }

    }
}
