package com.guttv.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guttv.aop.OperatorAop;
import com.guttv.bean.system.Auth;
import com.guttv.bean.system.SysUser;
import com.guttv.config.security.jwt.JwtAuthenticationTokenFilter;
import com.guttv.config.security.phoneSecurity.CodeAuthenticationFilter;
import com.guttv.config.security.phoneSecurity.CodeAuthenticationProvider;
import com.guttv.config.security.phoneSecurity.CodeUtil;
import com.guttv.config.security.phoneSecurity.SimpleCodeUtil;
import com.guttv.mapper.system.AuthMapper;
import com.guttv.mapper.system.SysUserMapper;
import com.guttv.util.ResultUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(codeAuthenticationProvider());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //允许同域下frame嵌套
        http.headers().frameOptions().sameOrigin().and().formLogin().loginPage("/login").successForwardUrl("/").and().logout().logoutSuccessUrl("/login").and().httpBasic();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = http.authorizeRequests();

        List<Auth> auths = authMapper.selectAll();

        //系统自定义权限
        auths.stream()
                .filter(a -> !StringUtils.isEmpty(a.getUrl()) && !a.getUrl().trim().isEmpty())
                .forEach(e -> urlRegistry.antMatchers(e.getUrl().split(";")).hasAuthority(e.getAuthCode()));

        http.authorizeRequests().antMatchers("/login**", "/codeLogin").permitAll().anyRequest().authenticated();
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
        //添加自定义拦截器
        http.addFilterAfter(codeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * jwt验证方式过滤器
     */
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
    /**
     * jwt验证方式过滤器
     */
    @Bean
    @ConditionalOnMissingBean(CodeUtil.class)
    public CodeUtil codeUtil() {
        return new SimpleCodeUtil();
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

    /**
     * 默认的 DaoAuthenticationProvider bean
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // 设置userDetailsService
        daoAuthenticationProvider.setUserDetailsService(userName -> {
            SysUser sysUser = new SysUser();
            sysUser.setUserName(userName);
            sysUser = sysUserMapper.selectOne(sysUser);
            if (sysUser == null) {
                return null;
            }
            List<SimpleGrantedAuthority> authorities = authMapper.authoritiesByUsernameQuery(userName).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
            return new User(userName, sysUser.getPassword(), sysUser.getAvailable(), true, true, true, authorities);
        });
        // 禁止隐藏用户未找到异常
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        // 使用BCrypt进行密码的hash
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 自定义的手机号登录 CodeAuthenticationFilter bean 过滤器
     */
    @Bean
    public CodeAuthenticationFilter codeAuthenticationFilter() throws Exception {
        CodeAuthenticationFilter codeAuthenticationFilter = new CodeAuthenticationFilter();
        codeAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
        codeAuthenticationFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        codeAuthenticationFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/login?error"));

        return codeAuthenticationFilter;
    }

    /**
     * 自定义的手机号登录 CodeAuthenticationProvider bean 处理器
     */
    @Bean
    public CodeAuthenticationProvider codeAuthenticationProvider() {
        CodeAuthenticationProvider codeAuthenticationProvider = new CodeAuthenticationProvider();
        codeAuthenticationProvider.setAuthMapper(authMapper);
        return codeAuthenticationProvider;
    }
}
