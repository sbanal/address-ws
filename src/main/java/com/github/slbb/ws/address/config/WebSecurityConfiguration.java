package com.github.slbb.ws.address.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ActiveProfiles("test")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApiAdminUser adminUser;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(adminUser.getUsername())
            .password(adminUser.getPassword())
            .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
            .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE,"/address/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/address/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT,"/address/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,"/address").hasRole("ADMIN")
            .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/address/**").permitAll()
            .and()
                .authorizeRequests().antMatchers("/swagger-ui/**", "/api/swagger-ui/**").permitAll()
            .and()
                .csrf().disable()
            .cors().disable();
    }

}
