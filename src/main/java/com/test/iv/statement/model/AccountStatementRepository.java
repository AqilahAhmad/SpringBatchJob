package com.test.iv.statement.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountStatementRepository extends JpaRepository<AccountStatement, Long> {

    @Query("SELECT a FROM AccountStatement a WHERE 1=1 AND " +
            "(:customerId is null or a.customerId = :customerId) AND " +
            "(coalesce(:accountNumber, null) is null or a.accountNumber in :accountNumber) AND " +
            "(:description is null or a.description = :description)")
    List<AccountStatement> findByCustomerIdAndAccountNumberAndDescription(
            @Param("customerId") Long customerId,
            @Param("accountNumber") List<Long> accountNumber,
            @Param("description") String description,
            Pageable pageable);
}
