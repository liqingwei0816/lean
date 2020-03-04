package com.github.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bean.system.SysUser;
import com.github.util.ResultUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.stream.Collectors;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private JWTUtil jwtUtil;
    @Resource
    private ObjectMapper objectMapper;

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        SysUser sysUser = objectMapper.readValue(httpServletRequest.getInputStream(), SysUser.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(sysUser.getUserName(),sysUser.getPassword());
        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String authorityString = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String token = jwtUtil.getToken(authResult.getName(),  authorityString);
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()){
            out.write(objectMapper.writeValueAsString( ResultUtils.success(token)));
            out.flush();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter out = response.getWriter()){
            out.write(objectMapper.writeValueAsString(ResultUtils.error("登录失败!")));
            out.flush();
        }

    }
}
