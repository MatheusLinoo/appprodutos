package br.com.mattlino.AppProdutos.service.dto;

import br.com.mattlino.AppProdutos.model.UserRole;

public record UserResponseDTO(String id, String login, UserRole role) {

}
