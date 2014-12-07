package it.demo.twitterlike.android.ui.adapters;

import it.demo.twitterlike.android.domain.InternalUserProfile;
import it.demo.twitterlike.android.tasks.AddFollowTask;
import it.demo.twitterlike.android.tasks.RemoveFollowTask;
import it.demo.twitterlike.android.ui.AbstractAsyncActivity;
import it.demo.twitterlike.android.ui.R;
import it.demo.twitterlike.android.utils.UserUtils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telly.groundy.Groundy;

public class UserListAdapter extends
		ExtendibleArrayAdapter<InternalUserProfile> {

	public UserListAdapter(Context context) {
		super(context);
	}

	public UserListAdapter(Context context, List<InternalUserProfile> users) {
		super(context, users);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final InternalUserProfile item = getItem(position);
		ViewHolder holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.adapter_userselector_row, null);
			holder = new ViewHolder();
			holder.ivUserSelectorIcon = (ImageView) convertView
					.findViewById(R.id.ivUserSelectorIcon);
			holder.ivFollowUser = convertView.findViewById(R.id.ivFollowUser);

			holder.ivFollowUser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					InternalUserProfile item = (InternalUserProfile) v.getTag();
					Groundy.create(AddFollowTask.class)
							.callback(getContext())
							.arg(AddFollowTask.USER_PARAMETER,
									item.getUser().getId())
							.callbackManager(
									((AbstractAsyncActivity) getContext())
											.getCallbacksManager())
							.queueUsing(getContext());

				}
			});

			holder.ivRemoveUser = convertView.findViewById(R.id.ivRemoveUser);

			holder.ivRemoveUser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					InternalUserProfile item = (InternalUserProfile) v.getTag();
					Groundy.create(RemoveFollowTask.class)
							.callback(getContext())
							.arg(RemoveFollowTask.USER_PARAMETER,
									item.getUser().getId())
							.callbackManager(
									((AbstractAsyncActivity) getContext())
											.getCallbacksManager())
							.queueUsing(getContext());

				}
			});

			holder.tvUserSelectorTitle = (TextView) convertView
					.findViewById(R.id.tvUserSelectorTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.ivUserSelectorIcon.setImageResource(getUserResourceId(item
				.getUser().getFirstName()));
		holder.tvUserSelectorTitle.setText(UserUtils.getUserFullName(item
				.getUser()));

		holder.ivRemoveUser.setVisibility(item.isFollow() ? View.VISIBLE
				: View.GONE);
		holder.ivFollowUser.setVisibility(item.isFollow() ? View.GONE
				: View.VISIBLE);
		holder.ivFollowUser.setTag(item);
		holder.ivRemoveUser.setTag(item);

		return convertView;
	}

	private static int getUserResourceId(String name) {
		final int result;
		if (isMale(name)) {
			result = R.drawable.ic_user_man_icon;
		} else {
			result = R.drawable.ic_user_woman_icon;
		}
		return result;
	}

	private static boolean isMale(String name) {
		return name != null && (!name.endsWith("a"));
	}

	private static class ViewHolder {
		private TextView tvUserSelectorTitle;

		private ImageView ivUserSelectorIcon;

		private View ivFollowUser, ivRemoveUser;

	}

}
