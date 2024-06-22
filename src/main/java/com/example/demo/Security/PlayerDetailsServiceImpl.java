package com.example.demo.Security;

import com.example.demo.Entities.Player;
import com.example.demo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class PlayerDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PlayerRepository playerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Player player = playerRepository.findByUsername(username);
            if(player == null){
                throw new UsernameNotFoundException("Player not found");
            }
            return new PlayerDetailsService(player);
    }
}
