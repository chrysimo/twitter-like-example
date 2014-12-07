package it.demo.twitterlike.server.repository;

import it.demo.twitterlike.server.domain.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "messages", path = "messages", exported = true)
public interface MessageRepository extends PagingAndSortingRepository<Message, Long>, JpaSpecificationExecutor<Message>{

	
	@Query("select m from #{#entityName} m left join m.author.following following where m.author.login=?1 or following.login=?1")
	Page<Message> findFollowedMessages(String login, Pageable pageable);
	
	@Query("select m from #{#entityName} m where m.author.login = ?1")
    Page<Message> findMessagesByAuthorLogin(String login, Pageable pageable);
}
