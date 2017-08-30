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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public class Header extends StreamReader {

    private int start;
    private int end;
    private int identSize; // size of identification struct
    private byte eiMag0;
    private byte eiMag1;
    private byte eiMag2;
    private byte eiMag3;
    private byte eiClass;
    private String eiClassString;
    private byte eiData;
    private String eiDataString;
    private byte eiVersion;
    private String eiVersionString;
    private byte eiOsabi;
    private String eiOsabiString;
    private byte eiAbiversion;
    private String eiAbiversionString;

    private short eType;
    private String eTypeString;
    private short eMachine;
    private String eMachineString;
    private int eVersion;
    private String eVersionString;
    private int eEntry;
    private int ePhoff;
    private int eShoff;
    private int eFlags;
    private short eEhsize;
    private short ePhentSize;
    private short ePhnum;
    private short eShentSize;
    private short eShnum;
    private short eShstrndx;

    private int nameSectionHeaderStart;
    private int nameSectionStart;

    // ELF Header
    // Bytes: name
    // 0-3 : ei_mag0 - ei_mag3
    // 4 : ei_class
    // 5 : ei_data
    // 6 : ei_version
    // 7 : ei_osabi
    // 8-15 : ei_pad
    // 16-17: e_type
    // 18-19: e_machine
    // 20-23: e_version
    // 24-27: e_entry
    // 28-31: e_phoff
    // 32-35: e_shoff
    // 36-39: e_flags
    // 40-41: e_ehsize (size of ELF header in bytes)
    // 42-43: e_phentsize (size of program file header entry) *
    // 44-45: e_phnum (number of program header entries) *
    // 46-47: e_shentsize (size of section header table entry) *
    // 48-49: e_shnum (number of section header entries) *
    // 50-51: e_shstrndx (location of string table for section header table)
    public Header(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        /******* Read Ident Struct ******/
        eiMag0 = (byte)stream.read();
        eiMag1 = (byte)stream.read();
        eiMag2 = (byte)stream.read();
        eiMag3 = (byte)stream.read();

        if(!checkBytes(new byte[] { eiMag0, eiMag1, eiMag2, eiMag3 }, 0, MachO.ElfMagic))
            throw new IllegalArgumentException("Magic number does not match");

        eiClass = (byte)stream.read();
        if(eiClass == 0)
            throw new AssertionError("Invalid class");
        eiClassString = MachO.getELFClassString(eiClass);

        eiData = (byte)stream.read();
        switch(eiData) {
        case MachO.ELFDATANONE:
            throw new AssertionError("Invalid data format");
        case MachO.ELFDATA2LSB:
            endianness = ByteOrder.LITTLE_ENDIAN;
            break;
        case MachO.ELFDATA2MSB:
            endianness = ByteOrder.BIG_ENDIAN;
            break;
        default:
            break;
        }
        eiDataString = MachO.getELFDataString(eiData);

        eiVersion = (byte)stream.read();
        eiVersionString = MachO.getEVString(0);

        eiOsabi = (byte)stream.read();
        eiAbiversion = (byte)stream.read();

        stream.skip(7);
        /******* Done Ident Struct ******/

        /******* Read Header ******/
        eType = readShort(stream);
        eTypeString = MachO.getETString(eType);
        eMachine = readShort(stream);
        eMachineString = MachO.getEMString(eMachine);
        eVersion = readInt(stream);
        eVersionString = MachO.getEVString(eVersion);
        eEntry = readInt(stream);
        ePhoff = readInt(stream);
        eShoff = readInt(stream);
        eFlags = readInt(stream);
        eEhsize = readShort(stream);
        ePhentSize = readShort(stream);
        ePhnum = readShort(stream);
        eShentSize = readShort(stream);
        eShnum = readShort(stream);
        eShstrndx = readShort(stream);
        /******* Done header ******/

    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getIdentSize() {
        return identSize;
    }

    public byte getEIClass() {
        return eiClass;
    }

    public String getClassString() {
        return eiClassString;
    }

    public byte getData() {
        return eiData;
    }

    public String getDataString() {
        return eiDataString;
    }

    public byte getEIVersion() {
        return eiVersion;
    }

    public String getEIVersionString() {
        return eiVersionString;
    }

    public short getType() {
        return eType;
    }

    public String getTypeString() {
        return eTypeString;
    }

    public short getMachine() {
        return eMachine;
    }

    public String getMachineString() {
        return eMachineString;
    }

    public int getEVersion() {
        return eVersion;
    }

    public String getEVersionString() {
        return eVersionString;
    }

    public int getEntryPoint() {
        return eEntry;
    }

    public int getPHOffset() {
        return ePhoff;
    }

    public int getShoff() {
        return eShoff;
    }

    public int getFlags() {
        return eFlags;
    }

    public short getHeaderSize() {
        return eEhsize;
    }

    public short getPHEntrySize() {
        return ePhentSize;
    }

    public short getPHNumber() {
        return ePhnum;
    }

    public short getSHEntrySize() {
        return eShentSize;
    }

    public short getSHNumber() {
        return eShnum;
    }

    public short getSHStringIndex() {
        return eShstrndx;
    }

    public int getMagic() {
        return ByteBuffer.allocate(4).put(eiMag0).put(eiMag1).put(eiMag2).put(eiMag3).getInt(0);
    }

    public String getMagicString() {
        StringBuilder magic = new StringBuilder();
        magic.append(String.format("%x ", eiMag0));
        magic.append(String.format("%x ", eiMag1));
        magic.append(String.format("%x ", eiMag2));
        magic.append(String.format("%x", eiMag3));
        return magic.toString();
    }

    public String getVersionString() {
        return MachO.getEVString(eiVersion);
    }

    public int getVersion() {
        return eVersion;
    }

    public String getOSABIString() {
        return MachO.getOSABIString(eiOsabi);
    }

    public int getABIVersion() {
        return eiAbiversion;
    }

    @Override
    public String toString() {
        return "Mach-O File " + eTypeString + " " + eMachineString + " " + eVersionString + "\n\t" + eShnum + " sections";
    }

}
