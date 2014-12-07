package it.demo.twitterlike.android.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class CallbackDialogFragment extends DialogFragment {

	public static String CODE_ARGUMENT = "code";

	public static String TITLE_ARGUMENT = "title";

	public static String TITLE_TEXT_ARGUMENT = "titleText";

	public static String MESSAGE_ARGUMENT = "message";

	public static String MESSAGE_TEXT_ARGUMENT = "messageText";

	public static String POSITIVEBUTTON_ARGUMENT = "positiveButton";
	public static String NEGATIVEBUTTON_ARGUMENT = "negativeButton";

	public static String NEUTRALBUTTON_ARGUMENT = "neutralButton";

	public static String ARGUMENTS_ARGUMENT = "arguments";

	private int code;

	private Integer title;

	private String titleText;

	private Integer message;

	private String messageText;

	private Integer positiveButton;

	private Integer negativeButton;

	private Integer neutralButton;

	private Object[] arguments;

	private YesNoListener listener;

	public CallbackDialogFragment() {

	}

	@Override
	public void setArguments(Bundle bundle) {
		this.code = bundle.getInt(CODE_ARGUMENT);

		this.title = (Integer) bundle.getSerializable(TITLE_ARGUMENT);
		this.titleText = (String) bundle.getSerializable(TITLE_TEXT_ARGUMENT);
		this.title = (Integer) bundle.getSerializable(TITLE_ARGUMENT);
		this.message = (Integer) bundle.getSerializable(MESSAGE_ARGUMENT);
		this.messageText = (String) bundle
				.getSerializable(MESSAGE_TEXT_ARGUMENT);
		this.positiveButton = (Integer) bundle
				.getSerializable(POSITIVEBUTTON_ARGUMENT);
		this.negativeButton = (Integer) bundle
				.getSerializable(NEGATIVEBUTTON_ARGUMENT);
		this.neutralButton = (Integer) bundle
				.getSerializable(NEUTRALBUTTON_ARGUMENT);
		this.arguments = (Object[]) bundle.getSerializable(ARGUMENTS_ARGUMENT);

	}

	public static CallbackDialogFragment createInstance(int code, String title,
			String message, Integer positiveButton, Integer negativeButton,
			Integer neutralButton, Object... arguments) {
		Bundle bundle = new Bundle();

		bundle.putInt(CODE_ARGUMENT, code);
		bundle.putSerializable(TITLE_TEXT_ARGUMENT, title);
		bundle.putSerializable(MESSAGE_TEXT_ARGUMENT, message);
		bundle.putSerializable(POSITIVEBUTTON_ARGUMENT, positiveButton);
		bundle.putSerializable(NEGATIVEBUTTON_ARGUMENT, negativeButton);
		bundle.putSerializable(NEUTRALBUTTON_ARGUMENT, neutralButton);
		bundle.putSerializable(ARGUMENTS_ARGUMENT, arguments);
		CallbackDialogFragment result = new CallbackDialogFragment();
		result.setArguments(bundle);

		return result;
	}

	public static CallbackDialogFragment createInstance(int code,
			Integer title, Integer message, Integer positiveButton,
			Integer negativeButton, Integer neutralButton, Object... arguments) {
		Bundle bundle = new Bundle();

		bundle.putInt(CODE_ARGUMENT, code);
		bundle.putSerializable(TITLE_ARGUMENT, title);
		bundle.putSerializable(MESSAGE_ARGUMENT, message);
		bundle.putSerializable(POSITIVEBUTTON_ARGUMENT, positiveButton);
		bundle.putSerializable(NEGATIVEBUTTON_ARGUMENT, negativeButton);
		bundle.putSerializable(NEUTRALBUTTON_ARGUMENT, neutralButton);
		bundle.putSerializable(ARGUMENTS_ARGUMENT, arguments);
		CallbackDialogFragment result = new CallbackDialogFragment();
		result.setArguments(bundle);

		return result;
	}

	public interface YesNoListener {
		void onYes(int code, Object... args);

		void onNo(int code, Object... args);

		void onNeutral(int code, Object... args);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof YesNoListener)) {
			throw new ClassCastException(activity.toString()
					+ " must implement YesNoListener");
		}

		this.listener = (YesNoListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			try {
				getDialog().setOnDismissListener(null);
			} catch (Exception e) {
			}
		super.onDestroyView();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder result = new AlertDialog.Builder(getActivity());

		if (title != null) {
			result.setTitle(title);
		} else {
			if (titleText != null) {
				result.setTitle(titleText);
			}
		}

		if (message != null) {
			result.setMessage(message);
		} else {
			if (messageText != null) {
				result.setMessage(messageText);
			}
		}

		if (positiveButton != null) {
			result.setPositiveButton(positiveButton,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (listener != null) {
								listener.onYes(code, arguments);
							}
						}
					});
		}
		if (negativeButton != null) {
			result.setNegativeButton(negativeButton,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (listener != null) {
								listener.onNo(code, arguments);
							}
						}
					});
		}
		if (neutralButton != null) {
			result.setNeutralButton(neutralButton,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (listener != null) {
								listener.onNeutral(code, arguments);
							}
						}
					});
		}
		return result.create();
	}
}
