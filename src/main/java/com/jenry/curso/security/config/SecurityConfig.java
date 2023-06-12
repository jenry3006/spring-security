package com.jenry.curso.security.config;

import com.jenry.curso.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // acessos publicos liberados
                .antMatchers("/webjars/**","/css/**","/image/**","/js/**").permitAll()
                .antMatchers("/","/home").permitAll()
                // acessos privados admin
                .antMatchers("/u/**").hasAuthority("ADMIN")
                // acessos privados medico
                .antMatchers("/medicos/**").hasAuthority("MEDICO")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login-error")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //passwordEncoder= tipo de crictografia que desejar
}
