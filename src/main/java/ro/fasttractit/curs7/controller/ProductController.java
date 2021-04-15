package ro.fasttractit.curs7.controller;

import com.github.fge.jsonpatch.JsonPatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ro.fasttractit.curs7.service.ProductService;
import ro.fasttractit.curs7.entity.Product;


@RestController
@RequiredArgsConstructor
@RequestMapping("/products")

public class ProductController {

    private final ProductService service;

    @GetMapping("/test")
    void populateList() {
        service.populateList();
    }

    @GetMapping
    Page<Product> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @PostMapping
    Product addProduct(@RequestBody Product newProduct){
       return  service.addProduct(newProduct);
    }

    @PutMapping("/{productId}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable Long productId){
        return service.replaceProduct(productId, newProduct);
    }

    @PatchMapping("/{productId}")
    Product patchProduct(@RequestBody JsonPatch patch, @PathVariable Long productId){
        return service.patchProduct(productId, patch);
    }

}
