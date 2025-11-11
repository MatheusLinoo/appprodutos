package br.com.mattlino.AppProdutos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mattlino.AppProdutos.infra.security.TokenService;
import br.com.mattlino.AppProdutos.repository.UserRepository;
import br.com.mattlino.AppProdutos.service.dto.AuthenticationDTO;
import br.com.mattlino.AppProdutos.service.dto.RefreshTokenDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO data) {
        var refreshToken = data.refreshToken();
        var login = tokenService.validateToken(refreshToken);
        if (login == null) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        var user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        var newAccessToken = tokenService.generateToken(user);

        return ResponseEntity.ok(new RefreshResponseDTO(newAccessToken));
    }
}
