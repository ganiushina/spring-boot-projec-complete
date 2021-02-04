package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.DeliveryAddress;
import com.geekbrains.springbootproject.entities.Order;
import com.geekbrains.springbootproject.entities.Product;
import com.geekbrains.springbootproject.entities.User;
import com.geekbrains.springbootproject.repositories.specifications.ProductFilters;
import com.geekbrains.springbootproject.repositories.specifications.ProductSpecs;
import com.geekbrains.springbootproject.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "word", required = false) String word,
                           @RequestParam(value = "min", required = false) Double min,
                           @RequestParam(value = "max", required = false) Double max
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        ProductFilters productFilter = new ProductFilters(word, min, max);

        Page<Product> products =
                productsService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, productFilter.getSpec());
        model.addAttribute("products", products);
        model.addAttribute("filterDef", productFilter.getFilterDefinition().toString());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        return "shop-page";
    }

    @GetMapping("/product_info/{id}")
    public String productPage(Model model, @PathVariable(value = "id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }
}
