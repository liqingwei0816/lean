package com.github.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aop.OperatorAop;
import com.github.bean.system.Auth;
import com.github.bean.system.SysUser;
import com.github.mapper.system.SysUserMapper;
import com.github.service.system.AuthService;
import com.github.util.ResultUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private AuthService authService;
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
            List<SimpleGrantedAuthority> authorities = authService.authoritiesByUsernameQuery(userName).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
            return new User(userName, sysUser.getPassword(), sysUser.getAvailable(), true, true, true, authorities);
        }).passwordEncoder(bCryptPasswordEncoder());
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //允许同域下frame嵌套
        http.headers().frameOptions().sameOrigin()
                //登录相关
                .and().formLogin().loginPage("/loginPage").loginProcessingUrl("/login").defaultSuccessUrl("/index")
                //登出相关
                .and().logout().logoutSuccessUrl("/loginPage").and().httpBasic();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.authorizeRequests();

        List<Auth> auths = authService.getAll();

        //系统自定义权限
        auths.stream()
                .filter(a -> !StringUtils.isEmpty(a.getUrl()) && !a.getUrl().trim().isEmpty())
                .forEach(e -> urlRegistry.antMatchers(e.getUrl().split(";")).hasAuthority(e.getAuthCode()));

        http.authorizeRequests().antMatchers("/login**","/loginPage", "/codeLogin").permitAll().anyRequest().authenticated();
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


    /**
     *  取消静态资源保护
     */
    @Override
    public void configure(WebSecurity web) {
        //解决静态资源拦截问题
        web.ignoring().antMatchers("/static/**");
    }

    /**
     *   PasswordEncoder 密码加密方式
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 数据添加操作人和更新人切面 bean
     */
    @Bean
    @ConditionalOnClass(SecurityContextHolder.class)
    public OperatorAop operatorAop() {
        return new OperatorAop();
    }

}
