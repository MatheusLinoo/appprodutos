package br.com.mattlino.AppProdutos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.mattlino.AppProdutos.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);

}
