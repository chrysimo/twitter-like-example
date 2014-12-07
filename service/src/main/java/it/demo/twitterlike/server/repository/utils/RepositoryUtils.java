package it.demo.twitterlike.server.repository.utils;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class RepositoryUtils {

	private RepositoryUtils() {
		
	}
	
	public static <T extends Serializable, K> K getEntity(CrudRepository<K, T> repository, T id) throws ResourceNotFoundException {
		K entity = repository.findOne(id);
		if (entity == null) {
			throw new ResourceNotFoundException();
		}
		return entity;
	}
}
