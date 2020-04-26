package com.googlecode.nickmcdowall.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    public ProductController(@Autowired ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/details/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse productDetails(@PathVariable String id) {
        return productService.getProductDetailsFor(id);
    }
}
