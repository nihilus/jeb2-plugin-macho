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

public class NoteSectionEntry extends StreamReader {
    private int nameSize;
    private int descSize;
    // private int type;
    private int size;
    private String name = "";
    private String desc = "";

    public NoteSectionEntry(byte[] data, int offset) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        stream.skip(offset);

        nameSize = readInt(stream);
        descSize = readInt(stream);
        /* type = */readInt(stream);

        byte[] nameBytes = new byte[nameSize];
        stream.read(nameBytes, 0, nameSize);
        name = new String(nameBytes);

        // Skip the padding bytes, aligned to 4 byte words
        stream.skip(4 - nameSize % 4);

        if(descSize > 0) {
            byte[] descBytes = new byte[descSize];
            stream.read(descBytes, 0, descSize);
            desc = new String(descBytes);
        }
        size = data.length - stream.available() - offset;

    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return String.format("-- %s -- \n%s", name, desc);
    }
}
