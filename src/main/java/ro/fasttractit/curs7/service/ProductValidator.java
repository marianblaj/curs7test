package ro.fasttractit.curs7.service;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ro.fasttractit.curs7.entity.Product;
import ro.fasttractit.curs7.exception.ValidationException;
import ro.fasttractit.curs7.repository.ProductRepository;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor

public class ProductValidator {

    private final ProductRepository repository;

    public void validateNewThrow(Product newProduct) {
        validateName(newProduct, true)
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    public void validateReplaceThrow(long productId, Product newProduct) {
        validateId(productId)
                .or(() -> validateName(newProduct, false))
                .ifPresent(ex -> {
                    throw ex;
                });
    }

    public void validateExistsOrThrow(Long productId) {
        validateId(productId)
                .ifPresent(ex -> {
                    throw ex;
                });
    }


    private Optional<ValidationException> validateName(Product product, boolean newEntity) {
        if (product.getName() == null)
            return of(new ValidationException("Name cannot be null."));
        else if (newEntity && repository.existsByName(product.getName()))
            return of(new ValidationException("Name cannot be duplicate."));
        else if (!newEntity && repository.existsByNameAndIdNot(product.getName(), product.getId()))
            return of(new ValidationException("Name cannot be duplicate."));

        else return empty();
    }


    private Optional<ValidationException> validateId(Long productId) {
        return repository.existsById(productId) ? empty() :
                of(new ValidationException("Product with Id " + productId + " does not exists."));

    }


}
