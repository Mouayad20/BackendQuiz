package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.dto.Product.ProductCreateDto;
import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import com.mk.BackendQuiz.dto.Product.ProductUpdateDto;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping(value = "/fetch")
    public Response fetchProducts(Pageable pageable) {
        Page<ProductFetchDto> page = productService.fetchProducts(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return Response.ok().setPayload(page.getContent()).setMetadata(headers);
    }

    @PostMapping(value = "/create")
    public Response createProduct(
            @RequestBody @Valid ProductCreateDto productCreateDto,
            @RequestHeader("Authorization") String token
    ) {
        return Response.ok().setPayload(productService.createProduct(productCreateDto, token));
    }

    @PatchMapping(value = "/update")
    public Response updateProduct(@RequestBody ProductUpdateDto productCreateDto) {
        return Response.ok().setPayload(productService.updateProduct(productCreateDto));
    }

    @DeleteMapping(value = "/delete")
    public Response deleteProduct(@RequestParam(name = "id") Long id) {
        return Response.ok().setPayload(productService.deleteProduct(id));
    }

    @GetMapping(value = "/report")
    public Response productReport() {
        return Response.ok().setPayload(productService.productReport());
    }

}
