package mk.ukim.finki.wp.kol2021.restaurant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2");
        web.ignoring().antMatchers("/h2/**");
        // TODO: If you are implementing the security requirements, remove line below
        web.ignoring().antMatchers("/**");
    }

}
