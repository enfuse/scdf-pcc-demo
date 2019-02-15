package io.enfuse.pipeline.geode.geodestreamprocessor.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

/**
 * TODO:
 *  A common design strategy in Gemfire applications is to use standard Java wrappers, such as Integer or Long, or String
 *  as the key class.  A better solution is to use a different key class for each domain class.  For example, an Account
 *  class should have an accompanying AccountKey class.  This has several advantages:
 *  The key-constraint attribute of the region ensures the right value is used when putting data into a region.
 *  If a String or Long is used it can easily be the wrong value.
 *  If ID is changed (such as from String to Long) all of the method signatures that have the key class do not have to be changed.
 *  Compound key structures that are used with co-location can be created.
 *
 *  https://github.com/Pivotal-Field-Engineering/gemfire-fe/blob/master/docs/Gemfire%20Tips%20and%20Tricks.docx
 *
 */

@Region("exampleRegion")
public class ExampleEntity {
    @Id
    private Long id;

    private String value;

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

    @Override
    public String toString() {
        return "ExampleEntity{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
