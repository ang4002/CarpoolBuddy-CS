package com.example.carpoolbuddy.userInfoActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carpoolbuddy.R;

public class UserInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spinner;
    FrameLayout fragmentDisplay;
    Parent parentFragment;
    Student studentFragment;
    Alumni alumniFragment;
    Teacher teacherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        spinner = findViewById(R.id.roleSpinner);
        fragmentDisplay = findViewById(R.id.fragmentDisplay);
        parentFragment = new Parent();
        studentFragment = new Student();
        alumniFragment = new Alumni();
        teacherFragment = new Teacher();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(i) {
            case 0:
                setFragment(parentFragment);
                break;
            case 1:
                setFragment(studentFragment);
                break;
            case 2:
                setFragment(teacherFragment);
                break;
            case 3:
                setFragment(alumniFragment);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentDisplay, fragment);
        fragmentTransaction.commit();
    }
}