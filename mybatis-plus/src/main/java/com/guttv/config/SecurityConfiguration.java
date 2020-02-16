package com.guttv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guttv.bean.system.Auth;
import com.guttv.bean.system.SysUser;
import com.guttv.mapper.AuthMapper;
import com.guttv.mapper.SysUserMapper;
import com.guttv.util.ResultUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthMapper authMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //todo 权限修改为添加权限代码authCode字段，管理同一功能需要多个接口场景
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
        //允许同域下frame嵌套
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin().and().formLogin().and().httpBasic();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.authorizeRequests();
        urlRegistry.antMatchers("**.css", "**.js", "/layui/**").authenticated();
        List<Auth> auths = authMapper.selectAll();
        //系统自定义权限
        auths.stream().filter(a -> !StringUtils.isEmpty(a.getUrl()) && !a.getUrl().trim().isEmpty())
                .forEach(e -> urlRegistry.antMatchers(e.getUrl().split(";")).hasAuthority(e.getAuthCode()));
        // 权限 角色 绑定关系级联删除
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

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
