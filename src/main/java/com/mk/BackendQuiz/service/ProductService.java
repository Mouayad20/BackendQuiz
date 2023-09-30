package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.controller.ProductController;
import com.mk.BackendQuiz.dto.Product.ProductCreateDto;
import com.mk.BackendQuiz.dto.Product.ProductDto;
import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import com.mk.BackendQuiz.dto.Product.ProductUpdateDto;
import com.mk.BackendQuiz.dto.Reporting.ProductReportDto;
import com.mk.BackendQuiz.exception.EntityType;
import com.mk.BackendQuiz.exception.ExceptionManager;
import com.mk.BackendQuiz.exception.ExceptionType;
import com.mk.BackendQuiz.model.Client;
import com.mk.BackendQuiz.model.Product;
import com.mk.BackendQuiz.repository.ClientRepository;
import com.mk.BackendQuiz.repository.ProductRepository;
import com.mk.BackendQuiz.security.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.mk.BackendQuiz.exception.EntityType.CLIENT;
import static com.mk.BackendQuiz.exception.EntityType.PRODUCT;
import static com.mk.BackendQuiz.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ClientRepository clientRepository;

    public Page<ProductFetchDto> fetchProducts(Pageable pageable) {
        logger.info("fetch all products. ");
        return productRepository.findAll(pageable).map(product -> modelMapper.map(product, ProductFetchDto.class));
    }

    public ProductDto createProduct(ProductCreateDto productCreateDto, String token) {

        String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, email);

        logger.info("client with email " + email + " Add a product " + productCreateDto.getName());

        Product product = modelMapper.map(productCreateDto, Product.class);
        product.setPublisher(client.get());
        product.setAvailableQuantity(product.getInitialQuantity());
        product.setCreationData(new Date());

        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return ExceptionManager.throwException(entityType, exceptionType, args);
    }

    public ProductUpdateDto updateProduct(ProductUpdateDto productUpdateDto) {
        Optional<Product> product = productRepository.findById(productUpdateDto.getId());
        if (product.isEmpty())
            throw exception(PRODUCT, ENTITY_NOT_FOUND, "updated");

        logger.info("product  " + product.get().getName() + " updating.. ");

        if (productUpdateDto.getName() != null)
            product.get().setName(productUpdateDto.getName());
        if (productUpdateDto.getCategory() != null)
            product.get().setCategory(productUpdateDto.getCategory());
        if (productUpdateDto.getDescription() != null)
            product.get().setDescription(productUpdateDto.getDescription());
        if (productUpdateDto.getPrice() != null)
            product.get().setPrice(productUpdateDto.getPrice());
        if (productUpdateDto.getAvailableQuantity() != null)
            product.get().setAvailableQuantity(productUpdateDto.getAvailableQuantity());

        return modelMapper.map(productRepository.save(product.get()), ProductUpdateDto.class);
    }

    @Transactional
    public Boolean deleteProduct(Long id) {
        logger.info("product with id " + id + " deleting.. ");
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw exception(PRODUCT, ENTITY_NOT_FOUND, "deleted");

        productRepository.delete(product.get());

        return !productRepository.findById(id).isPresent();
    }

    public ProductReportDto productReport() {
        logger.info("product reporting.. ");
        ProductReportDto productReportDto = new ProductReportDto();
        productReportDto.setInventoryStatus(productRepository.findInventoryStatus());
        productReportDto.setPriceAnalysis(productRepository.findPriceAnalysis());
        return productReportDto;
    }
}
