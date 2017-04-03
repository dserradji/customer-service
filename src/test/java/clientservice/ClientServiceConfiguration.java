package clientservice;

import java.util.Collection;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;

@Configuration
public class ClientServiceConfiguration extends WebSecurityConfigurerAdapter {

	/**
	 * Authorize all requests and use a custom
	 * {@link org.springframework.security.access.AccessDecisionManager} to
	 * grant them access.
	 * <p>
	 * This hack is needed to bypass OAuth2 authentication and authorization
	 * when running integration tests.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().anyRequest().authenticated()
				.accessDecisionManager(accessDecisionManager());
	}

	/**
	 * An AccessDecisionManager makes the final authorization decision based on
	 * the votes of decision voters of type
	 * {@link org.springframework.security.access.AccessDecisionVoter}.
	 * <p>
	 * Here only one AccessDecisionVoter is used and this voter returns
	 * ACCESS_GRANTED systematically.
	 * <p>
	 * The final decision strategy depends on the implementation of
	 * {@link org.springframework.security.access.AccessDecisionManager}
	 * <p>
	 * The implementation returned for this bean is
	 * {@link org.springframework.security.access.vote.AffirmativeBased.AffirmativeBased},
	 * this strategy grants access if at least one of the voters grants access.
	 * 
	 * @return The AccessDecisionManager
	 */
	@Bean
	public AccessDecisionManager accessDecisionManager() {
		return new AffirmativeBased(Collections.singletonList(new AccessDecisionVoter<Object>() {

			@Override
			public boolean supports(ConfigAttribute attribute) {
				return true;
			}

			@Override
			public boolean supports(Class<?> clazz) {
				return true;
			}

			@Override
			public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
				return ACCESS_GRANTED;
			}
		}));
	}
}
