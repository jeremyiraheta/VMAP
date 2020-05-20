package com.utec.vmap.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.utec.vmap.R;
import com.utec.vmap.ui.Edificios.MSV;
import com.utec.vmap.ui.Edificios.SLoader;

public class HomeFragment extends Fragment {
    private MSV gLView;

    private SLoader scene;

    private Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Mapa.obj"));
        scene.init();
        try{
            gLView = new MSV(this.getActivity(),scene);
            scene.setGLView(gLView);
        }catch (Exception ex)
        {
            Log.println(Log.ERROR,"",ex.getMessage()) ;
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(paused)
        {
            scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Mapa.obj"));
            scene.init();
            try{
                gLView = new MSV(this.getActivity(),scene);
            }catch (Exception ex)
            {

            }
            paused=false;
        }
    }
    private boolean paused=false;
    @Override
    public void onPause() {
        super.onPause();
        paused=true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return gLView;
    }
}