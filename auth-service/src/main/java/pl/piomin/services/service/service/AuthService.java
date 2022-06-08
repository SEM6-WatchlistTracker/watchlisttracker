package pl.piomin.services.service.service;

import pl.piomin.services.service.model.NewUser;
import pl.piomin.services.service.model.Role;
import pl.piomin.services.service.model.document.User;
import pl.piomin.services.service.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired private AuthRepository authRepository;
    @Autowired private TokenService tokenService;

    public String signIn(String email, String password) {
        User foundUser = authRepository.findByEmail(email);
        if (MatchPassword(password, foundUser.getPassword())) {
            return createToken(foundUser);
        }
        return null;
    }

    public String createToken(User user) {
        try {
            String token = tokenService.createToken(user);
            if(!token.isEmpty()) return token;
        }
        catch(Exception e) {}
        return null;
    }

    public String EncodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean MatchPassword(String loginPassword, String userPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(loginPassword, userPassword);
    }

    public NewUser signUp(String email, String password) {
        String encodedPassword = EncodePassword(password);
        User createdUser = authRepository.save(new User(email, encodedPassword, Role.USER));
        String token = createToken(createdUser);
        NewUser newUser = new NewUser(createdUser.getUserId(), token);
        return newUser;
        // add http request / messaging for create in user service
    }
}
