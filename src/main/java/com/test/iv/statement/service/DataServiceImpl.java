package com.test.iv.statement.service;

import com.test.iv.statement.dto.GetDataRequest;
import com.test.iv.statement.model.AccountStatement;
import com.test.iv.statement.model.AccountStatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    private AccountStatementRepository accountStatementRepository;

    @Autowired
    public DataServiceImpl(AccountStatementRepository accountStatementRepository) {
        this.accountStatementRepository = accountStatementRepository;
    }

    @Override
    public List<AccountStatement> getData(GetDataRequest request) {
        log.info("Retrieving data for {}", request);

        if (request.getPageSize() == 0) {
            request.setPageSize(10);
        }
        if (request.getPageNumber() == 0) {
            request.setPageNumber(1);
        }

        Pageable pageable = PageRequest.of(request.getPageNumber()-1, request.getPageSize());

        List<AccountStatement> accountStatementList = accountStatementRepository.findByCustomerIdAndAccountNumberAndDescription(
                request.getCustomerId(), request.getAccountNumber(), request.getDescription(), pageable);

        log.info("{} record is retrieved", accountStatementList.size());
        return accountStatementList;
    }
}
