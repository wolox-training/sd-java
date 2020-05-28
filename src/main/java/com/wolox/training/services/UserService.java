package com.wolox.training.services;

import com.wolox.training.DAO.UserAuthDetails;
import com.wolox.training.models.User;
import com.wolox.training.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findFirstByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    return new UserAuthDetails(user);
  }
}
