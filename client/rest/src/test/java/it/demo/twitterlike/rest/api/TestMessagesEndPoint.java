package it.demo.twitterlike.rest.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

public class TestMessagesEndPoint extends AbstractEndpointTest {

    
    @Test
    public void testMessageCreateAndDelete() {
    	
    
    	Object value = service.userOperations().findAll(null, Integer.MAX_VALUE, null);
    	
    	PagedResources<Resource<Message>> messages = service.messageOperations().findDashboardMessages(null, Integer.MAX_VALUE, null);
    	int size = messages.getContent() == null ? 0 : messages.getContent().size();
    	
    	Resource<Message> messageResource = service.messageOperations().create("Test message");
    	messages = service.messageOperations().findDashboardMessages(null, null, null);
    	assertEquals(size + 1, messages.getContent() == null ? 0 : messages.getContent().size());
    	
    
    
    	Map<String,UserProfile> followed = getIdentifierMap(service.userOperations().findMyFollow(null, Integer.MAX_VALUE, null).getContent());
    	
    	
    	
    	Resource<UserProfile> self = service.userOperations().getUserProfile();
    	followed.put(self.getId().getHref(), self.getContent());
    	
    	for (Resource<Message> currentMessage : messages) {
    		assertTrue(followed.containsKey(currentMessage.getLink("author").getHref()));
  
    	}
    	
    }
    
    
    protected <T> Map<String, T> getIdentifierMap(Iterable<Resource<T>> elements) {
    	 Map<String, T> result = new HashMap<String, T>();
    	if (elements != null) {
    		
    		for (Resource<T> current : elements) {
    			if (current.getContent() != null && current.getId() != null) {
    			result.put(current.getId().getHref(), current.getContent());
    			}
    		}
    	}
    	 
    	return result;
    }
    

}
