package com.magicallinone.app.dialogfragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.magicallinone.app.R;
import com.magicallinone.app.utils.ImageUtils;
import com.xtremelabs.imageutils.ImageLoader;

public class CardDetailDialogFragment extends BaseDialogFragment {

	public static final class Arguments {
		public static final String SET_ID = "set_id";
		public static final String CARD_NUMBER = "card_number";
	}
	
	public static CardDetailDialogFragment newInstance(String setId, int cardNumber){
		final CardDetailDialogFragment fragment = new CardDetailDialogFragment();
		
		Bundle args = fragment.getArguments();
		if (args == null){
			args = new Bundle();
		}
		
		args.putString(Arguments.SET_ID, setId);
		args.putInt(Arguments.CARD_NUMBER, cardNumber);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private String mSetId;
	private int mCardNumber;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSetId = getArguments().getString(Arguments.SET_ID);
		mCardNumber = getArguments().getInt(Arguments.CARD_NUMBER);
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialog_fragment_card_details, container, false);
		
		final ImageView imageView = (ImageView) view.findViewById(R.id.dialog_fragment_card_details_image);
        final ImageLoader imageLoader = getImageLoader();
        final String imageUrl = ImageUtils.getImageUrl(mSetId, mCardNumber);
        imageLoader.loadImage(imageView, imageUrl);
		
		return view;
	}
}
