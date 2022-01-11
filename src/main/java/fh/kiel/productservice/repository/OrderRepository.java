package fh.kiel.productservice.repository;

import fh.kiel.productservice.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findAllByName(String name);
}
