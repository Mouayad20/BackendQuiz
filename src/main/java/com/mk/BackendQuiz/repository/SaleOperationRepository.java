package com.mk.BackendQuiz.repository;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import com.mk.BackendQuiz.dto.Reporting.SalesReportDto;
import com.mk.BackendQuiz.model.SaleOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SaleOperationRepository extends JpaRepository<SaleOperation, Long> {

    @Query(value = "SELECT NEW com.mk.BackendQuiz.dto.Reporting.SalesReportDto(COUNT(*) , SUM(total)) \n " +
            "         FROM SaleOperation                                                              \n " +
            "        WHERE creationData >= ?1 AND creationData <= ?2                                  \n ")
    SalesReportDto findTotals(Date from, Date to);

    @Query(
            "SELECT t.product                                   \n " +
                    " FROM Transaction t                        \n " +
                    " Where t.saleOperation.creationData >= ?1  \n " +
                    "  AND  t.saleOperation.creationData <= ?2  \n " +
                    " GROUP BY t.product.id                     \n " +
                    " ORDER BY COUNT(*) DESC                    \n "
    )
    List<ProductFetchDto> findTopSellingProducts(Date from, Date to);

    @Query(
            " SELECT s.seller                       \n " +
                    " FROM SaleOperation s          \n " +
                    " Where s.creationData >= ?1    \n " +
                    "  AND  s.creationData <= ?2    \n " +
                    " GROUP BY s.seller.id          \n " +
                    " ORDER BY COUNT(*) DESC        \n "
    )
    List<ClientDto> findTopPerformingSellers(Date from, Date to);
}