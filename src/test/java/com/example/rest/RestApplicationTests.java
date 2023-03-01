package com.example.rest;

import com.example.rest.bean.PageableStock;
import com.example.rest.util.StockConstants;
import com.example.rest.bean.Stock;
import com.example.rest.repository.StockRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private StockRepository repository;

	@Before("")
	public void setup() {
		testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	@Test
	void contextLoads() {
		assert testRestTemplate != null;
		assert repository != null;
	}

	@Test
	public void testGetStocksAll(){
		ResponseEntity<PageableStock> response =
				testRestTemplate.getForEntity(StockConstants.URL_ROOT,PageableStock.class);
		assert response != null;
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		assert response.hasBody();
		PageableStock pageableStock = response.getBody();
		assert pageableStock != null;
		Assertions.assertEquals(10,pageableStock.getStocks().size());
		Assertions.assertEquals(10,pageableStock.getTotalElements());
		Assertions.assertEquals(1,pageableStock.getTotalPages());
	}

	@Test
	public void testGetStocksPaginated_FirstPage(){
		ResponseEntity<PageableStock> response =
				testRestTemplate.getForEntity(StockConstants.URL_PAGEABLE_STOCKS,PageableStock.class,5,1);
		assert response != null;
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		assert response.hasBody();
		PageableStock pageableStock = response.getBody();
		assert pageableStock != null;
		Assertions.assertEquals(5,pageableStock.getStocks().size());
		Assertions.assertEquals(10,pageableStock.getTotalElements());
		Assertions.assertEquals(2,pageableStock.getTotalPages());

	}

	@Test
	public void testGetStocksPaginated_LastPage(){
		ResponseEntity<PageableStock> response =
				testRestTemplate.getForEntity(StockConstants.URL_PAGEABLE_STOCKS,PageableStock.class,3,4);
		assert response != null;
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		assert response.hasBody();
		PageableStock pageableStock = response.getBody();
		assert pageableStock != null;
		Assertions.assertEquals(1,pageableStock.getStocks().size());
		Assertions.assertEquals(10,pageableStock.getTotalElements());
		Assertions.assertEquals(4,pageableStock.getTotalPages());

	}

	@Test
	public void testGetStocksPaginated_PageNotExist(){
		ResponseEntity<PageableStock> response =
				testRestTemplate.getForEntity(StockConstants.URL_PAGEABLE_STOCKS,PageableStock.class,10,2);
		assert response != null;
		Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}

	@Test
	public void testGetStockById(){
		ResponseEntity<Stock> response =
				testRestTemplate.getForEntity(StockConstants.URL_GET_STOCK,Stock.class,101);
		assert response != null;
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
		assert response.hasBody();
		Stock stockResp = response.getBody();
		assert stockResp != null;
		Stock stock = new Stock(101,"Stock101",100.0);
		Assertions.assertEquals(stock,stockResp);

	}

	@Test
	public void testGetStock_NotExist(){
		ResponseEntity<Stock> response =
				testRestTemplate.getForEntity(StockConstants.URL_GET_STOCK,Stock.class,1010);
		assert response != null;
		Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}

	@Test
	public void testCreateAndDeleteStock(){
		long rowsBefore = repository.count();
		String name = "Stock111";
		Double price = 111.0;
		Stock stockReq = new Stock(name,price);
		ResponseEntity<Stock> createResponse =
				testRestTemplate.postForEntity(StockConstants.URL_ROOT,stockReq,Stock.class);
		assert createResponse != null;
		Assertions.assertEquals(HttpStatus.CREATED,createResponse.getStatusCode());
		long rowsAfter = repository.count();
		assert rowsAfter == (rowsBefore+1);
		assert createResponse.getBody() != null;
		Assertions.assertTrue(createResponse.getBody().getId() > 0);
		Assertions.assertEquals(name,createResponse.getBody().getName());
		Assertions.assertEquals(price,createResponse.getBody().getCurrentPrice());

		//Delete this new Stock so that rest of the tests can depend on DB setup.
		testRestTemplate.delete(StockConstants.URL_DELETE_STOCK,createResponse.getBody().getId());
		rowsAfter = repository.count();
		assert rowsAfter == rowsBefore;
		Optional<Stock> stockOpt = repository.findById(createResponse.getBody().getId());
		Assertions.assertFalse(stockOpt.isPresent());
	}

	@Test
	public void testUpdateStock(){
		Integer id = 102;
		String name = "Stock102";
		Double newPrice = 1010.0;
		Stock stockReq = new Stock(name,newPrice);
		Stock response =
				testRestTemplate.patchForObject(StockConstants.URL_PATCH_STOCK,stockReq,Stock.class,id);
		assert response != null;
		Assertions.assertEquals(id,response.getId());
		Assertions.assertEquals(name,response.getName());
		Assertions.assertEquals(newPrice,response.getCurrentPrice());
	}
}
