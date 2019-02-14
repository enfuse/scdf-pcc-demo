package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.gemfire.mapping.annotation.ClientRegion;
import org.springframework.data.repository.CrudRepository;

@ClientRegion("exampleRegion")
public interface ExampleRepository extends CrudRepository<ExampleEntity, Long> {
}
