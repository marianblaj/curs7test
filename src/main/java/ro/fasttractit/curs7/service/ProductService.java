package ro.fasttractit.curs7.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ro.fasttractit.curs7.exception.ResourceNotFoundException;
import ro.fasttractit.curs7.entity.Product;
import ro.fasttractit.curs7.repository.ProductRepository;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository repository;
    private final ProductValidator productValidator;
    private final ObjectMapper mapper;

    public Page<Product> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void populateList() {
        for (int i = 1; i <= 10; i++) {
            repository.save(new Product("Produsul " + i));
        }
    }

    @SneakyThrows
    public Product addProduct(Product newProduct) {
        productValidator.validateNewThrow(newProduct);
        return repository.save(newProduct);
    }

    public Product replaceProduct(long productId, Product newProduct) {

        newProduct.setId(productId);
        productValidator.validateReplaceThrow(productId, newProduct);

        Product dbProduct = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn`t found product with id: " + productId));
        copyProduct(dbProduct, newProduct);
        return repository.save(newProduct);
    }

    private void copyProduct(Product db, Product newProduct) {
        db.setName(newProduct.getName());
        db.setDescription(newProduct.getDescription());
    }


    @SneakyThrows
    public Product patchProduct(Long productId, JsonPatch patch) {
        productValidator.validateExistsOrThrow(productId);

        Product dbProduct = repository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn`t found product with id: " + productId));


        JsonNode patchedProductJson = patch.apply(mapper.valueToTree(dbProduct));
        Product patchedProduct = mapper.treeToValue(patchedProductJson, Product.class);
        patchedProduct.setId(productId);

        return replaceProduct(productId, patchedProduct);
    }
}
