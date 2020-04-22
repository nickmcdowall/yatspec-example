package com.googlecode.nickmcdowall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product/details")
public class AppController {

    private final AppService appService;

    public AppController(@Autowired AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ProductResponse productDetails(@PathVariable String id) {
        return appService.getProductDetailsFor(id);
    }
}
