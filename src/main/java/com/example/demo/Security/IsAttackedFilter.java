package com.example.demo.Security;

import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IsAttackedFilter extends OncePerRequestFilter {

    @Autowired
    private PlayerRepository playerRepository;

    private boolean isAttacked() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Player player = playerRepository.findByUsername(username);
            if (player != null && player.isAttacked()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAttacked() && !request.getRequestURI().equals("/defend") && !request.getRequestURI().equals("/login") && !request.getRequestURI().equals("/register")) {
            response.sendRedirect("/defend");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
