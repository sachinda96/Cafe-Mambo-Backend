package lk.cafemambo.service.impl;

import lk.cafemambo.entity.LoginEntity;
import lk.cafemambo.entity.UserEntity;
import lk.cafemambo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            ArrayList<GrantedAuthority> roles=new ArrayList<GrantedAuthority>();
            UserEntity userEntity =userRepository.findByEmail(email);
            roles.add(new SimpleGrantedAuthority(userEntity.getRole()));
            return new User(userEntity.getEmail(),userEntity.getLoginEntity().getPassword(), roles);
        } catch (Exception e) {
            throw new UsernameNotFoundException("email not fount: " + email );
        }
    }
}
