package it.demo.twitterlike.android.tasks;

import org.apache.commons.lang3.StringUtils;

import com.telly.groundy.TaskResult;

public class MessageCreateTask extends AbstractMessageBasedRemoteInvocationTask {

	public static final String ARGS_MESSAGE_TEXT = "text";

	public static final String MESSAGE_RESULT = "message";

	public MessageCreateTask() {
		super(true);
	}

	protected TaskResult doInRemote() {

		String text = getStringArg(ARGS_MESSAGE_TEXT);
		if (StringUtils.isNotBlank(text)) {
			return succeeded().add(MESSAGE_RESULT,
					buildInternalMessage(getMessageOperations().create(text)));
		} else {
			return failed();
		}
	}

}
