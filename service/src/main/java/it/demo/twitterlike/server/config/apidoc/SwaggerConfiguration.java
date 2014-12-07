package it.demo.twitterlike.server.config.apidoc;

import it.demo.twitterlike.server.security.InternalUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RootResourceInformation;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.userdetails.UserDetails;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.wordnik.swagger.model.ApiInfo;

@Configuration
@EnableSwagger
public class SwaggerConfiguration implements EnvironmentAware {

	@Autowired
	private SpringSwaggerConfig springSwaggerConfig;

	private RelaxedPropertyResolver propertyResolver;

	@SuppressWarnings("unchecked")
	@Bean
	public SwaggerSpringMvcPlugin customImplementation() {
		return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
				.apiInfo(apiInfo()).excludeAnnotations(RepositoryRestController.class)
				.directModelSubstitute(RootResourceInformation.class,
						String.class)
				.ignoredParameterTypes(PagedResourcesAssembler.class, UserDetails.class, InternalUserDetails.class);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.propertyResolver = new RelaxedPropertyResolver(environment,
				"swagger.");
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(propertyResolver.getProperty("title"),
				propertyResolver.getProperty("description"),
				propertyResolver.getProperty("termsOfServiceUrl"),
				propertyResolver.getProperty("contact"),
				propertyResolver.getProperty("license"),
				propertyResolver.getProperty("licenseUrl"));
	}

}
