package fh.kiel.productservice.controller;

import fh.kiel.productservice.model.Order;
import fh.kiel.productservice.repository.OrderRepository;
import fh.kiel.productservice.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping(path = "/api/v1/orders")
public class OrderController {

    @Autowired
    OrderRepository orders;

    @Autowired
    InvoiceService invoiceService;

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Order> getOrders(@RequestParam(name = "name", required = false) String name) {
        if (StringUtils.hasText(name)) {
            return orders.findAllByName(name);
        }
        return orders.findAll();
    }

    // Rückgabe einer Bestllung mittels ID
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order getOrder(@PathVariable("id") String id) {
        return orders.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    // Neue Bestellung anlegen (Invoice wird erzeugt)
    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody final Order newOrder) {
        Order order = new Order();
        order.setName(newOrder.getName());
        order.setPrice(newOrder.getPrice());
        order.setAmount(newOrder.getAmount());
        order.setDate(new Date());
        Order savedOrder = orders.save(order);
        invoiceService.createInvoice(savedOrder);
        return savedOrder;
    }

    @PostMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Order updateOrder(@PathVariable("id") final String id, @RequestBody final Order newOrder) {
        Order order = orders.findById(id).orElseThrow(OrderNotFoundException::new);
        order.setName(newOrder.getName());
        order.setPrice(newOrder.getPrice());
        order.setAmount(newOrder.getAmount());
        return orders.save(order);
    }

    // Löschen einer Bestellung
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@PathVariable("id") final String id) {
        if (!orders.existsById(id)) {
            throw new OrderNotFoundException();
        }
        orders.deleteById(id);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Order not found in database!")
    private static class OrderNotFoundException extends RuntimeException {
    }
}
