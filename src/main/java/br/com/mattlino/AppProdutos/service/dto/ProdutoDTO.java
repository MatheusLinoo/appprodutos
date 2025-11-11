package br.com.mattlino.AppProdutos.service.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProdutoDTO(Long id, String codigoBarras, String nome, BigDecimal preco) {


}
