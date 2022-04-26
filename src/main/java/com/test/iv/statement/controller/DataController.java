package com.test.iv.statement.controller;

import com.test.iv.statement.dto.GetDataRequest;
import com.test.iv.statement.model.AccountStatement;
import com.test.iv.statement.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    private DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public List<AccountStatement> getData(GetDataRequest request){
        return dataService.getData(request);
    }
}
