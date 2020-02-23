package com.guttv.config.security.phoneSecurity;


import com.guttv.mapper.system.AuthMapper;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CodeAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private AuthMapper authMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String credentials = authentication.getPrincipal().toString();
        //获取过滤器封装的token信息
        CodeAuthenticationToken authenticationToken = (CodeAuthenticationToken) authentication;
        //不通过 todo 验证不通过处理 待添加验证码校验 验证码保存位置信息等
       /* if (userDetails == null) {
            throw new InternalAuthenticationServiceException("Unable to obtain user information");
        }*/
       if (true){
           throw new InternalAuthenticationServiceException("验证码错误");
       }
        List<SimpleGrantedAuthority> authorities = authMapper.authoritiesByUsernameQuery(credentials).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
        //通过
        CodeAuthenticationToken codeAuthenticationToken = new CodeAuthenticationToken(credentials,authorities);
        codeAuthenticationToken.setDetails(authenticationToken.getDetails());
        return codeAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CodeAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
