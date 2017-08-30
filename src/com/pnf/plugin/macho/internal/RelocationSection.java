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

public class RelocationSection extends Section {
    @SuppressWarnings("unused")
    private int entrySize;
    @SuppressWarnings("unused")
    private boolean RELA;
    @SuppressWarnings("unused")
    private SymbolTableSection symtab;

    private List<RelocationSectionEntry> entries = new ArrayList<>();

    public RelocationSection(byte[] data, int s_size, int s_offset, int entrySize, boolean RELA) {
        super(data, s_size, s_offset);
        this.entrySize = entrySize;
        this.RELA = RELA;

        for(int index = 0; index < s_size / entrySize; index++) {
            entries.add(new RelocationSectionEntry(data, entrySize, s_offset + entrySize * index, RELA));
        }
    }

    public void setSymbolTable(SectionHeader header) {
        symtab = (SymbolTableSection)header.getSection();
        for(RelocationSectionEntry entry : entries) {
            entry.setSymbolTable(header);
        }
    }

    public List<RelocationSectionEntry> getEntries() {
        return entries;
    }
}
