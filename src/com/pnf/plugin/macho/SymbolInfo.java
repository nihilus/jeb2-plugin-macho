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

import com.pnfsoftware.jeb.core.units.codeobject.ISymbolInformation;
import com.pnfsoftware.jeb.core.units.codeobject.SymbolType;

import static com.pnf.plugin.macho.internal.MachO.*;

public class SymbolInfo implements ISymbolInformation {

    private String name;
    private long address;
    private long identifier;
    private SymbolType type;
    @SuppressWarnings("unused")
    private long size;

    public SymbolInfo(String name, long address, int type, int size) {
        this.name = name;
        this.address = address;
        this.identifier = address;
        this.size = (int)size;

        switch(type) {
        case STT_FUNC:
            this.type = SymbolType.FUNCTION;
            break;
        case STT_SECTION:
            this.type = SymbolType.SECTION;
            break;
        case STT_FILE:
            this.type = SymbolType.FILE;
            break;
        case STT_OBJECT:
            this.type = SymbolType.OBJECT;
            break;
        default:
            break;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public long getAddress() {
        return address;
    }

    @Override
    public long getIdentifier() {
        return identifier;
    }

    public long getOffset() {
        return -1;
    }

    @Override
    public int getFlags() {
        return -1;
    }

    @Override
    public SymbolType getType() {
        return type;
    }

    public long getSize() {
        return -1;
    }

    @Override
    public long getRelativeAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getSymbolRelativeAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getSymbolSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}