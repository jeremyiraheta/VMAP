package com.utec.vmap.ui.Edificios;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.andresoviedo.app.model3D.demo.SceneLoader;
import org.andresoviedo.app.model3D.view.ModelActivity;
import org.andresoviedo.app.model3D.view.ModelSurfaceView;
import org.andresoviedo.util.android.ContentUtils;

import java.io.IOException;

public class EdificiosSelect extends Fragment {
    private static final int REQUEST_CODE_LOAD_TEXTURE = 1000;

    private MSV gLView;

    private SLoader scene;

    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this.getContext(), ModelActivity.class);
        intent.putExtra("uri", "assets://assets/models/Fundadores.obj");
        intent.putExtra("immersiveMode", "true");
        intent.putExtra("backgroundColor", "0 0 0 1");
        this.startActivity(intent);
       /* handler = new Handler(this.getActivity().getMainLooper());
        scene = new SLoader(this.getActivity(),Uri.parse("assets://assets/Fundadores.obj"));
        scene.init();
        try{
            gLView = new MSV(this.getActivity(),scene);
            scene.setGLView(gLView);
        }catch (Exception ex)
        {
            Log.println(Log.ERROR,"",ex.getMessage()) ;
            return;
        }*/
    }

   /* @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return gLView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_LOAD_TEXTURE:
                // The URI of the selected file
                final Uri uri = data.getData();
                if (uri != null) {
                    Log.i("ModelActivity", "Loading texture '" + uri + "'");
                    try {
                        ContentUtils.setThreadActivity(this.getActivity());
                        scene.loadTexture(null, uri);
                    } catch (IOException ex) {
                        Log.e("ModelActivity", "Error loading texture: " + ex.getMessage(), ex);
                    } finally {
                        ContentUtils.setThreadActivity(null);
                    }
                }
        }
    }*/
}
