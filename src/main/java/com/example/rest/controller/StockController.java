package com.example.rest.controller;

import com.example.rest.bean.PageableStock;
import com.example.rest.bean.Stock;
import com.example.rest.exception.*;
import com.example.rest.repository.StockRepository;
import com.example.rest.util.StockConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class StockController {


    @Autowired
    private StockRepository repository;

    Logger logger = LoggerFactory.getLogger(StockController.class);

    @GetMapping( path = {StockConstants.URL_ROOT,StockConstants.URL_PAGEABLE_STOCKS})
    public PageableStock getStocks(@PathVariable Map<String,String> pathVariableMap) throws RuntimeException{
        logger.info("Request received for stock listing. Input:"+pathVariableMap);

        int stocksPerPage = getFromVariableMap(pathVariableMap, StockConstants.STOCKS_PER_PAGE);
        int pageNumber = getFromVariableMap(pathVariableMap,StockConstants.PAGE_NUMBER);

        if(stocksPerPage == 0 && pageNumber == 0 ) {
            List<Stock> stocks = repository.findAll();
            if(stocks == null || stocks.isEmpty()) {
                throw new StockNotFoundException();
            }

            PageableStock pageableStock = new PageableStock();
            pageableStock.setStocks(stocks);
            pageableStock.setTotalPages(1);
            pageableStock.setTotalElements(stocks.size());
            return pageableStock;
        }else{
            /* Manual pagination , can be used in-case any data transformation needed.
            return repository.findAll().stream()
                    .skip(stocksPerPage * (pageNumber-1))
                    .limit(stocksPerPage)
                    .collect(Collectors.toList());*/

            Pageable pageable = PageRequest.of(pageNumber-1,stocksPerPage);
            Page<Stock> stocksPage = repository.findAll(pageable);
            if(stocksPage.getContent() == null || stocksPage.getContent().isEmpty())
                throw new StockNotFoundException();

            PageableStock pageableStock = new PageableStock();
            pageableStock.setStocks(stocksPage.getContent());
            pageableStock.setTotalPages(stocksPage.getTotalPages());
            pageableStock.setTotalElements(stocksPage.getTotalElements());
            return pageableStock;
        }
    }

    private int getFromVariableMap(Map<String, String> pathVariableMap, String variable) {
        int result = 0;
        try {
            if(pathVariableMap.get(variable) != null)
                result = Integer.parseInt(pathVariableMap.get(variable));
        }catch (NumberFormatException nfe){
            throw new PaginationReqException();
        }
        return result;
    }

    @GetMapping( path = StockConstants.URL_GET_STOCK)
    public Stock getStock(@PathVariable Integer id) throws RuntimeException {
        logger.info("Request received for Stock details for Id:"+id);

        Optional<Stock> stockOpt = repository.findById(id);
        if(stockOpt.isPresent())
            return stockOpt.get();
        else
            throw new StockNotFoundException(id);

    }

    @PostMapping( path = StockConstants.URL_ROOT)
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock){
        logger.info("Request received for Stock Creation. Input:"+stock);
        try {
            stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            Stock savedStock = repository.save(stock);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedStock.getId())
                    .toUri();
            return ResponseEntity.created(location).body(savedStock);
        }catch (Exception ex){
            logger.error("Exception occurred while creating stock!",ex);
            throw new UnableToCreateStockException("Unable to create stock!");
        }
    }

    @PatchMapping( path = StockConstants.URL_PATCH_STOCK)
    public ResponseEntity<Stock> updateStock(@PathVariable Integer id, @RequestBody Stock stock){
        logger.info("Request received for Stock Update for Id:"+id+" with new price:"+stock.getCurrentPrice());
        Stock stockToUpdate = this.getStock(id);
        try {
            stockToUpdate.setCurrentPrice(stock.getCurrentPrice());
            stockToUpdate.setLastUpdate(new Timestamp(System.currentTimeMillis()));
            Stock updatedStock = repository.save(stockToUpdate);
            return ResponseEntity.ok().body(updatedStock);
        }catch (Exception ex){
            logger.error("Exception occurred while updating stock!",ex);
            throw new UnableToUpdateStockException(id);
        }
    }

    @DeleteMapping( path = StockConstants.URL_DELETE_STOCK)
    public ResponseEntity<String> deleteStock(@PathVariable Integer id){
        logger.info("Request received for Stock Delete for Id:"+id);
        this.getStock(id);
        try {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception ex){
            logger.error("Exception occurred while deleting stock!",ex);
            throw new UnableToDeleteStockException(id);
        }
    }
}
