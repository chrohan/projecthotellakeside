package com.dailycodework.lakesidehotel.service;

import com.dailycodework.lakesidehotel.Security.User.HotelUserDetails;
import com.dailycodework.lakesidehotel.model.User;
import com.dailycodework.lakesidehotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
       return HotelUserDetails.buildUserDetails(user);
    }
}
