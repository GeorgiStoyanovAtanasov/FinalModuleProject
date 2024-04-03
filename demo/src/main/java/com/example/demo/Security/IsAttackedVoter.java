//package com.example.demo.Security;
//
//import com.example.demo.Entities.Player;
//import com.example.demo.Repositories.PlayerRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.AccessDecisionVoter;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.vote.AuthenticatedVoter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//
//@Component
//public class IsAttackedVoter implements AccessDecisionVoter<Object> {
//    @Autowired
//    PlayerRepository playerRepository;
//
//    @Override
//    public boolean supports(ConfigAttribute attribute) {
//        return true;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return true;
//    }
//
//    @Override
//    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
//        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
//            return ACCESS_DENIED;
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication2.getName();
//        Player player = playerRepository.findByUsername(username);
//        if (player.isAttacked()) {
//            return ACCESS_DENIED;
//        }
//
//        return ACCESS_GRANTED;
//    }
//}
