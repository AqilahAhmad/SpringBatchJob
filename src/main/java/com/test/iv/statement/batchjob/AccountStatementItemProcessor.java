package com.test.iv.statement.batchjob;

import com.test.iv.statement.model.AccountStatement;
import org.springframework.batch.item.ItemProcessor;

public class AccountStatementItemProcessor implements ItemProcessor<AccountStatement, AccountStatement> {

    @Override
    public AccountStatement process(final AccountStatement accountStatement) {
        return accountStatement;
    }
}
