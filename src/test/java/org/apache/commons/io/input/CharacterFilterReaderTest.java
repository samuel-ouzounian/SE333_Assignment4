/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.io.input;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CharacterFilterReaderTest {

    @Test
    public void testInputSize0FilterSize1() throws IOException {
        final StringReader input = new StringReader("");
        final HashSet<Integer> codePoints = new HashSet<>();
        codePoints.add(Integer.valueOf('a'));
        try (CharacterFilterReader reader = new CharacterFilterReader(input, 'A')) {
            assertEquals(-1, reader.read());
            char[] buffer = new char[10];
            int read = reader.read(buffer, 0, buffer.length);
            assertEquals(-1, read);
        }
    }

    @Test
    public void testInputSize1FilterSize1() throws IOException {
        try (StringReader input = new StringReader("a");
                CharacterFilterReader reader = new CharacterFilterReader(input, 'a')) {
            assertEquals(-1, reader.read());
        }
    }

    @Test
    public void testInputSize2FilterSize1FilterAll() throws IOException {
        final StringReader input = new StringReader("aa");
        try (CharacterFilterReader reader = new CharacterFilterReader(input, 'a')) {
            assertEquals(-1, reader.read());
        }
    }

    @Test
    public void testInputSize2FilterSize1FilterFirst() throws IOException {
        final StringReader input = new StringReader("abc");
        try (CharacterFilterReader reader = new CharacterFilterReader(input, 'a')) {
            assertEquals('b', reader.read());
            char[] buffer = new char[3];
            int read = reader.read(buffer, 0, buffer.length);
            assertEquals(1, read);
            assertEquals("c", new String(buffer, 0, read));

            assertEquals(-1, reader.read());
        }
    }

    @Test
    public void testInputSize2FilterSize1FilterLast() throws IOException {
        final StringReader input = new StringReader("ab");
        try (CharacterFilterReader reader = new CharacterFilterReader(input, 'b')) {
            assertEquals('a', reader.read());
            assertEquals(-1, reader.read());
        }
    }
    @Test
    public void testFilteringWithGaps() throws IOException {
        final StringReader input = new StringReader("abcd");
        try (CharacterFilterReader reader = new CharacterFilterReader(input, 'b')) {
            char[] buffer = new char[4];
            int read = reader.read(buffer, 0, buffer.length);
            assertEquals(3, read);
            assertEquals('a', buffer[0]);
            assertEquals('c', buffer[1]);
        }
    }
}
