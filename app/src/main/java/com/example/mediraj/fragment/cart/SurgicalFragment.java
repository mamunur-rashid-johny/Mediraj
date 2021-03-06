package com.example.mediraj.fragment.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediraj.R;
import com.example.mediraj.activity.CheckoutInformation;
import com.example.mediraj.adaptar.fragadapter.SfAdapter;
import com.example.mediraj.localdb.AppDatabase;
import com.example.mediraj.localdb.SurgicalService;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;


public class SurgicalFragment extends Fragment implements SfAdapter.SfItemClick {
    AppDatabase db;
    List<SurgicalService> dataList;
    SfAdapter sfAdapter;
    SfAdapter.SfItemClick sfItemClick;
    private View view;
    private RecyclerView cartRec;
    private TextView noData;
    private ExtendedFloatingActionButton checkoutBtn;

    public SurgicalFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_diagnostic, container, false);
        db = AppDatabase.getDbInstance(getContext());
        sfItemClick = this;
        checkoutBtn = view.findViewById(R.id.checkoutBtn);
        noData = view.findViewById(R.id.noData);
        cartRec = view.findViewById(R.id.cartRec);
        cartRec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cartRec.setHasFixedSize(true);
        getItemList();

        checkoutBtn.setOnClickListener(v -> {
            getContext().startActivity(new Intent(getContext(), CheckoutInformation.class).putExtra("data", "3"));
        });
        return view;
    }

    private void getItemList() {
        dataList = db.surgicalServiceDao().getAllSurgical();
        if (dataList.size() >= 1) {
            noData.setVisibility(View.GONE);
            cartRec.setVisibility(View.VISIBLE);
            checkoutBtn.setVisibility(View.VISIBLE);
            sfAdapter = new SfAdapter(getContext(), dataList,sfItemClick);
            cartRec.setAdapter(sfAdapter);
        } else {
            cartRec.setVisibility(View.GONE);
            checkoutBtn.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getItemList();
    }

    @Override
    public void senDataSize(int size) {
        if (size==0){
            checkoutBtn.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }
}