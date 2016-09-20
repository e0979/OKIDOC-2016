package com.colmenadeideas.okidoc.includes.config.libs.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.colmenadeideas.okidoc.R;


public class CustomAlertBox extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public int layoutToLoad;
    public Class activityClass;

    public CustomAlertBox(Activity a, int b, Class c) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.layoutToLoad = b;
        this.activityClass = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(layoutToLoad);
        yes = (Button) findViewById(R.id.btn_yes);
        yes.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Intent intent = new Intent(c, activityClass);
                c.startActivity(intent);
                c.finish();
                break;
        }
        dismiss();
    }
}