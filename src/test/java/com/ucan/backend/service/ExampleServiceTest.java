package com.ucan.backend.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleServiceTest {

    @Test
    void exampleTestMethod() {
        // Given
        String expectedValue = "Hello JUnit";
        String actualValue = "Hello JUnit";


        // Assert that the outcome is as expected
        assertEquals(expectedValue, actualValue, "The greeting should match");
    }

    @Test
    void anotherExampleTest() {
        // Add another test case
        assertEquals(4, 2 + 2, "2 + 2 should equal 4");
    }
}