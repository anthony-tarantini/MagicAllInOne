package com.magicallinone.app.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.AssetManager;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.magicallinone.app.application.MagicApplication;
import com.magicallinone.app.datasets.CardTable;
import com.magicallinone.app.datasets.SetCardTable;
import com.magicallinone.app.datasets.SetTable;
import com.magicallinone.app.models.Card;
import com.magicallinone.app.models.Set;
import com.magicallinone.app.providers.MagicContentProvider;

public class ApiService extends IntentService {
	private static final String SERVICE = "ApiService";

	public static final class Actions {
		public static final String READ_DONE = "read_done";
	}
	
	public static final class Extras {
		public static final String SET = "set";
	}	
			
	private String mSet;
	
	public ApiService(){
		super(SERVICE);
	}
	
	@Override
	protected void onHandleIntent(Intent intent){
		mSet = intent.getStringExtra(Extras.SET);
		AssetManager assetManager = MagicApplication.getContext().getAssets();
		try {
			final ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
			InputStream inputStream = assetManager.open("sets/" + mSet +".json");
			Type setType = new TypeToken<Set>(){}.getType();
			Set set = MagicApplication.getParser().fromJson(new InputStreamReader(inputStream), setType);
			ContentProviderOperation operation;
			final ContentValues setValues = SetTable.getContentValues(set);
			Log.d("TESTING", set.name);
			operation = ContentProviderOperation.newInsert(MagicContentProvider.Uris.SET_URI).withValues(setValues).build();
			operations.add(operation);
			for (Card card : set.cards){
				ContentValues cardValues = CardTable.getContentValues(card);
				operation = ContentProviderOperation.newInsert(MagicContentProvider.Uris.CARD_URI).withValues(cardValues).build();
				operations.add(operation);
				ContentValues setCard = new ContentValues();
				setCard.put(SetCardTable.Columns.SET_CODE, set.code);
				setCard.put(SetCardTable.Columns.CARD_ID, card.multiverseid);
				operation = ContentProviderOperation.newInsert(MagicContentProvider.Uris.SET_CARD_URI).withValues(setCard).build();
				operations.add(operation);
			}
			final ContentResolver resolver = getContentResolver();
			resolver.applyBatch(MagicContentProvider.AUTHORITY, operations);
			sendIntent();
		} catch(IOException exception){
			Log.e(ApiService.class.getCanonicalName(), exception.getMessage(), exception);
		} catch (RemoteException exception) {
			Log.e(ApiService.class.getCanonicalName(), exception.getMessage(), exception);
		} catch (OperationApplicationException exception) {
			Log.e(ApiService.class.getCanonicalName(), exception.getMessage(), exception);
		} 
	}
	
	private void sendIntent(){
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(Actions.READ_DONE);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(Extras.SET, mSet);
		sendBroadcast(broadcastIntent);
		
	}
}
