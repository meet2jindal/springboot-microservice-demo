package springbootmicroservice.auth.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import springbootmicroservice.auth.service.entity.UserCredential;
import springbootmicroservice.auth.service.model.CustomUserDetails;
import springbootmicroservice.auth.service.repository.UserCredentialRepository;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findByUserName(username);
        return userCredentialOptional.map(CustomUserDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("No User exist with username : " + username));
    }
}
