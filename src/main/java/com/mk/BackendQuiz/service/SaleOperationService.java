package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import com.mk.BackendQuiz.dto.Reporting.SalesReportDto;
import com.mk.BackendQuiz.dto.Sales.SaleOperationDto;
import com.mk.BackendQuiz.dto.Sales.SaleOperationFetchDto;
import com.mk.BackendQuiz.dto.Sales.TransactionDto;
import com.mk.BackendQuiz.exception.EntityType;
import com.mk.BackendQuiz.exception.ExceptionManager;
import com.mk.BackendQuiz.exception.ExceptionType;
import com.mk.BackendQuiz.model.Client;
import com.mk.BackendQuiz.model.Product;
import com.mk.BackendQuiz.model.SaleOperation;
import com.mk.BackendQuiz.model.Transaction;
import com.mk.BackendQuiz.repository.ClientRepository;
import com.mk.BackendQuiz.repository.ProductRepository;
import com.mk.BackendQuiz.repository.SaleOperationRepository;
import com.mk.BackendQuiz.repository.TransactionRepository;
import com.mk.BackendQuiz.security.JwtTokenUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.mk.BackendQuiz.exception.EntityType.*;
import static com.mk.BackendQuiz.exception.ExceptionType.ENTITY_EXCEPTION;
import static com.mk.BackendQuiz.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class SaleOperationService {
    @Autowired
    private SaleOperationRepository saleOperationRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Page<SaleOperationFetchDto> fetchSaleOperations(Pageable pageable) {
        return saleOperationRepository
                .findAll(pageable)
                .map(saleOperation ->
                        modelMapper.map(saleOperation, SaleOperationFetchDto.class)
                );
    }

    public SaleOperationDto createSaleOperation(List<TransactionDto> transactions, String token) {
        if (transactions.isEmpty())
            throw exception(TRANSACTION, ENTITY_EXCEPTION, "There must be at least one transaction.");

        String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isEmpty()) {
            throw exception(CLIENT, ENTITY_NOT_FOUND, email);
        }

        SaleOperation saleOperation = new SaleOperation();
        saleOperation.setCreationData(new Date());
        saleOperation.setClient(client.get());
        saleOperation.setTransactions(transactions
                .stream()
                .map(transactionDto -> {

                            Optional<Product> product = productRepository.findById(transactionDto.getProduct().getId());
                            if (product.isEmpty()) {
                                throw exception(PRODUCT, ENTITY_NOT_FOUND, "found");
                            }
                            if (saleOperation.getSeller() == null) {
                                saleOperation.setSeller(product.get().getPublisher());
                            }

                            transactionDto.setPrice(product.get().getPrice() * transactionDto.getQuantity());
                            saleOperation.setTotal(saleOperation.getTotal() + transactionDto.getPrice());

                            long quantity = product.get().getAvailableQuantity() - transactionDto.getQuantity();
                            if (quantity < 0)
                                throw exception(TRANSACTION, ENTITY_EXCEPTION, "The quantity demanded is more than the quantity available.");
                            product.get().setAvailableQuantity(quantity);

                            transactionDto.setProduct(modelMapper.map(productRepository.save(product.get()), ProductFetchDto.class));


                            return modelMapper.map(transactionDto, Transaction.class);
                        }
                ).collect(Collectors.toList())
        );

        saleOperation.getTransactions().forEach(transaction -> transaction.setSaleOperation(saleOperation));

        return modelMapper.map(saleOperationRepository.save(saleOperation), SaleOperationDto.class);
    }

    public SaleOperationDto updateSaleOperation(SaleOperationDto saleOperationDto) {
        Optional<SaleOperation> saleOperation = saleOperationRepository.findById(saleOperationDto.getId());
        if (saleOperation.isEmpty()) {
            throw exception(SALE_OPERATION, ENTITY_NOT_FOUND, "found");
        }

        saleOperation.get().setTotal(0.0);
        saleOperationDto
                .getTransactions()
                .forEach(transactionDto -> {

                    Optional<Transaction> transaction = transactionRepository.findById(transactionDto.getId());
                    if (transaction.isEmpty())
                        throw exception(TRANSACTION, ENTITY_NOT_FOUND, "found");

                    Optional<Product> product = productRepository.findById(transactionDto.getProduct().getId());
                    if (product.isEmpty()) {
                        throw exception(PRODUCT, ENTITY_NOT_FOUND, "found");
                    }

                    if (transactionDto.getPrice() != null) {
                        transaction.get().setPrice(transactionDto.getPrice());
                    }

                    if (transactionDto.getQuantity() != null) {

                        if (transaction.get().getQuantity() >= transactionDto.getQuantity()) {
                            product.get().setAvailableQuantity(
                                    product.get().getAvailableQuantity()
                                            + (transaction.get().getQuantity() - transactionDto.getQuantity())
                            );
                        } else {
                            long quantity = product.get().getAvailableQuantity() - (transactionDto.getQuantity() - transaction.get().getQuantity());
                            if (quantity < 0)
                                throw exception(TRANSACTION, ENTITY_EXCEPTION, "The quantity demanded is more than the quantity available.");

                            product.get().setAvailableQuantity(quantity);
                        }

                        productRepository.save(product.get());

                        transaction.get().setQuantity(transactionDto.getQuantity());
                    }

                    saleOperation.get().setTotal(saleOperation.get().getTotal() + transaction.get().getPrice());

                    transactionRepository.save(transaction.get());

                });

        return modelMapper.map(saleOperationRepository.save(saleOperation.get()), SaleOperationDto.class);
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return ExceptionManager.throwException(entityType, exceptionType, args);
    }

    public SalesReportDto saleReport(Date from, Date to) {
        SalesReportDto salesReportDto = saleOperationRepository.findTotals(from, to);

        salesReportDto.setTopSellingProducts(
                new ArrayList<>(saleOperationRepository.findTopSellingProducts(from, to))
        );
        salesReportDto.setTopPerformingSellers(
                saleOperationRepository.findTopPerformingSellers(from, to)
                        .stream()
                        .limit(5)
                        .collect(Collectors.toList())
        );

        return salesReportDto;
    }
}
