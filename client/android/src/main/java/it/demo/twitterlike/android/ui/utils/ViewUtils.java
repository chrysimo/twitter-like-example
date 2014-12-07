package it.demo.twitterlike.android.ui.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ViewUtils {

	private ViewUtils() {

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void openKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void closeKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		closeKeyboard(imm, view);
	}

	public static void closeKeyboard(InputMethodManager imm, View view) {
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static boolean populateTextView(View root, int id, String message) {
		return populateTextView(root.findViewById(id), message);

	}

	public static boolean populateTextView(View view, String message) {
		if (view != null && view instanceof TextView) {
			return populateTextView((TextView) view, message);
		}
		return false;
	}

	public static boolean populateTextView(TextView textView, String message) {
		if (textView != null) {
			textView.setText(message);
			return true;
		}
		return false;
	}

	public static void removeParentView(View view) {
		if (view != null) {
			ViewParent viewParent = view.getParent();
			if (viewParent != null && viewParent instanceof ViewGroup) {
				((ViewGroup) viewParent).removeView(view);
			}
		}
	}

	public static View getFirstViewByTag(View root, Object tag) {
		View result = null;

		if (root != null) {
			result = root.findViewWithTag(tag);
			if (result == null && root instanceof ViewGroup) {
				ViewGroup groupView = (ViewGroup) root;
				final int childCount = groupView.getChildCount();
				for (int i = 0; i < childCount; i++) {
					result = getFirstViewByTag(groupView.getChildAt(i), tag);
					if (result != null) {
						break;
					}

				}

			}
		}
		return result;

	}

}
