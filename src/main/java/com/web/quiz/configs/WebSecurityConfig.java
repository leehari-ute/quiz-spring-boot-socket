package com.web.quiz.configs;

import com.web.quiz.components.LoginFailureHandler;
import com.web.quiz.components.LoginSuccessHandler;
import com.web.quiz.components.OAuth2SuccessHandler;
import com.web.quiz.constant.Role;
import com.web.quiz.services.UserDetailsServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImp();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers()
                    .frameOptions().sameOrigin()
                    .and()
                .authorizeRequests()
                    .antMatchers("/api/search-quiz").permitAll()
                    .antMatchers("/dashboard", "/editor-quiz/**", "/dashboard/**", "/api/**").authenticated()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/dashboard/**", "/dashboard").hasAnyAuthority(Role.TEACHER, Role.USER)
                    .antMatchers("/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .loginPage("/auth/login")
                    .successHandler(oAuth2SuccessHandler())
                    .failureUrl("/auth/login?error")
//                .failureHandler(authenticationFailureHandler())
                    .and()
                .formLogin()
                    .loginPage("/auth/login").permitAll()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/j_spring_security_check")
                    .successHandler(loginSuccessHandler())
                    .failureUrl("/auth/login?error")
//                    .failureHandler(authenticationFailureHandler())
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                    .logoutSuccessUrl("/auth/login?logout")
                    .permitAll()
                    .and()
                .exceptionHandling()
        ;
    }
}
