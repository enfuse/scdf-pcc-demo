package com.example.gemfire.domain;


import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

public class ExampleRegion implements PdxSerializable {

    public static final String VALUE = "value";

    @Override
    public void toData(PdxWriter writer) {
        writer.writeString(VALUE, value);
    }

    @Override
    public void fromData(PdxReader reader) {
        reader.readString(value);
    }

    @Id
    private String value;

    @PersistenceConstructor
    public ExampleRegion(String value) {
        this.value = value;
    }


}
