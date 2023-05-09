package springbootmicroservice.auth.service.service;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springbootmicroservice.auth.service.entity.UserCredential;
import springbootmicroservice.auth.service.repository.UserCredentialRepository;

@Service
public class AuthenticationService {
    @Autowired
    UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredential userCredential){
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);

        return "User saved successfully : " + userCredential.toString();
    }

    public String generateJWtToken(String userName){
        return jwtService.generateToken(userName);
    }

    public boolean validateToken(String token, String userName){
        return jwtService.validateToken(token, userName);
    }


}
