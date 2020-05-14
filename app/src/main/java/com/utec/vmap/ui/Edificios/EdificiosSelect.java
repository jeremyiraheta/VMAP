package com.utec.vmap.ui.Edificios;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.andresoviedo.app.model3D.view.ModelActivity;

public class EdificiosSelect extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this.getContext(), ModelActivity.class);
        intent.putExtra("uri", "assets://assets/Fundadores.obj");
        intent.putExtra("immersiveMode", "true");
        intent.putExtra("backgroundColor", "0 0 0 1");
        this.startActivity(intent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
