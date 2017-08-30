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

public class SymbolTableEntry extends StreamReader {
    protected ByteArrayInputStream stream;

    protected int name;
    protected String nameString;
    protected int value;
    protected int size;
    protected int info;
    protected int other;
    protected short sectionHeaderIndex;
    protected int bind;
    protected String bindString;
    protected int type;
    protected String typeString;

    public SymbolTableEntry(byte[] data, int offset) {
        stream = new ByteArrayInputStream(data);
        stream.skip(offset);

        name = readInt(stream);

        value = readInt(stream);
        size = readInt(stream);
        info = stream.read();
        other = stream.read();
        sectionHeaderIndex = readShort(stream);
        bind = info >> 4;
        bindString = ELF.getSTBString(bind);

        type = info & 0xf;
        typeString = ELF.getSTTString(type);
    }

    public void setName(StringTableSection nameTable) {
        this.nameString = nameTable.getString(name);
    }

    public String getName() {
        return nameString;
    }

    public int getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }

    public int getInfo() {
        return info;
    }

    public int getOther() {
        return other;
    }

    public short getSectionHeaderIndex() {
        return sectionHeaderIndex;
    }

    public String getBindString() {
        return bindString;
    }

    public int getBind() {
        return bind;
    }

    public int getType() {
        return type;
    }

    public String getTypeString() {
        return typeString;
    }
}
