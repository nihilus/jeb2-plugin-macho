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
import java.util.HashMap;
import java.util.List;

public class SymbolTableSection extends Section {

    // Wraps the bytes of a Symbol Table
    // Keeps a list of entries for the data
    @SuppressWarnings("unused")
    private int entrySize;
    private List<SymbolTableEntry> entries = new ArrayList<>();

    public SymbolTableSection(byte[] data, int size, int offset, int entrySize, StringTableSection nameTable) {
        super(data, size, offset);
        this.entrySize = entrySize;
        this.nameTable = nameTable;
        for(int index = 0; index < size / entrySize; index++) {
            entries.add(new SymbolTableEntry(data, offset + index * entrySize));
        }
    }

    public SymbolTableEntry getEntry(int index) {
        return entries.get(index);
    }

    public List<SymbolTableEntry> getEntries() {
        return entries;
    }

    @Override
    public void setNameTable(StringTableSection nameTable) {
        super.setNameTable(nameTable);
        for(SymbolTableEntry entry : entries) {
            entry.setName(nameTable);
        }
    }

    public HashMap<Integer, String> toHashMap() {
        HashMap<Integer, String> output = new HashMap<>();
        for(SymbolTableEntry entry : entries) {
            if(entry.getType() == ELF.STT_FUNC)
                output.put(entry.getValue(), entry.getName());
        }
        return output;
    }

}
