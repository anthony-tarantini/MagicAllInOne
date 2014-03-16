package com.magicallinone.app.fragment;

import android.app.Fragment;
import android.os.Bundle;

import com.xtremelabs.imageutils.ImageLoader;

/**
 * Created by tony on 2014-03-15.
 */
public class BaseFragment extends Fragment {

    private ImageLoader mLoader;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoader = ImageLoader.buildImageLoaderForFragment(this);
    }

    protected ImageLoader getImageLoader(){
        return mLoader;
    }

    @Override
    public void onDestroy() {
        mLoader.destroy();
        super.onDestroy();
    }
}
