package com.test.iv.statement.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetDataRequest {
    private int pageSize;
    private int pageNumber;
    private Long customerId;
    private List<Long> accountNumber;
    private String description;
}
