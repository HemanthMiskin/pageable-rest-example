package com.example.rest.util;

public interface StockConstants {
    String URL_ROOT = "/api/stocks";
    String STOCKS_PER_PAGE = "stocksPerPage";
    String PAGE_NUMBER = "pageNumber";
    String URL_PAGEABLE_STOCKS = URL_ROOT+"/"+STOCKS_PER_PAGE+"/{"+STOCKS_PER_PAGE+"}/"+PAGE_NUMBER+"/{"+PAGE_NUMBER+"}";
    String URL_GET_STOCK = URL_ROOT+"/{id}";
    String URL_PATCH_STOCK = URL_ROOT+"/{id}";
    String URL_DELETE_STOCK = URL_ROOT+"/{id}";

}
