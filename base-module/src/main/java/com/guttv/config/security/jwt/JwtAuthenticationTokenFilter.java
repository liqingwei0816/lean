package com.guttv.config.security.jwt;

import com.guttv.mapper.system.AuthMapper;
import com.guttv.util.SpringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.header:token}")
    private String token_header;

    /* private JwtUtils jwtUtils=new JwtUtils();*/

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        httpServletRequest.getSession().invalidate();

        String username = httpServletRequest.getHeader(token_header);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //todo 未使用jwt生成token token未验证
            //User user = jwtUtils.getUserFromToken(auth_token);
            //if (jwtUtils.validateToken(auth_token, user)) {
            List<SimpleGrantedAuthority> authorities = SpringUtil.getBean(AuthMapper.class).authoritiesByUsernameQuery(username).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            logger.info(String.format("Authenticated user %s, setting security context", username));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // }
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
