package fr.uge.revevue.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenFilter tokenFilter;

    @Autowired
    public SecurityConfig(TokenFilter tokenFilter, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService){
        this.tokenFilter = tokenFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/css/**", "/prism/**", "/script/**", "/h2-console/**").permitAll()
// Pas besoin des antMatchers puisqu'on utilise les PreAuthorize pour gérer la sécurité

//                .antMatchers("/","/signup", "/login", "/refresh").permitAll()   // Client léger
//                .antMatchers("/codes/{codeId}", "/reviews/{reviewId}").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/api/v1/signup", "/api/v1/login", "/api/v1/refresh", "/api/v1/codes/filter/**").permitAll()  // Client lourd
//                .antMatchers("api/v1/codes/{codeId}", "api/v1/posts/comments/{postId}",
//                        "api/v1/posts/reviews/{postId}/", "api/v1/reviews/{reviewId}").permitAll()
//                .anyRequest().authenticated()
                .and().logout().deleteCookies("bearer", "refresh");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider (){
        var dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(bCryptPasswordEncoder);
        return dao;
    }
}
