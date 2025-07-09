package com.axcent.TimeSheet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
//andiamo a leggere il token e ad estrarre i dati dall'utente
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer "))
        {
            String token = authHeader.substring(7);

            //Il token viene decodificato
            Claims claims = Jwts.parser()
                    .setSigningKey("T7n3WcR9Q3ZjLwV5FfN2nPr3MqXwBv1Xh8ZmYk9rI4UzWvGp3Xl7J5Gb1MvLsFz0".getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            Long userId = claims.get("userId",Number.class).longValue();
            String nome = claims.get("nome", String.class);
            String cognome = claims.get("cognome",String.class);
            String sede = claims.get("sede", String.class);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId,null,new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(auth);

            //iniettiamo i dati nella request corrente
            request.setAttribute("userId",userId);
            request.setAttribute("nome",nome);
            request.setAttribute("cognome",cognome);
            request.setAttribute("sede",sede);
        }

        filterChain.doFilter(request,response);
    }
}
