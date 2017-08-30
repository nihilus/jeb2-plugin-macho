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
import java.util.ArrayList;
import java.util.List;

import com.pnfsoftware.jeb.core.units.UnitNotification;

public class ProgramHeaderTable extends StreamReader {
    // private int offset;
    // private int entrySize;
    // private int number;
    private List<ProgramHeader> entries = new ArrayList<>();

    public ProgramHeaderTable(byte[] data, int offset, int entrySize, int number, List<UnitNotification> notifications) {

        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        stream.skip(offset);

        for(int index = 0; index < number; index++) {
            entries.add(new ProgramHeader(data, offset + entrySize * index, notifications));
        }
    }

    public List<ProgramHeader> getHeaders() {
        return entries;
    }
}
