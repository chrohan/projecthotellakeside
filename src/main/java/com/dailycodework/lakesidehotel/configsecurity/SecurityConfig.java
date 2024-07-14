package com.dailycodework.lakesidehotel.configsecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?"
        );

        return jdbcUserDetailsManager;
    }

//    @Bean
//    public UserDetailsManager inMemoryUserDetailsManager() {
//
//        UserDetails rohan = User.builder()
//                .username("rohan")
//                .password("{noop}test123")
//                .roles("ADMIN","EMPLOYEE")
//                .build();
//
//        UserDetails mohan = User.builder()
//                .username("mohan")
//                .password("{noop}test123")
//                .roles("EMPLOYEE")
//                .build();
//
//        return new InMemoryUserDetailsManager(rohan,mohan);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(configurer ->
               configurer
                       .requestMatchers(HttpMethod.GET, "/rooms/get-rooms").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.POST,"/rooms/add/new-room").hasRole("EMPLOYEE")
                       .requestMatchers(HttpMethod.PUT,"/rooms/update/**").hasRole("ADMIN")
                       .requestMatchers(HttpMethod.DELETE, "rooms/delete/**").hasRole("ADMIN")
        );
        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
