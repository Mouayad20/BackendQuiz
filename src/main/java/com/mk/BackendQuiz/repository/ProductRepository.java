package com.mk.BackendQuiz.repository;

import com.mk.BackendQuiz.dto.Reporting.InventoryStatus;
import com.mk.BackendQuiz.dto.Reporting.PriceAnalysis;
import com.mk.BackendQuiz.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT  NEW com.mk.BackendQuiz.dto.Reporting.InventoryStatus(\n" +
            "    WEEK(p.creationData) ,\n" +
            "    COUNT(p.id) )\n" +
            "FROM\n" +
            "    Product p \n" +
            "GROUP BY\n" +
            "    WEEK(p.creationData)\n" +
            "ORDER BY\n" +
            "    WEEK(p.creationData)"
    )
    List<InventoryStatus> findInventoryStatus();

    @Query("SELECT NEW com.mk.BackendQuiz.dto.Reporting.PriceAnalysis( \n" +
            "    MIN(p.price) ,  \n " +
            "    MAX(p.price) ,  \n " +
            "    AVG(p.price)  ) \n " +
            "    FROM Product p "
    )
    PriceAnalysis findPriceAnalysis();

}