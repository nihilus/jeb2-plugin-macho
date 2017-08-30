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

import com.pnfsoftware.jeb.core.units.UnitNotification;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

/**
 * Wrapper for ELF Files
 */
public class ELFFile {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(ELFFile.class);

    private Header header;

    private int headerNameStringTable;

    public byte[] image;
    private long baseAddr;

    private List<UnitNotification> notifications;

    public int getHeaderNameStringTable() {
        return headerNameStringTable;
    }

    public List<UnitNotification> getNotifications() {
        return notifications;
    }

    private SectionHeaderTable sectionHeaderTable;
    private ProgramHeaderTable programHeaderTable;
    private List<Section> sections;

    public ELFFile(byte[] data) {
        notifications = new ArrayList<>();
        header = new Header(data);

        sectionHeaderTable = new SectionHeaderTable(data, header.getShoff(), header.getSHEntrySize(), header.getSHNumber(),
                header.getSHStringIndex(), notifications);

        sections = sectionHeaderTable.getSections();

        programHeaderTable = new ProgramHeaderTable(data, header.getPHOffset(), header.getPHEntrySize(), header.getPHNumber(),
                notifications);

        int maxAddr = 0;
        @SuppressWarnings("unused")
        int maxAddrSize = 0;
        int addr;
        int minAddr = Integer.MAX_VALUE;
        for(ProgramHeader header : programHeaderTable.getHeaders()) {
            addr = header.getVirtualAddress();
            if(header.getSizeInMemory() > 0) {
                if((addr + header.getSizeInMemory()) > maxAddr) {
                    maxAddr = addr + header.getSizeInMemory();
                }
                if(addr < minAddr) {
                    minAddr = addr;
                }
            }

        }
        baseAddr = minAddr;

        image = new byte[maxAddr - minAddr];

        for(ProgramHeader header : programHeaderTable.getHeaders()) {
            // Address of 0 indicates it is not in the memory image
            if(header.getSizeInMemory() > 0 && header.getType() != ELF.SHT_NOBITS) {
                System.arraycopy(data, header.getOffsetInFile(), image, header.getVirtualAddress() - minAddr, header.getSizeInFile());
            }
        }
        sectionHeaderTable.doRelocations(image);
    }

    public Header getHeader() {
        return header;
    }

    public SectionHeaderTable getSectionHeaderTable() {
        return sectionHeaderTable;
    }

    public ProgramHeaderTable getProgramHeaderTable() {
        return programHeaderTable;
    }

    public List<Section> getSections() {
        return sections;
    }

    public int getArch() {
        return header.getMachine();
    }

    public boolean isLittleEndian() {
        return header.getEIClass() == ELF.ELFDATA2LSB;
    }

    public long getEntryPoint() {
        return header.getEntryPoint();
    }

    public byte[] getImage() {
        return image;
    }

    public long getImageSize() {
        return image.length;
    }

    public long getImageBase() {
        // Depends on page size
        return baseAddr;
    }

    public int getWordSize() {
        switch(header.getData()) {
        case ELF.ELFCLASS32:
            return 32;
        case ELF.ELFCLASS64:
            return 64;
        default:
            // Means we don't know
            // Need to ask the user
            return -1;
        }
    }

    public int getFlags() {
        return header.getFlags();
    }

    public int getType() {
        return header.getType();

    }
}
