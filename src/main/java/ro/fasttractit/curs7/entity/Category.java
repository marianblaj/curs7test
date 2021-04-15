package ro.fasttractit.curs7.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private Long Id;

    public Category(String category) {
        this.category = category;
    }

    private String category;
}
