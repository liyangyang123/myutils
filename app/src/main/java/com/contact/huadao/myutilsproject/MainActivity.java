package com.contact.huadao.myutilsproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyy.utils.logutils.MLogUtils;
import com.lyy.utils.myviewioc.ViewById;
import com.lyy.utils.myviewioc.ViewClick;
import com.lyy.utils.myviewioc.ViewUtiles;

public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.btn_test)
    Button btn_test;
    @ViewById(R.id.tv_test)
    TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtiles.inject(this);
    }
    @ViewClick({R.id.btn_test,R.id.tv_test})
    public void Click(View view){
        switch (view.getId()){
            case R.id.btn_test:
                MLogUtils.i("-------Click------");
                break;
            case R.id.tv_test:
                break;
        }

    }

}
