package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.gemfire.mapping.annotation.Region;
import org.springframework.data.gemfire.repository.GemfireRepository;

@Region("exampleRegion")
public interface ExampleRepository extends GemfireRepository<ExampleEntity, String> {

}
