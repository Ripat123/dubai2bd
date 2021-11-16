package com.sbitbd.dubai2bd.ui.seller_reg;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbitbd.dubai2bd.R;

public class seller_reg extends Fragment {

    private SellerRegViewModel mViewModel;

    public static seller_reg newInstance() {
        return new seller_reg();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seller_reg_fragment, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SellerRegViewModel.class);
        // TODO: Use the ViewModel
    }

}