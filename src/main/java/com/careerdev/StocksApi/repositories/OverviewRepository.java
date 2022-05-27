package com.careerdev.StocksApi.repositories;

import com.careerdev.StocksApi.models.Overview;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OverviewRepository extends CrudRepository<Overview,Long> {

    public Optional<Overview> findBySymbol (String symbol);

    public Optional<Overview> findByName (String name);

    public List<Overview> findByExchange (String exchange);

    public List<Overview> findByAssetType (String assetType);

    public List<Overview> findByCurrency (String currency);

    public List<Overview> findByCountry (String country);

    public List<Overview> findBySector (String sector);

    //public Long removeBySymbol (String symbol);


    /*
 "Symbol"X
"AssetType"X
"Name"X
"Exchange"X
"Currency"X
"Country"X
"Sector"
* */

}
