package com.careerdev.StocksApi.repositories;

import com.careerdev.StocksApi.models.Overview;
import org.springframework.data.repository.CrudRepository;

public interface OverviewRepository extends CrudRepository<Overview,Long> {
}
