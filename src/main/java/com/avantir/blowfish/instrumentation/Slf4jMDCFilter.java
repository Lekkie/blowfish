package com.avantir.blowfish.instrumentation;

import com.avantir.blowfish.config.Slf4jMDCFilterConfig;
import com.avantir.blowfish.services.StringService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by lekanomotayo on 10/03/2018.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class Slf4jMDCFilter extends OncePerRequestFilter {

    private final String responseHeader;
    private final String mdcTokenKey;
    private final String requestHeader;

    @Autowired
    StringService stringService;

    public Slf4jMDCFilter() {
        responseHeader = Slf4jMDCFilterConfig.DEFAULT_RESPONSE_TOKEN_HEADER;
        mdcTokenKey = Slf4jMDCFilterConfig.DEFAULT_MDC_UUID_TOKEN_KEY;
        requestHeader = Slf4jMDCFilterConfig.DEFAULT_REQUEST_TOKEN_HEADER;
    }

    public Slf4jMDCFilter(final String responseHeader, final String mdcTokenKey, final String requestHeader) {
        this.responseHeader = responseHeader;
        this.mdcTokenKey = mdcTokenKey;
        this.requestHeader = requestHeader;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws java.io.IOException, ServletException {
        try {
            final String token;
            if (!stringService.isEmpty(requestHeader) && !stringService.isEmpty(request.getHeader(requestHeader))) {
                token = request.getHeader(requestHeader);
            } else {
                token = UUID.randomUUID().toString().toUpperCase().replace("-", "");
            }
            MDC.put(mdcTokenKey, token);
            if (!stringService.isEmpty(responseHeader)) {
                response.addHeader(responseHeader, token);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(mdcTokenKey);
        }
    }


    @Override
    protected boolean isAsyncDispatch(final HttpServletRequest request) {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }

}
