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

package com.pnf.plugin.macho;

import com.pnf.plugin.macho.internal.MachO;
import com.pnf.plugin.macho.internal.ProgramHeader;
import com.pnf.plugin.macho.internal.SectionHeader;
import com.pnfsoftware.jeb.core.units.codeobject.ISegmentInformation;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class MachOSectionInfo implements ISegmentInformation {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(MachOSectionInfo.class);

    private String name;
    private int flags;
    private long fileOffset;
    private long memOffset;
    private long fileSize;
    private long memSize;

    public MachOSectionInfo(SectionHeader elfsection) {
        name = elfsection.getName();
        int elfFlags = elfsection.getFlags();
        this.flags = ((elfFlags & MachO.SHF_EXECINSTR) != 0 ? FLAG_EXECUTE : 0) | ((elfFlags & MachO.SHF_ALLOC) != 0 ? FLAG_READ : 0)
                | ((elfFlags & MachO.SHF_WRITE) != 0 ? FLAG_WRITE : 0);
        fileOffset = elfsection.getOffset();
        memOffset = elfsection.getAddress();
        if(elfsection.getType() == MachO.SHT_NOBITS) {
            fileSize = 0;
            memSize = elfsection.getSize();
        }
        else {
            fileSize = elfsection.getSize();
            memSize = elfsection.getSize();
        }
    }

    public MachOSectionInfo(ProgramHeader elfsection) {
        name = "";
        int elfFlags = elfsection.getFlags();
        this.flags = ((elfFlags & MachO.PF_X) != 0 ? FLAG_EXECUTE : 0) | ((elfFlags & MachO.PF_R) != 0 ? FLAG_READ : 0)
                | ((elfFlags & MachO.PF_W) != 0 ? FLAG_WRITE : 0);
        fileOffset = elfsection.getOffsetInFile();
        memOffset = elfsection.getVirtualAddress();
        fileSize = elfsection.getSizeInFile();
        memSize = elfsection.getSizeInMemory();
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getOffsetInFile() {
        return fileOffset;
    }

    @Override
    public long getOffsetInMemory() {
        return memOffset;
    }

    @Override
    public long getSizeInFile() {
        return fileSize;
    }

    @Override
    public long getSizeInMemory() {
        return memSize;
    }

}
