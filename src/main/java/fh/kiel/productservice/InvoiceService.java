package fh.kiel.productservice;

import fh.kiel.productservice.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Service
public class InvoiceService {

    @ResponseStatus(HttpStatus.CREATED)
    public void createInvoice(Order newOrder){

        final String uri = "http://invoices:8080/api/v1/invoices/";

        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.postForObject(uri, newOrder, String.class);
        System.out.println(result);
    }
}
