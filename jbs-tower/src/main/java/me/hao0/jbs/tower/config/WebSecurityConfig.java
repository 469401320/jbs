package me.hao0.jbs.tower.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.html");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();


        http.authorizeRequests()


            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth,
            @Value("${jbs.user:admin}") String user,
            @Value("${jbs.pass:admin}") String pass) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(user).password(pass).roles("ADMIN");
    }
}