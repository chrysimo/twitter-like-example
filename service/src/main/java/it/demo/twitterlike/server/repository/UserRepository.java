package it.demo.twitterlike.server.repository;


import it.demo.twitterlike.server.domain.User;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users", exported = true)
public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

	@Query("select u from #{#entityName} u where u.email=?1 and u.password=?2")
	@RestResource(exported = false)
	User login(String email, String password);
	
	Slice<User> findByLastName(String lastname, Pageable pageable);
	
	@Query("select u from #{#entityName} u where u.activationKey = ?1")
    User getUserByActivationKey(String activationKey);
    
    @Query("select u from #{#entityName} u where u.activated = false and u.createdDate > ?1")
    List<User> findNotActivatedUsersByCreationDateBefore(DateTime dateTime);
    
    
    @Query("select following from #{#entityName} u inner join u.following following where u.login = ?1")
    Page<User> findFollowersByLogin(String login, Pageable pageable);
    
    @Query("select followers from #{#entityName} u inner join u.followers followers where u.login = ?1")
    Page<User> findFollowingsByLogin(String login, Pageable pageable);

    User findOneByEmail(String email);

}
