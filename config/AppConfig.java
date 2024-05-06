package ng.optisoft.rosabon.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.modelmapper.ModelMapper;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


@EnableSwagger2
@EnableWebMvc
@EnableScheduling
@ConditionalOnProperty(name = "scheduling.enabled", matchIfMissing = true)
@EnableCaching
@Configuration
public class AppConfig extends WebMvcConfigurationSupport {
	

	private String cloudinaryApiKey = "433687219241395";
	

	private String cloudinarySecretKey = "URKCPUw1FLw5aTGdwwRaDHkClq0";

	
	private String cloudinaryCloudName = "rosabon";

	private static  final String AUTHORIZATION_HEADER = "Authorization";
	private ApiKey apikey(){
		return  new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
	}
	
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.OAS_30).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(metaData())
				.securitySchemes(Collections.singletonList(apikey()))
				.securityContexts(Collections.singletonList(securityContext()));
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(Collections.singletonList(new SecurityReference(AUTHORIZATION_HEADER, new AuthorizationScope[0])))
				.build();
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Rosabon Main Service")
				.description("Handling Treasury and Credit solutions - Engineered By Optisoft Technology Co. Ltd ")
				.version("0.0.1").license("Rosabon MOU With Optisoft").licenseUrl("https://opisoft.ng")
				.contact(new Contact("Optisoft", "https://optisoft.ng", "+info@optisoft.ng")).build();
	}

	@Bean
	public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
			ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
			EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
			WebEndpointProperties webEndpointProperties, Environment environment) {
		List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
		Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
		allEndpoints.addAll(webEndpoints);
		allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
		allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
		String basePath = webEndpointProperties.getBasePath();
		EndpointMapping endpointMapping = new EndpointMapping(basePath);
		boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
				basePath);
		return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
				corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
				shouldRegisterLinksMapping, null);
	}

	private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
			String basePath) {
		return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
				|| ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
	}

//	@Bean
//	public FlywayMigrationStrategy repair() {
//		return flyway -> {
//			flyway.repair();
//			flyway.migrate();
//		};
//	}

	@Bean
	public Cloudinary setupCloudinary() {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "rosabon", "api_key",
				"433687219241395", "api_secret", "URKCPUw1FLw5aTGdwwRaDHkClq0", "secure", true));

		return cloudinary;
	}
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("customers", "plans","wema_access_tokens");
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public LoadingCache<String, String> getOtpCache()
	{
		final Integer EXPIRE_MINS = 10;
		
		LoadingCache<String, String> otpCache;
		
//		  otpCache = CacheBuilder.newBuilder()
//				  .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
//				  .build((String a) ->  {
//					  return "";
//				  });
		//				  });
		  
		  otpCache = CacheBuilder.newBuilder()
				  .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
				  .build(new CacheLoader<String,String>() {
					  @Override
					public String load(String key) {
						  return "";
					  }
				  });
		  
		  return otpCache;
	}
  
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

//	@Bean
//	public ResourceBundleMessageSource messageSource() {
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//		source.setBasename("messages"); // assumes your file is named messages.properties
//		source.setDefaultEncoding("UTF-8");
//		return source;
//	}

}
