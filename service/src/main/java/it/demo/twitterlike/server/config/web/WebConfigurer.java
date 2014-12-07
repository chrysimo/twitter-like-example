package it.demo.twitterlike.server.config.web;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
//@AutoConfigureAfter(CacheConfiguration.class)
public class WebConfigurer implements ServletContextInitializer,
		EmbeddedServletContainerCustomizer {

	private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

	@Inject
	private Environment env;

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException {
		// if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
	            initH2Console(servletContext);
	  //      }

	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// TODO Auto-generated method stub

	}
	
	 /**
     * Initializes H2 console
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", new org.h2.server.web.WebServlet());
        h2ConsoleServlet.addMapping("/console/*");
        h2ConsoleServlet.setLoadOnStartup(1);
    }

}
