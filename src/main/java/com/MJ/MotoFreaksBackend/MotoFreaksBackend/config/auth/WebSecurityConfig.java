package com.MJ.MotoFreaksBackend.MotoFreaksBackend.config.auth;

import com.MJ.MotoFreaksBackend.MotoFreaksBackend.enums.Role;
import com.MJ.MotoFreaksBackend.MotoFreaksBackend.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable().sessionManagement().and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/auth/login", "/auth/register", "/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs").permitAll()

                //USER - GET
                .antMatchers(HttpMethod.GET, "/auth/validation", "/auth/roles", "/cars/**",
                        "/challenge/**", "/message/**", "/posts/**", "/sentence", "/user/**").hasAuthority(Role.USER.toString())
                //USER- POST
                .antMatchers(HttpMethod.POST, "/challenge/**/competitor/**", "/message/read/**",
                        "/posts/**", "/user/**").hasAuthority(Role.USER.toString())
                //USER- PUT
                .antMatchers(HttpMethod.PUT, "/challenge/**/competitor/**", "/message/send/**",
                        "/posts/**", "/user/**").hasAuthority(Role.USER.toString())
                //USER- DELETE
                .antMatchers(HttpMethod.DELETE, "/posts/**", "/user/**").hasAuthority(Role.USER.toString())

                //MODERATOR- PUT
                .antMatchers(HttpMethod.POST, "/cars/**", "/challenge").hasAuthority(Role.MODERATOR.toString())
                //MODERATOR- DELETE
                .antMatchers(HttpMethod.DELETE, "/cars/**", "/sentence/**").hasAuthority(Role.MODERATOR.toString())
                //MODERATOR- POST
                .antMatchers(HttpMethod.POST, "/auth/set/moderator/**", "/cars/merge", "/challenge/**",
                        "/sentence/merge").hasAuthority(Role.MODERATOR.toString())

                //ADMIN
                .antMatchers("/**").hasAuthority(Role.ADMIN.toString())
                .anyRequest().authenticated()
                .and().csrf()
                .disable().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        http.cors();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new UserDetailsServiceImpl();
    }
}
