package it.demo.twitterlike.android.ui.adapters;

import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.android.domain.InternalUserProfile;
import it.demo.twitterlike.android.ui.R;
import it.demo.twitterlike.android.ui.UserDetailsActivity;
import it.demo.twitterlike.android.utils.UserUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageListAdapter extends ExtendibleArrayAdapter<InternalMessage> {

	public MessageListAdapter(Context context) {
		super(context);
	}

	public MessageListAdapter(Context context, List<InternalMessage> messages) {
		super(context, messages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final InternalMessage item = getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.adapter_messageview_row, null);
			holder = new ViewHolder();
			holder.tvMessageUpdateTime = (TextView) convertView
					.findViewById(R.id.tvMessageUpdateTime);
			holder.tvUserFirstName = (TextView) convertView
					.findViewById(R.id.tvUserFirstName);

			holder.tvUserFirstName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					InternalUserProfile author = ((InternalUserProfile) holder.tvUserFirstName
							.getTag());
					if (author != null) {
						UserDetailsActivity.show(getContext(), author);
					}

				}
			});

			holder.tvUserMessage = (TextView) convertView
					.findViewById(R.id.tvUserMessage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvMessageUpdateTime.setText(formatOrderTime(item.getMessage()
				.getLastModifiedDate()));
		holder.tvUserMessage.setText(item.getMessage().getText());

		holder.tvUserFirstName.setTag(item.getAuthor());
		holder.tvUserFirstName.setText(UserUtils.getUserFullName(item
				.getAuthor().getUser()));
		return convertView;
	}

	protected String formatOrderTime(Date time) {
		String result = null;
		if (time != null) {
			if (time.after(todayStart())) {
				result = new SimpleDateFormat("HH:mm").format(time);
			} else {
				result = new SimpleDateFormat("dd-MM HH:mm").format(time);
			}
		}
		return result;
	}

	public static Date todayStart() {
		return dayStart(new Date());
	}

	public static Date dayStart(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	private static class ViewHolder {
		private TextView tvUserFirstName, tvUserMessage, tvMessageUpdateTime;

	}

}
