package com.smart.tech.start.registration.user;

import com.smart.tech.start.registration.user.User;
import com.smart.tech.start.registration.user.UserRepository;
import com.smart.tech.start.registration.token.ConfirmationToken;
import com.smart.tech.start.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "User with email %s could not be found.";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

     public String signUpUser(User user){
         boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

         if (userExists){
             throw new IllegalStateException("Email already taken");
         }

         String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

         user.setPassword(encodedPassword);

         userRepository.save(user);

         String token = UUID.randomUUID().toString();
         ConfirmationToken confirmationToken = new ConfirmationToken(
                 token,
                 LocalDateTime.now(),
                 LocalDateTime.now().plusMinutes(15),
                 user
         );

         confirmationTokenService.saveConfirmationToken(confirmationToken);

         return token;
     }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }
}
