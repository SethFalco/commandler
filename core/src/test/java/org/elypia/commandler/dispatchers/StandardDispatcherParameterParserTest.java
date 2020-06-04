/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.commandler.dispatchers;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author seth@elypia.org (Seth Falco)
 */
public class StandardDispatcherParameterParserTest {

    @Test
    public void testParseNormalParameters() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("hello world")
        );
        List<List<String>> actual = parser.parse("\"hello world\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testParse4Params() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("hello"),
            List.of("world"),
            List.of("four"),
            List.of("parameters")
        );
        List<List<String>> actual = parser.parse("hello world four parameters");

        assertEquals(expected, actual);
    }

    @Test
    public void testMixBetweenQuotesAndNonQuotes() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("hello world"),
            List.of("world"),
            List.of("does this work")
        );
        List<List<String>> actual = parser.parse("\"hello world\" world \"does this work\"");

        assertEquals(expected, actual);
    }

//    @Test
    public void testEscapedQuotes() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("\"hello world"),
            List.of("\"no u")
        );
        List<List<String>> actual = parser.parse("\"\\\"hello world\" \"\\\"no u\"");

        assertEquals(expected, actual);
    }

    /**
     * Regex can sometimes cause StackOverflows when handling
     * large repetitive input.
     */
    @RepeatedTest(4)
    public void testLargeRepetitiveInput() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world!")
        );
        List<List<String>> actual = parser.parse("\"Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world!\"");

        assertEquals(expected, actual);
    }

    @Test
    public void testListOfItems() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("this", "is", "a", "single", "parameter")
        );
        List<List<String>> actual = parser.parse("this, is, a, single, parameter");

        assertEquals(expected, actual);
    }

    /**
     * Ensure that this is still picked up as one parameter
     * instead of two.
     */
    @Test
    public void testSingleQuotesItemWithComma() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("This is a simple String, but contains a comma!")
        );
        List<List<String>> actual = parser.parse("\"This is a simple String, but contains a comma!\"");

        assertEquals(expected, actual);
    }

    /**
     * We don't split list items unless there is a space after the comma
     * as this can be intentional, such as with numbers.
     */
    @Test
    public void testListOfItemsWithThousandSeperatedNumbers() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("100"),
            List.of("2,000"),
            List.of("12,000"),
            List.of("20,000", "30,000")
        );
        List<List<String>> actual = parser.parse("100 2,000 12,000 20,000, 30,000");

        assertEquals(expected, actual);
    }

    @Test
    public void testBlankParams() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of();
        List<List<String>> actual = parser.parse("");

        assertEquals(expected, actual);
    }

//    @Test
    public void testStringWithEscapedQuote() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<List<String>> expected = List.of(
            List.of("Hello world!\"Hello world!")
        );
        List<List<String>> actual = parser.parse("\"Hello world!\\\"Hello world!\"");

        assertEquals(expected, actual);
    }

    /**
     * Regex can sometimes cause StackOverflows when handling
     * large repetitive input.
     *
     * This is the same test as before with a long string, but
     * we only check if item splitting is good.
     */
    @RepeatedTest(4)
    public void testLargeRepetitiveStringItemOnly() {
        StandardDispatcherParameterParser parser = new StandardDispatcherParameterParser();

        List<String> expected = List.of("Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world!");
        List<String> actual = parser.parseItems("\"Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world! Hello world!\"");

        assertEquals(expected, actual);
    }
}
