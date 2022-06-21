package com.smart.tech.start.registration.services;

import com.smart.tech.start.registration.models.User;
import com.smart.tech.start.registration.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s could not be found.";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

     public String singUpUser(User user){
         boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

         if (userExists){
             throw new IllegalStateException("Email already taken");
         }

         String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

         user.setPassword(encodedPassword);

         userRepository.save(user);

         return "it works";
     }
}
