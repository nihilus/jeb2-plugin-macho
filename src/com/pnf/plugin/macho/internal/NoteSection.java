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

public class NoteSection extends Section {
    @SuppressWarnings("unused")
    private int entrySize;
    private List<NoteSectionEntry> entries = new ArrayList<>();

    public NoteSection(byte[] data, int size, int offset, int entrySize) {
        super(data, size, offset);

        int entryOffset = 0;
        // int nameSize;
        // int descSize;
        NoteSectionEntry entry;
        while(entryOffset < size) {
            entry = new NoteSectionEntry(data, entryOffset + offset);
            entries.add(entry);
            entryOffset += entry.getSize();
        }
    }

    public List<NoteSectionEntry> getEntries() {
        return entries;
    }

    public String getText() {
        String output = "";
        for(NoteSectionEntry entry : getEntries()) {
            output = output + entry.toString() + "\n";
        }
        return output;
    }
}
