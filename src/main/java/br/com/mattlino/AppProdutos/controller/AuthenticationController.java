package br.com.mattlino.AppProdutos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mattlino.AppProdutos.infra.security.TokenService;
import br.com.mattlino.AppProdutos.model.User;
import br.com.mattlino.AppProdutos.repository.UserRepository;
import br.com.mattlino.AppProdutos.service.dto.LoginResponseDTO;
import br.com.mattlino.AppProdutos.service.dto.AuthenticationDTO;
import br.com.mattlino.AppProdutos.service.dto.RefreshResponseDTO;
import br.com.mattlino.AppProdutos.service.dto.RefreshTokenDTO;
import br.com.mattlino.AppProdutos.service.dto.UserResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

        var user = (User) auth.getPrincipal();
        var accessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(accessToken, refreshToken));

    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshTokenDTO data) {
        var refreshToken = data.refreshToken();
        var login = tokenService.validateRefreshToken(refreshToken);
        if (login == null) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        var user = userRepository.findByLogin(login);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        var newAccessToken = tokenService.generateAccessToken((User) user);

        return ResponseEntity.ok(new RefreshResponseDTO(newAccessToken));
    }

    @GetMapping("/me")
    public ResponseEntity me() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = null;
        if (principal instanceof User) {
            user = (User) principal;
            return ResponseEntity.ok(new UserResponseDTO(user.getId(), user.getLogin(), user.getRole()));
        } else if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return ResponseEntity.ok(userDetails.getUsername());
        }
        return ResponseEntity.status(401).body("User not authenticated or invalid principal type");
    }
}
