package com.example.windows10.addressconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //HIde Action Bar
        getSupportActionBar().hide();

        startApp();
    }

    private void startApp(){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
    }

    private long currentTime=0L;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()- currentTime> 2000){
            currentTime= System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

}
