package com.guttv.config.security.phoneSecurity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 520L;

    @Getter
    @Setter
    private  Object principal;
    /**
     * 验证码
     */
    @Getter
    @Setter
    private  String code;

    public CodeAuthenticationToken(String mobile,String code) {
        super(null);
        this.principal = mobile;
        this.code=code;
        this.setAuthenticated(false);
    }

    public CodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}

