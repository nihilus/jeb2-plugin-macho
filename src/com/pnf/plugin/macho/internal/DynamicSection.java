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

import java.util.ArrayList;
import java.util.List;

public class DynamicSection extends Section {
    private List<DynamicSectionEntry> entries = new ArrayList<>();

    @SuppressWarnings("unused")
    private SectionHeader stringTable;

    public DynamicSection(byte[] data, int size, int offset, int sh_entsize) {
        super(data, size, offset);

        for(int index = offset; index < offset + size; index += sh_entsize) {
            entries.add(new DynamicSectionEntry(data, sh_entsize, index));
        }
    }

    public List<DynamicSectionEntry> getEntries() {
        return entries;
    }

    public void setStringTable(SectionHeader stringTable) {
        this.stringTable = stringTable;
        for(DynamicSectionEntry entry : entries) {
            entry.setStringTable(stringTable);
        }
    }
}
