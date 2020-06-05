package com.utec.vmap.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.utec.vmap.R;
import com.utec.vmap.Util;

public class LoginFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        if(!Util.Load(getActivity(),"carnet").equals(""))
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.nav_profile);
        return root;
    }
}