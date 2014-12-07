package it.demo.twitterlike.android.ui;

import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.android.tasks.MessageCreateTask;
import it.demo.twitterlike.android.ui.utils.ViewUtils;

import org.apache.commons.lang3.StringUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.telly.groundy.Groundy;
import com.telly.groundy.annotations.OnStart;
import com.telly.groundy.annotations.OnSuccess;
import com.telly.groundy.annotations.Param;

public class MessageCreateActivity extends AbstractAsyncActivity {

	public static final String MESSAGE_RESULT = MessageCreateTask.MESSAGE_RESULT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagecreate);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setTitle(R.string.activity_message_create_title);

		final EditText etMessage = (EditText) findViewById(R.id.et_message);
		final View buttonOk = findViewById(R.id.bt_message_create);
		final View buttonCancel = findViewById(R.id.bt_message_cancel);

		etMessage.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i,
					int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1,
					int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {

				buttonOk.setEnabled(StringUtils
						.isNotBlank(editable != null ? editable.toString()
								: null));
			}
		});

		buttonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Editable text = etMessage.getText();
				if (text != null) {
					saveMessage(text.toString());
				}

			}
		});

		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});

		{
			Editable editable = etMessage.getText();
			buttonOk.setEnabled(StringUtils
					.isNotBlank(editable != null ? editable.toString() : null));
		}

		ViewUtils.openKeyboard(this);

	}
	
	protected void saveMessage(String message ) {
		Groundy.create(MessageCreateTask.class)
		.callback(this)
		.arg(MessageCreateTask.ARGS_MESSAGE_TEXT,
				message)
		.callbackManager(callbacksManager)
		.queueUsing(this);
	}

	@OnStart(MessageCreateTask.class)
	public void onDeleteUserTaskStarted() {
		showProgressDialog(getString(R.string.progressbar_create_message));
	}

	@OnSuccess({ MessageCreateTask.class })
	public void onMessageCreated(
			@Param(MessageCreateTask.MESSAGE_RESULT) InternalMessage message) {
		dismissProgressDialog();
		ViewUtils.closeKeyboard(this, findViewById(R.id.et_message));
		makeToastText(R.string.toast_message_created);
		Intent result = new Intent();
		result.putExtra(MESSAGE_RESULT, message);
		setResult(RESULT_OK, result);
		
		finish();
	}

	public static void show(Activity context, int requestCode) {
		final Intent intent = new Intent(context, MessageCreateActivity.class);
		context.startActivityForResult(intent, requestCode);

	}

}
