package it.demo.twitterlike.server.config.repository;

import it.demo.twitterlike.server.repository.UserRepository;
import it.demo.twitterlike.server.security.SecurityUtils;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
@EnableSpringDataWebSupport
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement(proxyTargetClass = true)
public class JpaRepositoryConfiguration {

	@Component(value = "springSecurityAuditorAware")
	private static class SpringSecurityAuditorAware implements
			AuditorAware<String> {

		@Override
		public String getCurrentAuditor() {
			return SecurityUtils.getCurrentLogin();
		}
	}
}
