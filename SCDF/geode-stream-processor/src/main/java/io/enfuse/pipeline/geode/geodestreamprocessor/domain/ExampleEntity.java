package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleEntity that = (ExampleEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ExampleEntity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
