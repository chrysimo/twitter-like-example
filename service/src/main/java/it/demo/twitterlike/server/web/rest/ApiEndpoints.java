package it.demo.twitterlike.server.web.rest;

public interface ApiEndpoints {

	String SERVICES_BASE_ENDPOINT = "/rest/";
	String API_BASE_ENDPOINT = SERVICES_BASE_ENDPOINT + "api/";
	String ACCESS_BASE_ENDPOINT = SERVICES_BASE_ENDPOINT + "access";
	String API_ENDPOINT_PEOPLE = API_BASE_ENDPOINT + "users";
	String API_ENDPOINT_ADMIN = API_BASE_ENDPOINT + "admin";
	String API_ENDPOINT_MESSAGES = API_BASE_ENDPOINT + "messages";
	
}
