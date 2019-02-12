package com.example.gemfire.domain;

import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;

@Region("ExampleRegion")
public interface ExampleRegionRepository extends GemfireRepository<ExampleRegion, String> {

}
