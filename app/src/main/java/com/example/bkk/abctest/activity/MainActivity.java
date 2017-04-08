package com.example.bkk.abctest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bkk.abctest.R;
import com.example.bkk.abctest.fragment.RegistrationFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, RegistrationFragment.newInstance(), "RegistrationFragment")
                    .commit();
        }
    }

}
