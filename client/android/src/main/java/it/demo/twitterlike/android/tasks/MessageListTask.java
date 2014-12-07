package it.demo.twitterlike.android.tasks;

import it.demo.twitterlike.android.domain.InternalMessage;
import it.demo.twitterlike.rest.api.Message;

import java.util.ArrayList;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

import com.telly.groundy.TaskResult;

public class MessageListTask extends
		AbstractMessageBasedRemoteInvocationTask {

	public static final String ARGS_LINK = "link";

	public static final String MESSAGE_LIST = "messages";

	public MessageListTask() {
		super(true);
	}

	protected TaskResult doInRemote() {

		String _link = getStringArg(ARGS_LINK);
		Link link = _link == null ? null : new Link(_link);

		PagedResources<Resource<Message>> pages = getMessageOperations()
				.findDashboardMessages(null, Integer.MAX_VALUE, link);
		ArrayList<InternalMessage> result = new ArrayList<InternalMessage>();
		if (pages != null) {
			for (Resource<Message> currentMessage : pages) {
				result.add(buildInternalMessage(currentMessage));
			}
		}
		return succeeded().add(MESSAGE_LIST, result);
	}
}
