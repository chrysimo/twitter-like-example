package it.demo.twitterlike.rest.api;

import org.junit.Assert;
import org.junit.Test;

public class TestAuthenticatedUserEndPoint extends AbstractEndpointTest {

    @Test
    public void testAuthenticatedUser() {
    	UserProfile user = service.userOperations().getUserProfile().getContent();
    	 Assert.assertNotNull(user);
    }

}
