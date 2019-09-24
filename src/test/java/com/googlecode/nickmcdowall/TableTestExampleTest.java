package com.googlecode.nickmcdowall;

import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.SpecListener;
import com.googlecode.yatspec.junit.Table;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpecListener.class)
public class TableTestExampleTest {

    @ParameterizedTest
    @Table({
            @Row({"Jeremy",  "Clarkson",    "Jeremy Clarkson"}),
            @Row({"Richard", "Hammond",     "Richard Hammond"}),
            @Row({"James",   "May",         "James May"})
    })
    public void parametersFromTheTableTestAreCorrectlyMapped(String firstName, String lastName, String expectedFullName) {
        String combinedFullName = format("%s %s", firstName, lastName);

        assertThat(combinedFullName, equalTo(expectedFullName));
    }

}
