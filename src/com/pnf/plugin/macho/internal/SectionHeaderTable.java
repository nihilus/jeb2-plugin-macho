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

import com.pnfsoftware.jeb.core.units.NotificationType;
import com.pnfsoftware.jeb.core.units.UnitNotification;

public class SectionHeaderTable extends StreamReader {
    private short entrySize;
    private int offset;
    private short nameTableIndex;
    private short number;

    private List<SectionHeader> entries = new ArrayList<>();

    private List<SectionHeader> relocations = new ArrayList<>();

    public SectionHeaderTable(byte[] data, int offset, short entrySize, short number, short nameTableIndex,
            List<UnitNotification> notifications) {

        this.offset = offset;
        this.entrySize = entrySize;
        this.number = number;
        this.nameTableIndex = nameTableIndex;
        ByteArrayInputStream stream = new ByteArrayInputStream(data);

        stream.skip(offset);

        stream.mark(0);

        // Get the string name table section information
        stream.skip(nameTableIndex * entrySize);

        NameSectionHeader names = new NameSectionHeader(data, entrySize, offset + nameTableIndex * entrySize);

        StringTableSection nameTable = null;
        SectionHeader header;
        // First pass to grab the name table and initialize sections
        for(int sh_index = 0; sh_index < number; sh_index++) {
            header = new SectionHeader(data, entrySize, offset + sh_index * entrySize, names);
            entries.add(header);
            if(header.getType() == MachO.SHT_RELA || header.getType() == MachO.SHT_REL) {
                relocations.add(header);
            }
        }
        // Second pass to set the name table of all sections and link sections
        for(int index = 0; index < entries.size(); index++) {
            header = entries.get(index);
            if(header.getLink() > entries.size() || header.getLink() < 0) {
                notifications.add(new UnitNotification(NotificationType.CORRUPTION, "sh_link points to a nonexistent section"));
            }
            if(header.getType() == MachO.SHT_DYNSYM || header.getType() == MachO.SHT_SYMTAB) {
                int strtabIndex = header.getLink();
                nameTable = (StringTableSection)(entries.get(strtabIndex).getSection());
                header.setNameTable(nameTable);
            }
            if(header.getType() == MachO.SHT_REL || header.getType() == MachO.SHT_RELA) {
                ((RelocationSection)header.getSection()).setSymbolTable(entries.get(header.getLink()));
            }
            if(header.getType() == MachO.SHT_DYNAMIC) {
                ((DynamicSection)header.getSection()).setStringTable(entries.get(header.getLink()));
            }
        }
    }

    public void doRelocations(byte[] mem) {

        // Third pass to apply relocations
        RelocationSection section;
        offset = 0;
        // int addend;
        // char type;
        for(SectionHeader reloc : relocations) {
            section = (RelocationSection)(reloc.getSection());
            for(RelocationSectionEntry entry : section.getEntries()) {
                entry.doRelocation(mem);
            }
        }
    }

    public SectionHeader getHeader(int index) {
        return entries.get(index);
    }

    public short getEntrySize() {
        return entrySize;
    }

    public int getOffset() {
        return offset;
    }

    public short getNameTableIndex() {
        return nameTableIndex;
    }

    public short getNumber() {
        return number;
    }

    public StringTableSection getStringTable() {
        for(SectionHeader entry : entries) {
            if(entry.getName().equals(".strtab")) {
                return (StringTableSection)(entry.getSection());
            }
        }
        throw new RuntimeException("No string tables found");
    }

    public List<Section> getSections() {
        List<Section> output = new ArrayList<>();
        for(SectionHeader entry : entries) {
            output.add(entry.getSection());
        }
        return output;
    }

    public List<SectionHeader> getHeaders() {
        return entries;
    }

}
