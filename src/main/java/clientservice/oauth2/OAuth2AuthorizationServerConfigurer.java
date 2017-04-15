package clientservice.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@EnableAuthorizationServer
@Configuration
public class OAuth2AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	/* OAuth2 in memory credentials */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("clientId").secret("clientSecret").scopes("read","write","read-write").and()
				.build();
	}

}
