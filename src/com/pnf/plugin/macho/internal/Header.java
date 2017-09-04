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
    private final byte moMag0;
    private final byte moMag1;
    private final byte moMag2;
    private final byte moMag3;
    private String eiClassString;
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

    private final int wordsize;
    private final ByteOrder endian;
    
    public Header(byte[] data) {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);

        moMag0 = (byte)stream.read();
        moMag1 = (byte)stream.read();
        moMag2 = (byte)stream.read();
        moMag3 = (byte)stream.read();
        byte[] Magic = new byte[] { moMag0, moMag1, moMag2, moMag3 };
        
        ByteBuffer magicbf = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(MachO.MH_MAGIC);
        ByteBuffer magic64bf = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(MachO.MH_MAGIC64);
        
        if(checkBytes(Magic, 0, magicbf.order(ByteOrder.LITTLE_ENDIAN).array()) || checkBytes(Magic, 0, magic64bf.order(ByteOrder.LITTLE_ENDIAN).array())){
            endian = endianness = ByteOrder.LITTLE_ENDIAN;
        } else if(checkBytes(Magic, 0, magicbf.order(ByteOrder.BIG_ENDIAN).array()) || checkBytes(Magic, 0, magic64bf.order(ByteOrder.BIG_ENDIAN).array())){
            endian = endianness = ByteOrder.BIG_ENDIAN;
        } else 
            throw new IllegalArgumentException("Magic number does not match");
    
        if(checkBytes(Magic, 0, magicbf.order(ByteOrder.LITTLE_ENDIAN).array()) || checkBytes(Magic, 0, magicbf.order(ByteOrder.BIG_ENDIAN).array())){
             wordsize = 32;           
        } else {
            wordsize = 64;
        }    
    }
   
    public ByteOrder getEndian() {
        return endian;
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

    public String getClassString() {
        return eiClassString;
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

    public int getWordsize() {
        switch(wordsize){
            case 32:
            case 64:
                return wordsize;
            default:
                // Means that we don't know
                // Need to ask the user
                return -1;
        }
    }

}
