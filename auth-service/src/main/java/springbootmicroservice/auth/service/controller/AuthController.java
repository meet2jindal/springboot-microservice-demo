package springbootmicroservice.auth.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springbootmicroservice.auth.service.entity.UserCredential;
import springbootmicroservice.auth.service.model.AuthRequest;
import springbootmicroservice.auth.service.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserCredential userCredential) {
        return authenticationService.saveUser(userCredential);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        try {
            final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authenticate.isAuthenticated()) {
                return authenticationService.generateJWtToken(authRequest.getUsername());
            }
        } catch (AuthenticationException ex) {
            return ex.getMessage();
        }

        return "Invalid Access";
    }

    @GetMapping("/validate")
    public boolean validateDateToken(@RequestParam String token, @RequestParam String username) {
        return authenticationService.validateToken(token, username);
    }


}
