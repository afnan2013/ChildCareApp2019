package com.example.childcareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.childcareapp.ChildQRCodeScanActivity;
import com.example.childcareapp.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Anonymous on 6/7/2019.
 */

public class EntryChildAdminFragment extends Fragment {
    Button btn_entry, btn_exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_child_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_entry = view.findViewById(R.id.btn_entry_child);

        btn_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChildQRCodeScanActivity.class);
                intent.putExtra("activity", "entry");
                startActivity(intent);
            }
        });


    }

}
