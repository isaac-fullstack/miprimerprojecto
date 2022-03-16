
package com.example.demo.configuraciones;

import com.example.demo.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter{
    
    @Autowired
    public ClienteServicio clienteServicio; 
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(clienteServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    } 
     
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http    .authorizeRequests()
                .antMatchers("/css/**","/js/**","/img/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("clave")
                .defaultSuccessUrl("/libro/listado")
                .failureUrl("/login?error=error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login").permitAll()
                .and().csrf().disable();
    }
}
