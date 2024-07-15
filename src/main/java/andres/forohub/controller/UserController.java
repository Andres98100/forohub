package andres.forohub.controller;

import andres.forohub.domain.user.*;
import andres.forohub.infra.security.DtoJWTToken;
import andres.forohub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRespository userRespository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<DtoResponseUser> createUser(@RequestBody @Valid DtoRegisterUser dtoRegisterUser,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        String encryptedPassword = passwordEncoder.encode(dtoRegisterUser.password());
        User user = new User(dtoRegisterUser ,encryptedPassword);
        user = userRespository.save(user);
        DtoResponseUser dtoResponseUser = new DtoResponseUser(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.created(uriComponentsBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri())
                .body(dtoResponseUser);
    }

    @GetMapping
    public String getUser() {
        return "user";
    }
}
