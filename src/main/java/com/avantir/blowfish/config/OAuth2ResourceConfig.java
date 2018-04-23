package com.avantir.blowfish.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by lekanomotayo on 13/03/2018.
 */


@Order(2)
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceConfig extends ResourceServerConfigurerAdapter {


    private TokenExtractor tokenExtractor = new BearerTokenExtractor();


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .addFilterAfter(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                        // We don't want to allow access to a resource with no token so clear
                        // the security context in case it is actually an OAuth2Authentication
                        if (tokenExtractor.extract(request) == null) {
                            SecurityContextHolder.clearContext();
                        }
                        filterChain.doFilter(request, response);
                    }
                }, AbstractPreAuthenticatedProcessingFilter.class)
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**", "/api-docs/**", "/api/**", "/console/**").permitAll()
                //.antMatchers("/api/**" ).authenticated();
                .antMatchers("/**" ).authenticated();

        /*
                .addFilterAfter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                        // We don't want to allow access to a resource with no token so clear
                        // the security context in case it is actually an OAuth2Authentication
                        if (tokenExtractor.extract(request) == null) {
                            SecurityContextHolder.clearContext();
                        }
                        filterChain.doFilter(request, response);
                    }
                }, AbstractPreAuthenticatedProcessingFilter.class);
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/api/**").access("#oauth2.hasScope('read')");
        http.authorizeRequests().anyRequest().authenticated();
        */


        /*
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.GET, "/api/**").access("#oauth2.hasScope('read')")
        .antMatchers(HttpMethod.POST, "/api/**").access("#oauth2.hasScope('write')");
        */

    }


    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

    @Bean
    public RemoteTokenServices remoteTokenServices(final @Value("${spring.auth.server.url}") String checkTokenUrl,
                                                   final @Value("${spring.auth.server.clientId}") String clientId,
                                                   final @Value("${spring.auth.server.clientsecret}") String clientSecret) {
        final RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl(checkTokenUrl);
        remoteTokenServices.setClientId(clientId);
        remoteTokenServices.setClientSecret(clientSecret);
        remoteTokenServices.setAccessTokenConverter(accessTokenConverter());
        return remoteTokenServices;
    }

    /*
    // For JDBC Token

    @Bean
    public DefaultTokenServices createTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore( createTokenStore() );
        return defaultTokenServices;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices( createTokenServices() );
    }

    @Bean
    public TokenStore createTokenStore() {
        return new JwtTokenStore( createJwtAccessTokenConverter() );
    }

    @Bean
    public JwtAccessTokenConverter createJwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter( new JwtConverter() );
        return converter;
    }

    public static class JwtConverter extends DefaultAccessTokenConverter implements JwtAccessTokenConverterConfigurer {

        @Override
        public void configure(JwtAccessTokenConverter converter) {
            converter.setAccessTokenConverter(this);
        }

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            OAuth2Authentication auth = super.extractAuthentication(map);
            auth.setDetails(map); //this will get spring to copy JWT content into Authentication
            return auth;
        }
    }
     */



}
