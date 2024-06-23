package dev.aj.config.security.service;

import dev.aj.config.security.util.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    private final JwtTokenService tokenService;
    private final SecurityUserDetailsService securityUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(authToken) && request.getCookies() != null) {
            authToken = Stream.of(request.getCookies())
                              .filter(cookie -> cookie.getName().equals("AccessToken"))
                              .map(Cookie::getValue)
                              .findFirst()
                              .orElse(null);
        }
        if (StringUtils.isNotBlank(authToken) && authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);

            boolean isValidToken = StringUtils.isNotBlank(token) && tokenService.validateToken(token);

            if (isValidToken) {
                String username = tokenService.getUsernameFromToken(token);
                UserDetails userDetails;
                try {
                    userDetails = securityUserDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException exception) {
                    response.setHeader(AUTHORIZATION, "");
                    throw new UsernameNotFoundException(exception.getMessage());
                }

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } else {
                response.setHeader(AUTHORIZATION, "");
            }
        }
        filterChain.doFilter(request, response);
    }
}
