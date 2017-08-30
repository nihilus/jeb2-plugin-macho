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
import java.util.List;
import com.pnfsoftware.jeb.core.units.NotificationType;
import com.pnfsoftware.jeb.core.units.UnitNotification;

public class ProgramHeader extends StreamReader {
    private int type;
    private String typeString;
    private int offset;
    private int vaddr;
    private int paddr;
    private int fileSize;
    private int memorySize;
    private int flags;
    private String flagsString;
    private int align;
    private Section section;

    public int getType() {
        return type;
    }

    public String getTypeString() {
        return typeString;
    }

    public int getOffsetInFile() {
        return offset;
    }

    public int getVirtualAddress() {
        return vaddr;
    }

    public int getPhysicalAddress() {
        return paddr;
    }

    public int getSizeInFile() {
        return fileSize;
    }

    public int getSizeInMemory() {
        return memorySize;
    }

    public int getFlags() {
        return flags;
    }

    public String getFlagsString() {
        return flagsString;
    }

    public int getAlign() {
        return align;
    }

    public Section getSection() {
        return section;
    }

    public ProgramHeader(byte[] data, int sectionOffset, List<UnitNotification> notifications) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        stream.skip(sectionOffset);
        type = readInt(stream);
        offset = readInt(stream);
        vaddr = readInt(stream);
        paddr = readInt(stream);
        fileSize = readInt(stream);
        memorySize = readInt(stream);
        flags = readInt(stream);

        flagsString = "";
        flagsString = String.format("%c%c%c", (flags & ELF.PF_X) != 0 ? 'E' : ' ', (flags & ELF.PF_W) != 0 ? 'W' : ' ',
                (flags & ELF.PF_R) != 0 ? 'R' : ' ');

        align = readInt(stream);

        typeString = ELF.getPTString(type);

        if(type == ELF.PT_GNU_STACK && (flags & ELF.PF_X) != 0) {
            notifications.add(new UnitNotification(NotificationType.AREA_OF_INTEREST, "Stack is executable"));
        }
    }
}
