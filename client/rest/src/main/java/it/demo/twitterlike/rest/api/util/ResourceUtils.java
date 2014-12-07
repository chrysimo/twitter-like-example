package it.demo.twitterlike.rest.api.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Resource;

public class ResourceUtils {

	public static <T> Iterable<T> getContents(
			Resource<? extends T>... resources) {
		List<T> result = new ArrayList<T>();
		if (resources != null) {
			for (Resource<? extends T> current : resources) {
				if (current != null && current.getContent() != null) {
					result.add(current.getContent());
				}
			}
		}
		return result;
	}

}
