/*
 * JEB Copyright PNF Software, Inc.
 * 
 *     https://www.pnfsoftware.com
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

package com.pnf.plugin.macho.internal;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class Section extends StreamReader {
    protected int size;
    protected int offset;
    protected StringTableSection nameTable;
    protected ByteArrayInputStream stream;
    protected byte[] data;

    public Section(byte[] data, int size, int offset) {
        // Setup member variables and set stream to start of section
        stream = new ByteArrayInputStream(data);
        stream.skip(offset);
        this.size = size;
        this.offset = offset;
        this.data = data;
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(data, offset, offset + size);
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    public void setNameTable(StringTableSection nameTable) {
        this.nameTable = nameTable;
    }
}
