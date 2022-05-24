package com.careerdev.StocksApi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockModel {

    @JsonProperty("Symbol")
    private String symbol;

    @JsonProperty("Name")
    private String name;

    @JsonProperty ("Description")
    private String description;

    @JsonProperty ("Exchange")
    private String exchange;

    @JsonProperty ("Sector")
    private String sector;

    @JsonProperty ("Address")
    private String address;

    @JsonProperty ("MarketCapitalization")
    private long marketCap;


    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExchange() {
        return exchange;
    }

    public String getSector() {
        return sector;
    }

    public String getAddress() {
        return address;
    }

    public long getMarketCap() {
        return marketCap;
    }

    @Override
    public String toString() {
        return "{" +
                "\"symbol\":\"" + symbol + '"' +
                ", \"name\":\"" + name + '"' +
                ", \"description\":\"" + description + '"' +
                ", \"exchange\":\"" + exchange + '"' +
                ", \"sector\":\"" + sector + '"' +
                ", \"address\":\"" + address + '"' +
                ", \"marketCap\":" + marketCap +
                '}';
    }
}


