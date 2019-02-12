package com.example.gemfire;

import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;

@Region("ExampleRegion")
public interface ExampleRegionRepository extends GemfireRepository<ExampleRegion, String> {

}
