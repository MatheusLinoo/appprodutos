package br.com.mattlino.AppProdutos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.mattlino.AppProdutos.model.Produtos;
import br.com.mattlino.AppProdutos.service.dto.ProdutoDTO;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, Long>{

    @Query(nativeQuery = true, value = """
            SELECT p.id, 
            p.codigo_Barras AS codigoBarras,
            p.nome,
            p.preco
            FROM tb_produtos p 
            WHERE p.id = :id
            """)
    ProdutoDTO findByIdDto(long id);

}
