package br.com.mattlino.AppProdutos.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.mattlino.AppProdutos.model.Produtos;
import br.com.mattlino.AppProdutos.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdutosService {

    private final ProdutosRepository produtosRepository;

    public Optional<Produtos> atualizaProduto(Produtos produto) {
        log.info("Atualizando produto: {}", produto);
        final var produtoExistente = produtosRepository.findById(produto.getId());
        produtoExistente.ifPresent(p -> {
            produto.setCodigoBarras(p.getCodigoBarras());
            produto.setNome(p.getNome());
            produto.setPreco(p.getPreco());
            produtosRepository.saveAndFlush(produto);
        });
        return produtoExistente;
    }

}
