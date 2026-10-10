package com.example.saassystem.security;

import com.example.saassystem.tenant.TenantContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @Author cxt
 * @Date 2026-10-09 16:06
 */

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            try {
                Claims c = jwtUtil.parse(auth.substring(7));
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        c.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(token);
                TenantContext.set(c.get("tid", Long.class));   // 注入租户
            } catch (Exception e) { /* token 无效则不认证，由 Security 拦截 */ }
        }
        try {
            chain.doFilter(req, res);
        } finally {
            TenantContext.clear();                    // 防止线程池复用串租户
            SecurityContextHolder.clearContext();
        }
    }
}
