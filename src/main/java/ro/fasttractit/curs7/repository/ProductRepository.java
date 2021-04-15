package ro.fasttractit.curs7.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ro.fasttractit.curs7.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
