package com.guttv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guttv.aop.OperatorAop;
import com.guttv.bean.system.Auth;
import com.guttv.bean.system.SysUser;
import com.guttv.mapper.system.AuthMapper;
import com.guttv.mapper.system.SysUserMapper;
import com.guttv.util.ResultUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthMapper authMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userName -> {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(userName);
            sysUser = sysUserMapper.selectOne(sysUser);
            if (sysUser == null) {
                return null;
            }
            List<SimpleGrantedAuthority> authorities = authMapper.authoritiesByUsernameQuery(userName).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
            return new User(userName, sysUser.getPassword(), sysUser.getAvailable(), true, true, true, authorities);
        }).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        //允许同域下frame嵌套
        http.headers().frameOptions().sameOrigin().and().formLogin().and().logout().logoutSuccessUrl("/login").and().httpBasic();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.authorizeRequests();

        List<Auth> auths = authMapper.selectAll();

        //系统自定义权限
        auths.stream()
                .filter(a -> !StringUtils.isEmpty(a.getUrl()) && !a.getUrl().trim().isEmpty())
                .forEach(e -> urlRegistry.antMatchers(e.getUrl().split(";")).hasAuthority(e.getAuthCode()));

        http.authorizeRequests().antMatchers("/login**").anonymous().anyRequest().authenticated();
        //自定义权限不足是返回信息
        http.exceptionHandling(exceptionHandlingCustomizer -> exceptionHandlingCustomizer.accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            try (PrintWriter writer = httpServletResponse.getWriter()) {
                ResultUtils error = ResultUtils.error(403, "权限不足");
                writer.write(objectMapper.writeValueAsString(error));
                writer.flush();
            }
        }));
    }

    @Override
    public void configure(WebSecurity web) {
        //解决静态资源拦截问题
        web.ignoring().antMatchers("/static/**");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnClass(SecurityContextHolder.class)
    public OperatorAop operatorAop() {
        return new OperatorAop();
    }

}
