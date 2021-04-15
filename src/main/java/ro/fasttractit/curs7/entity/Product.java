package ro.fasttractit.curs7.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import java.util.UUID;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class Product {

    @javax.persistence.Id
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;
    private String shopId;


    public Product(String name) {
        this.name = name;
        this.shopId = UUID.randomUUID().toString();
    }

    public Product(String name, String description) {
        this.name = name;
        this.shopId = UUID.randomUUID().toString();
        this.description = description;
    }


}
