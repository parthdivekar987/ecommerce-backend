package com.zosh.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                // CORRECTED LINE
                Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));

                // --- THIS IS THE FIX ---
                // We now correctly read the roles/authorities from the JWT claims
                String authoritiesString = String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);

                // Create the authentication object with the correct roles
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // It's good practice to log the specific error
                logger.error("JWT validation error: " + e.getMessage());
                throw new BadCredentialsException("Invalid token received");
            }
        }

        // This is crucial, it passes the request to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}