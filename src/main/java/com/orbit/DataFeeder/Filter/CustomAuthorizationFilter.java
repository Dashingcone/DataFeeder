package com.orbit.DataFeeder.Filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    final private String AUTHORIZATION = "Authorization";
    final private String BEARER = "Bearer ";
    private final static Logger logger =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().equals("/notebook/login") ||
                request.getServletPath().equals("/notebook/refreshToken")){
            filterChain.doFilter(request,response);
        }else{
            String authFiler = request.getHeader(AUTHORIZATION);
            if(authFiler!=null && authFiler.startsWith(BEARER)){
               try{
                   String token = authFiler.substring(BEARER.length());
                   Algorithm algorithm  = Algorithm.HMAC256("secret".getBytes());
                   JWTVerifier verifier = JWT.require(algorithm).build();
                   DecodedJWT decodedJWT = verifier.verify(token);
                   String userName = decodedJWT.getSubject();
                   String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
                   Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                   stream(roles).forEach(ro->{
                       authorities.add(new SimpleGrantedAuthority(ro));
                   });

                   UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                           new UsernamePasswordAuthenticationToken(
                           userName,null,authorities);
                   SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                   filterChain.doFilter(request,response);
               }catch (Exception e){
                    logger.log(Level.INFO,"Error logging in: {} "+ "as null/wrong/expired/compromised Token");
                    response.setHeader("Error",e.getMessage());
                    response.setStatus(FORBIDDEN.value());

                   Map<String,String> map = new HashMap<>();
                   map.put("Error_Message",e.getMessage());
                   response.setContentType(APPLICATION_JSON_VALUE);
                   new ObjectMapper().writeValue(response.getOutputStream(),map);
               }
            }else{
                logger.log(Level.INFO,"Access Denied as no Token available");
                response.setHeader("Error","Access Denied as no Token available");
                response.setStatus(FORBIDDEN.value());

                Map<String,String> map = new HashMap<>();
                map.put("Error_Message","Access Denied as no Token available");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),map);
                filterChain.doFilter(request,response);
            }
        }
    }
}
