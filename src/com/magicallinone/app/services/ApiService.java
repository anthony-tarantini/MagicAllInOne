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
import com.magicallinone.app.models.Sets;
import com.magicallinone.app.providers.MagicContentProvider;

public class ApiService extends IntentService {
	private static final String SERVICE = "ApiService";

	public static final class Actions {
		public static final String SETS_LOAD = "sets_load";
		public static final String SET_READ = "set_read";
	}

	public static final class Extras {
		public static final String OPERATION = "operation";
		public static final String QUERY = "query";
	}

	public static final class Operations {
		public static final int SET_LIST = 1;
		public static final int SINGLE_SET = 2;
	}

	public static final String SETS = "sets/";
	public static final String JSON = ".json";

	private String mQuery;
	private int mOperation;
	private AssetManager mAssetManager;

	public ApiService() {
		super(SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mQuery = intent.getStringExtra(Extras.QUERY);
		mOperation = intent.getIntExtra(Extras.OPERATION, -1);
		mAssetManager = MagicApplication.getContext().getAssets();
		final ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		switch (mOperation) {
		case Operations.SINGLE_SET:
			readSingleSet(operations);
			break;
		case Operations.SET_LIST:
			readSetList(operations);
		default:
			break;
		}
		performOperations(operations);
		sendIntent();
	}

	private void readSingleSet(
			final ArrayList<ContentProviderOperation> operations) {
		try {
			InputStream inputStream = mAssetManager.open(SETS + mQuery + JSON);
			Type setType = new TypeToken<Set>() {
			}.getType();
			Set set = MagicApplication.getParser().fromJson(
					new InputStreamReader(inputStream), setType);
			ContentProviderOperation operation;
			final ContentValues setValues = SetTable.getContentValues(set);
			operation = ContentProviderOperation
					.newInsert(MagicContentProvider.Uris.SET_URI)
					.withValues(setValues).build();
			operations.add(operation);
			for (Card card : set.cards) {
				ContentValues cardValues = CardTable.getContentValues(card);
				operation = ContentProviderOperation
						.newInsert(MagicContentProvider.Uris.CARD_URI)
						.withValues(cardValues).build();
				operations.add(operation);
				ContentValues setCard = new ContentValues();
				setCard.put(SetCardTable.Columns.SET_CODE, set.code);
				setCard.put(SetCardTable.Columns.CARD_ID, card.multiverseid);
				operation = ContentProviderOperation
						.newInsert(MagicContentProvider.Uris.SET_CARD_URI)
						.withValues(setCard).build();
				operations.add(operation);
			}
		} catch (IOException exception) {
			Log.e(ApiService.class.getCanonicalName(), exception.getMessage(),
					exception);
		}
	}

	private void readSetList(
			final ArrayList<ContentProviderOperation> operations) {
		try {
			InputStream inputStream = mAssetManager.open(SETS + mQuery + JSON);
			Type setType = new TypeToken<Sets>() {
			}.getType();
			Sets sets = MagicApplication.getParser().fromJson(
					new InputStreamReader(inputStream), setType);
			ContentProviderOperation operation;
			for (Set set : sets.sets) {
				final ContentValues setValues = SetTable.getContentValues(set);
				operation = ContentProviderOperation
						.newInsert(MagicContentProvider.Uris.SET_URI)
						.withValues(setValues).build();
				operations.add(operation);
			}
		} catch (IOException exception) {
			Log.e(ApiService.class.getCanonicalName(), exception.getMessage(),
					exception);
		}
	}

	private void performOperations(
			final ArrayList<ContentProviderOperation> operations) {
		final ContentResolver resolver = getContentResolver();
		try {
			resolver.applyBatch(MagicContentProvider.AUTHORITY, operations);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			e.printStackTrace();
		}
	}

	private void sendIntent() {
		Intent broadcastIntent = new Intent();
		switch(mOperation){
		case Operations.SINGLE_SET:
			broadcastIntent.setAction(Actions.SET_READ);
			break;
		case Operations.SET_LIST:
			broadcastIntent.setAction(Actions.SETS_LOAD);
			break;
		}
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra(Extras.QUERY, mQuery);
		sendBroadcast(broadcastIntent);
	}
}
