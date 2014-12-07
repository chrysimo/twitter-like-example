package it.demo.twitterlike.server.config.security.basic;

import it.demo.twitterlike.server.config.utils.ProfileUtils;
import it.demo.twitterlike.server.security.AuthoritiesConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Profile(ProfileUtils.PROFILE_SECURITY_BASIC)
@Configuration

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class BasicAuthenticationSecurityAdapter extends WebMvcConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
	}

	@Bean
	public ApplicationSecurity applicationSecurity() {
		return new ApplicationSecurity();
	}
	
	
	@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	            .userDetailsService(userDetailsService)
	                .passwordEncoder(passwordEncoder);
	    }

	/*@Profile(ProfileUtils.PROFILE_SECURITY_BASIC)
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Configuration
	protected static class AuthenticationSecurity extends
			GlobalAuthenticationConfigurerAdapter {
		
		@Autowired
		private UserDetailsService userDetailsService;
		@Autowired
		private PasswordEncoder passwordEncoder;
		
		@Autowired
		private MessageService messageService;
				
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {			
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

		}
	}*/

	@Order(1) 
	protected static class ApplicationSecurity extends
			WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			  http.httpBasic().and()
	           
	            .csrf()
	            .disable()
	            
	            .authorizeRequests()
	            .antMatchers("/app/rest/register").permitAll()
                .antMatchers("/app/rest/activate").permitAll()
                .antMatchers("/app/rest/authenticate").permitAll()
                .antMatchers("/app/rest/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/app/**").authenticated().antMatchers("/rest/**").authenticated();
	                
			  
			
			
			
			
			
			// @formatter:off
			/*	http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest()
			.fullyAuthenticated().and().formLogin().loginPage("/login")
			.failureUrl("/login?error").permitAll();*/
			
		/*	http.authorizeRequests().antMatchers("/login").permitAll().anyRequest()
			.fullyAuthenticated().and().formLogin().loginPage("/login")
			.failureUrl("/login?error").and().logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
			.exceptionHandling().accessDeniedPage("/access?error");*/
			
			/* http.httpBasic();
			 http.csrf().disable();*/
			
		//	 http.antMatcher("/app/rest/logs/**").authorizeRequests().anyRequest().authenticated().and().csrf().disable().httpBasic();	
//		 http.antMatcher("/rest/**").authorizeRequests().anyRequest().authenticated().and().csrf().disable().httpBasic();
			
			// @formatter:on
		}
	}
	

}
