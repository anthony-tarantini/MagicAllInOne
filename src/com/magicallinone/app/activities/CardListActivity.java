package com.magicallinone.app.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.magicallinone.app.R;
import com.magicallinone.app.fragment.CardListFragment;
import com.magicallinone.app.listeners.OnCardSelectedListener;
import com.magicallinone.app.managers.FontManager;
import com.magicallinone.app.services.ApiService;

public class CardListActivity extends BaseFragmentActivity implements
		OnCardSelectedListener {

	public static final class Extras {
		public static final String SET = "set";
		public static final String DECK_ID = "deck_id";
		public static final String REQUEST_CODE = "request_code";
	}

	private String mSetId;
	private AlertDialog mAlertDialog;
	private int mDeckId;
	private int mRequestCode;

	public static void newInstance(final Context context, final String set) {
		final Intent intent = new Intent(context, CardListActivity.class);
		intent.putExtra(Extras.SET, set);
		context.startActivity(intent);
	}

	public static void newInstanceForResult(final Activity activity,
			final String set, final int deckId, final int requestCode) {
		final Intent intent = new Intent(activity, CardListActivity.class);
		intent.putExtra(Extras.SET, set);
		intent.putExtra(Extras.DECK_ID, deckId);
		intent.putExtra(Extras.REQUEST_CODE, requestCode);
		activity.startActivityForResult(intent,
				RequestCodes.ADD_CARD_SET_REQUEST);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);

		mSetId = getIntent().getStringExtra(Extras.SET);
		mDeckId = getIntent().getIntExtra(Extras.DECK_ID, -1);
		mRequestCode = getIntent().getIntExtra(Extras.REQUEST_CODE, -1);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		CardListFragment fragment = CardListFragment.newInstance(mSetId);
		fragment.setOnCardSelectedListener(this);
		fragmentTransaction.add(R.id.content_frame, fragment,
				CardListFragment.class.getCanonicalName());
		fragmentTransaction.commit();

		ActionBar supportActionBar = getActionBar();
		supportActionBar.setDisplayHomeAsUpEnabled(true);
		supportActionBar.setHomeButtonEnabled(true);
	}

	@Override
	public void onCardSelected(int cardId) {
		if (mRequestCode == RequestCodes.ADD_CARD_SET_REQUEST)
			showNewDeckDialog(cardId);
	}

	private void showNewDeckDialog(final int cardId) {
		LayoutInflater inflater = this.getLayoutInflater();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = inflater.inflate(R.layout.dialog_add_card, null);
		final EditText countEditText = ((EditText) view
				.findViewById(R.id.dialog_add_card_count));
		countEditText.setTypeface(FontManager.INSTANCE.getAppFont());
		Button cancelButton = (Button) view
				.findViewById(R.id.dialog_add_card_cancel);
		cancelButton.setTypeface(FontManager.INSTANCE.getAppFont());
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAlertDialog != null) {
					mAlertDialog.dismiss();
				}
			}
		});
		Button submitButton = (Button) view
				.findViewById(R.id.dialog_add_card_submit);
		submitButton.setTypeface(FontManager.INSTANCE.getAppFont());
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();
				addNewCard(cardId,
						Integer.parseInt(countEditText.getText().toString()));
				finish();
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	private void addNewCard(int cardId, int quantity) {
		Intent intent = new Intent(this, ApiService.class);
		intent.putExtra(ApiService.Extras.OPERATION,
				ApiService.Operations.ADD_CARD);
		intent.putExtra(ApiService.Extras.DECK_ID, mDeckId);
		intent.putExtra(ApiService.Extras.CARD_ID, cardId);
		intent.putExtra(ApiService.Extras.QUANTITY, quantity);
		startService(intent);
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		super.finish();
	}
}
