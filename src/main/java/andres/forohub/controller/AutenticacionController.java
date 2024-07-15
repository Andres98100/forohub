package andres.forohub.controller;

import andres.forohub.domain.user.DtoUserAuthentication;
import andres.forohub.domain.user.User;
import andres.forohub.infra.security.DtoJWTToken;
import andres.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> authenticationUser(@RequestBody @Valid DtoUserAuthentication dtoUserAuthentication){
        Authentication authToken = new UsernamePasswordAuthenticationToken(dtoUserAuthentication.username()
                , dtoUserAuthentication.password());
        var authentication = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new DtoJWTToken(JWTtoken));
    }
}
