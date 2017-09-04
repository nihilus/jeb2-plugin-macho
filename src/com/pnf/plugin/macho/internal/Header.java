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
    private byte moMag0;
    private byte moMag1;
    private byte moMag2;
    private byte moMag3;
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
        moMag0 = (byte)stream.read();
        moMag1 = (byte)stream.read();
        moMag2 = (byte)stream.read();
        moMag3 = (byte)stream.read();
        byte[] Magic = new byte[] { moMag0, moMag1, moMag2, moMag3 };
        
        ByteBuffer magicbf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(MachO.MH_MAGIC);
        ByteBuffer magic64bf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(MachO.MH_MAGIC64);
        magicbf.rewind();
        magic64bf.rewind();
        
        if(checkBytes(Magic, 0, magicbf.order(ByteOrder.LITTLE_ENDIAN).array()) || checkBytes(Magic, 0, magic64bf.order(ByteOrder.LITTLE_ENDIAN).array())){
            endianness = ByteOrder.LITTLE_ENDIAN;
        } else if(checkBytes(Magic, 0, magicbf.order(ByteOrder.BIG_ENDIAN).array()) || checkBytes(Magic, 0, magic64bf.order(ByteOrder.BIG_ENDIAN).array())){
            endianness = ByteOrder.BIG_ENDIAN;
        } else 
            throw new IllegalArgumentException("Magic number does not match");
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
        return ByteBuffer.allocate(4).put(moMag0).put(moMag1).put(moMag2).put(moMag3).getInt(0);
    }

    public String getMagicString() {
        StringBuilder magic = new StringBuilder();
        magic.append(String.format("%x ", moMag0));
        magic.append(String.format("%x ", moMag1));
        magic.append(String.format("%x ", moMag2));
        magic.append(String.format("%x", moMag3));
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
