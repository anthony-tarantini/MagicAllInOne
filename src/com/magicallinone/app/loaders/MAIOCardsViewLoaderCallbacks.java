package com.magicallinone.app.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.net.Uri;

import com.magicallinone.app.callbacks.MAIOCallbacksListener;
import com.magicallinone.app.callbacks.MAIOLoaderCallbacks;
import com.magicallinone.app.providers.MAIOContentProvider;

/**
 * Created by tony on 2014-03-21.
 */
public class MAIOCardsViewLoaderCallbacks extends MAIOLoaderCallbacks {
    private static final Uri URI = MAIOContentProvider.Uris.CARDS_URI;
    private static final int LOADER_ID = 1;

    public MAIOCardsViewLoaderCallbacks(final Context context, final LoaderManager loaderManager, final MAIOCallbacksListener maioCallbacksListener) {
        super(context, loaderManager, maioCallbacksListener);
    }

    @Override
    public Uri getUri() {
        return URI;
    }

    @Override
    public int getLoaderId() {
        return LOADER_ID;
    }
}
