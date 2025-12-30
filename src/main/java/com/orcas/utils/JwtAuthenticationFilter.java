package com.orcas.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orcas.constants.MappingConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; 
    private final List<String> skipServices = Arrays.asList(MappingConstants.URL_API_V1+MappingConstants.URL_LOGIN, MappingConstants.URL_API_V1+MappingConstants.HEALTH_CHECK);

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
    	System.out.println(request.getRequestURI());
    	if(skipServices.contains(request.getRequestURI()) || "OPTIONS".equals(request.getMethod())) {
    		System.out.println("JWT is skipped");
    		filterChain.doFilter(request, response); 
    		return;
    	} else {
    		String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                if(jwtUtil.isExpired(jwt)) {
                	System.out.println("JWT token Expired");
                	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                } else {
                	System.out.println("JWT token Validated");
                	if(Arrays.asList("POST", "PATCH", "DELETE").contains(request.getMethod()) && !jwtUtil.extractRole(jwt).equals("A")) {
                		System.out.println("JWT Role Not Present");
                    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                	} else {
                		filterChain.doFilter(request, response);
                    	return;
                	}
                }
            } else {
            	System.out.println("JWT token Not Present");
            	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
    	}
    }
}