package com.guttv.config;

import com.guttv.bean.SysUser;
import com.guttv.mapper.AuthMapper;
import com.guttv.mapper.SysUserMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthMapper authMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userName->{
            SysUser sysUser = new SysUser();sysUser.setUserName(userName);
            sysUser = sysUserMapper.selectOne(sysUser);
            if (sysUser==null){
                return null;
            }
            List<SimpleGrantedAuthority> authorities = authMapper.authoritiesByUsernameQuery(userName).stream().map(e -> new SimpleGrantedAuthority(e.getUrl())).collect(Collectors.toList());
            return new User(userName, sysUser.getPassword(), sysUser.getAvailable(), true, true, true, authorities);
        }).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许同域下frame嵌套
        http.headers().frameOptions().sameOrigin().and().formLogin().and().httpBasic();
        http.authorizeRequests().antMatchers("/login**").anonymous().anyRequest().authenticated();

    }
}
