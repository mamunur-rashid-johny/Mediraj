package com.example.mediraj.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediraj.R;
import com.example.mediraj.adaptar.ServiceAdapter;
import com.example.mediraj.adaptar.imageslider.ImageSliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ServiceAdapter.ServiceInterface {
    public static final String TAG = HomeActivity.class.getName();
    RecyclerView ser_rec;
    List<String> titles;
    List<Integer> images;
    ServiceAdapter serviceAdapter;
    ServiceAdapter.ServiceInterface serviceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLoader();

        serviceInterface = this;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.history) {
                startActivity(new Intent(this, OrderActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.cart) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.more) {
                startActivity(new Intent(getApplicationContext(), MoreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });

        //add item to the list
        titles = new ArrayList<>();
        images = new ArrayList<>();


        titles.add(getString(R.string.medi_service));
        titles.add(getString(R.string.da));
        titles.add(getString(R.string.ds));
        titles.add(getString(R.string.clinic_service));
        titles.add(getString(R.string.bb));
        titles.add(getString(R.string.hp));
        titles.add(getString(R.string.md));
        titles.add(getString(R.string.online_doctor));

        images.add(R.drawable.ic_capsule);
        images.add(R.drawable.ic_doctor1);
        images.add(R.drawable.ic_diagonistic);
        images.add(R.drawable.ic_patient);
        images.add(R.drawable.ic_blood_bank);
        images.add(R.drawable.ic_pathology_1);
        images.add(R.drawable.ic_surgical1);
        images.add(R.drawable.ic_doctor);


        //initialize views
        ser_rec = findViewById(R.id.sec_rec);
        ser_rec.setOverScrollMode(View.OVER_SCROLL_NEVER);
        ser_rec.setLayoutManager(new GridLayoutManager(this, 2));
        ser_rec.setHasFixedSize(true);
        serviceAdapter = new ServiceAdapter(this, titles, images, serviceInterface);
        ser_rec.setAdapter(serviceAdapter);


    }


    @Override
    public void onClickInterface(String serName) {
        if (serName.equals(getString(R.string.medi_service))) {
            Intent intent = new Intent(HomeActivity.this, MedicineService.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.clinic_service))) {
            Intent intent = new Intent(HomeActivity.this, ClinicService.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.bb))) {
            Intent intent = new Intent(HomeActivity.this, BloodbankService.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.ds))) {
            Intent intent = new Intent(HomeActivity.this, DiagnosticActivity.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.md))) {
            Intent intent = new Intent(HomeActivity.this, SurgicalActivity.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.hp))) {
            Intent intent = new Intent(HomeActivity.this, HomePathologyActivity.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.da))) {
            Intent intent = new Intent(HomeActivity.this, DoctorListActivity.class);
            startActivity(intent);
        } else if (serName.equals(getString(R.string.online_doctor))) {
            Intent intent = new Intent(HomeActivity.this, EmergencyDoctorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to exit?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert11 = alertDialog.create();
        alert11.show();
    }

    //section for image slider api call
    private void imageLoader() {
        int[] img = {R.drawable.mediraj, R.drawable.mediraj_one, R.drawable.mediraj_two};
        SliderView sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new ImageSliderAdapter(this, img));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.DROP);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4);
        sliderView.startAutoCycle();
    }


}