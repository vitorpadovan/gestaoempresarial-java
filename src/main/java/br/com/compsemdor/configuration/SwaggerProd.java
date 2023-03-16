package br.com.compsemdor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerProd implements WebMvcConfigurer {

	@Bean
	public Docket jovensSaradosApiSwagger() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/**")).build().apiInfo(new ApiInfoBuilder().title("Api Jovens Sarados")
						.version(getClass().getPackage().getImplementationVersion()).build());
	}

	// http://localhost:8080/swagger-ui
	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/swagger-ui/");
		// any other alias
	}
}
