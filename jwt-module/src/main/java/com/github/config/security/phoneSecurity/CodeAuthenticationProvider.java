package com.github.config.security.phoneSecurity;


import com.github.service.system.AuthService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CodeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private AuthService authService;

    @Resource
    private CodeUtil codeUtil;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String credentials = authentication.getPrincipal().toString();
        //获取过滤器封装的token信息 TODO 这种方式现在确实验证码获取途径 codeUtil中有添加验证码接口
        CodeAuthenticationToken authenticationToken = (CodeAuthenticationToken) authentication;
        //不通过
        String codeVo = authenticationToken.getCode();
        if (codeVo==null|| !Objects.equals(codeVo,codeUtil.getCode(credentials))){
            throw new InternalAuthenticationServiceException("验证码错误");
        }
        //移除验证码
        codeUtil.removeCode(credentials);
        List<SimpleGrantedAuthority> authorities = authService.authoritiesByUsernameQuery(credentials).stream().filter(a -> !StringUtils.isEmpty(a.getAuthCode())).map(e -> new SimpleGrantedAuthority(e.getAuthCode())).collect(Collectors.toList());
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
