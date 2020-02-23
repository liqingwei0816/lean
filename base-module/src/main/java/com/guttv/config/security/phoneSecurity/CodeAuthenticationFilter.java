package com.guttv.config.security.phoneSecurity;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    private String mobileParameterName = "mobile";
    private String codeParameterName = "code";

    public CodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/codeLogin", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String mobile = this.obtainMobile(request);
        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();
        String code = this.obtainCode(request);
        CodeAuthenticationToken authRequest = new CodeAuthenticationToken(mobile,code);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Nullable
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(this.mobileParameterName);
    }

    /**
     * 短信验证码
     */
    @Nullable
    protected String obtainCode(HttpServletRequest request) {
        return request.getParameter(this.codeParameterName);
    }

    protected void setDetails(HttpServletRequest request, CodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }


}

