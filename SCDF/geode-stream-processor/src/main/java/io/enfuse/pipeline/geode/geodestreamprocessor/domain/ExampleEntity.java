package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region("exampleRegion")
public class ExampleEntity {
    @Id
    private String id;

    private String value;

    public ExampleEntity(){

    }

    public ExampleEntity(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExampleEntity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
