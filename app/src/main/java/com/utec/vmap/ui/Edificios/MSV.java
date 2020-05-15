package com.utec.vmap.ui.Edificios;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


import java.io.IOException;

public class MSV extends GLSurfaceView {
    private Activity parent;
    private MR mRenderer;
    private TC touchHandler;
    private SLoader scene;
    public  MSV(Activity activity, SLoader scene) throws IllegalAccessException, IOException
    {
        super(activity);
        // parent component
        this.parent = activity;
        this.scene = scene;
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // This is the actual renderer of the 3D space
        mRenderer = new MR(this);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        // TODO: enable this?
        // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        touchHandler = new TC(this, mRenderer);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchHandler.onTouchEvent(event);
    }

    public SLoader getScene() {
        return scene;
    }

    public MR getModelRenderer() {
        return mRenderer;
    }
}
