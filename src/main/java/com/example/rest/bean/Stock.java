package com.example.rest.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
public class Stock {
    @Id
    @GeneratedValue
    private Integer id;

    @Pattern(regexp = "^[a-zA-Z0-9_]*$" , message = "Name must be Alphanumeric" )
    @Size( min = 2, max = 25 , message = "Name should be at least 2 character long and should not exceed 25 char")
    private String name;

    @DecimalMin(value = "0.1", message = "CurrentPrice must be greater or equal to 0.1")
    @NotNull( message = "CurrentPrice can not be Null or Empty")
    private Double currentPrice;

    @Column( columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastUpdate;

    public Stock(){

    }
    public Stock(String name, Double currentPrice){
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Stock(Integer id, String name, Double currentPrice){
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object otherObj){
        if(otherObj == null || ! (otherObj instanceof Stock) )
            return false;
        Stock otherStock = (Stock) otherObj;
        return this.id != null && this.id.equals(otherStock.getId())
                && this.name != null && this.name.equals(otherStock.getName())
                && this.currentPrice != null && this.currentPrice.equals(otherStock.getCurrentPrice());
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
