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

public class DynamicSectionEntry extends StreamReader {
    private int tag;
    private String tagString;
    private int un;
    private int val;
    private String valString;
    private int ptr;

    public int getTag() {
        return tag;
    }

    public String getTagString() {
        return tagString;
    }

    public int getUn() {
        return un;
    }

    public int getVal() {
        return val;
    }

    public String getValString() {
        return valString;
    }

    public int getPtr() {
        return ptr;
    }

    public DynamicSectionEntry(byte[] data, int size, int offset) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        stream.skip(offset);
        stream.mark(0);

        tag = readInt(stream);
        tagString = MachO.getDT(tag);
        un = readInt(stream);

        switch(tag) {
        case MachO.DT_NULL:
            val = un;
            break;
        case MachO.DT_NEEDED:
            val = un;
            break;
        case MachO.DT_PLTRELSZ:
            ptr = un;
            break;
        case MachO.DT_PLTGOT:
            ptr = un;
            break;
        case MachO.DT_HASH:
            ptr = un;
            break;
        case MachO.DT_STRTAB:
            ptr = un;
            break;
        case MachO.DT_SYMTAB:
            ptr = un;
            break;
        case MachO.DT_RELA:
            ptr = un;
            break;
        case MachO.DT_RELASZ:
            val = un;
            break;
        case MachO.DT_RELAENT:
            val = un;
            break;
        case MachO.DT_STRSZ:
            val = un;
            break;
        case MachO.DT_SYMENT:
            val = un;
            break;
        case MachO.DT_INIT:
            ptr = un;
            break;
        case MachO.DT_FINI:
            ptr = un;
            break;
        case MachO.DT_SONAME:
            val = un;
            break;
        case MachO.DT_RPATH:
            val = un;
            break;
        case MachO.DT_SYMBOLIC:
            break;
        case MachO.DT_REL:
            ptr = un;
            break;
        case MachO.DT_RELSZ:
            val = un;
            break;
        case MachO.DT_RELENT:
            val = un;
            break;
        case MachO.DT_PLTREL:
            val = un;
            break;
        case MachO.DT_DEBUG:
            ptr = un;
            break;
        case MachO.DT_TEXTREL:
            break;
        case MachO.DT_JMPREL:
            ptr = un;
            break;
        case MachO.DT_LOPROC:
            break;
        case MachO.DT_HIPROC:
            break;
        default:
            break;
        }
    }

    public void setStringTable(SectionHeader stringTable) {
        switch(tag) {
        case MachO.DT_NEEDED:
        case MachO.DT_SONAME:
        case MachO.DT_RPATH:
            if(stringTable.getSection() instanceof StringTableSection) {
                valString = ((StringTableSection)stringTable.getSection()).getString(val);
                break;
            }
            //$FALL-THROUGH$
        default:
            valString = Integer.toHexString(un);
        }
    }
}
