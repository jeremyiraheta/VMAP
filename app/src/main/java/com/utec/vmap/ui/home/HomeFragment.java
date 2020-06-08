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
import com.utec.vmap.Util;
import com.utec.vmap.ui.Edificios.MSV;
import com.utec.vmap.ui.Edificios.SLoader;

import org.andresoviedo.android_3d_model_engine.model.Object3D;
import org.andresoviedo.android_3d_model_engine.model.Object3DData;
import org.andresoviedo.android_3d_model_engine.services.Object3DBuilder;

import java.util.Random;

public class HomeFragment extends Fragment {
    private MSV gLView;

    private SLoader scene;

    private Handler handler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(), Uri.parse("assets://assets/models/Mapa.obj"));
        scene.addObject(Object3DBuilder.buildAxis());
        scene.setCameraAnimation(false);
        scene.init(new Thread(new Runnable() {
            @Override
            public void run() {
                while(scene != null)
                {
                    try{
                        Util.LoadLocations(scene);
                        Thread.sleep(30000);
                    }catch (Exception ex)
                    {
                    }
                }
            }
        }));
        scene.getCamera().xPos = 0.9263429f;
        scene.getCamera().yPos = 1.9843282f;
        scene.getCamera().zPos = 1.7422535f;
        scene.getCamera().xUp = -0.053720906f;
        scene.getCamera().yUp = 0.6728751f;
        scene.getCamera().zUp = -0.73780364f;
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