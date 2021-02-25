package myboot.vega2k.rest.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import myboot.vega2k.rest.account.AccountService;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	AccountService accountService;
	
	//@Autowired
	//TokenStore tokenStore;
	
	@Autowired
	private DataSource dataSource;



	// OAuth2 인증 서버 보안(Password) 정보를 설정
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) 
           throws Exception {
		security.passwordEncoder(passwordEncoder);		
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
		/*	
		clients.inMemory().withClient("myApp")
				   .authorizedGrantTypes("password", "refresh_token")
				   .scopes("read", "write")
				   .secret(this.passwordEncoder.encode("pass"))
				   .accessTokenValiditySeconds(10 * 60)
				   .refreshTokenValiditySeconds(6 * 10 * 60);
		*/		   
	}

	@Override
	 public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws 
	 Exception {
			endpoints.authenticationManager(authenticationManager)
					 .userDetailsService(accountService)
					 .tokenStore(tokenStore())
					 .approvalStore(approvalStore());
					 //.tokenStore(tokenStore);
	 }
	
	@Bean
    public TokenStore tokenStore() { //(2)
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() { //(3)
        return new JdbcApprovalStore(dataSource);
    }

}
