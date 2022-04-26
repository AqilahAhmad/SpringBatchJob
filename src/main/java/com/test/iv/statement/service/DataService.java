package com.test.iv.statement.service;

import com.test.iv.statement.dto.GetDataRequest;
import com.test.iv.statement.model.AccountStatement;

import java.util.List;

public interface DataService {

    List<AccountStatement> getData(GetDataRequest request);
}
