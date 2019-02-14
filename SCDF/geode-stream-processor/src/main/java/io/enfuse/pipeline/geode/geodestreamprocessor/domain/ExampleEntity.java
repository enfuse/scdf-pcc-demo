package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("exampleRegion")
public class ExampleEntity {
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
}
