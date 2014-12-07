package it.demo.twitterlike.server.repository;

import it.demo.twitterlike.server.domain.Authority;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, String>, JpaSpecificationExecutor<Authority>{

}
