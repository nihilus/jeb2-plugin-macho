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

import com.pnf.plugin.macho.internal.ELF;
import com.pnf.plugin.macho.internal.ELFFile;
import com.pnfsoftware.jeb.core.units.codeobject.ILoaderInformation;
import com.pnfsoftware.jeb.core.units.codeobject.ProcessorType;
import com.pnfsoftware.jeb.core.units.codeobject.SubsystemType;
import com.pnfsoftware.jeb.util.io.Endianness;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class ELFLoaderInformation implements ILoaderInformation {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(ELFLoaderInformation.class);

    public static int HAS_RELOCATIONS;
    public static int HAS_SYMBOLS;

    public long compTimestamp;
    public long entryPoint;
    public int flags;
    public long imageBase;
    public long imageSize;
    public ProcessorType targetProcessor;
    public SubsystemType targetSubsystem;
    public int wordSize;
    public boolean isLibrary;
    public boolean isLittleEndian;

    public ELFLoaderInformation(ELFFile elf) {

        wordSize = elf.getWordSize();
        entryPoint = elf.getEntryPoint();
        flags = elf.getFlags();
        imageBase = elf.getImageBase();
        imageSize = elf.getImageSize();
        isLibrary = (elf.getType() == ELF.ET_DYN);
        isLittleEndian = elf.isLittleEndian();
        switch(elf.getArch()) {
        case ELF.EM_ARM:
            if(wordSize == 32)
                targetProcessor = ProcessorType.ARM;
            else if(wordSize == 64)
                targetProcessor = ProcessorType.ARM64;
            break;
        case ELF.EM_MIPS:
            if(wordSize == 32)
                targetProcessor = ProcessorType.MIPS;
            else if(wordSize == 64)
                targetProcessor = ProcessorType.MIPS64;
            break;
        case ELF.EM_X86_64:
            targetProcessor = ProcessorType.X86_64;
            break;
        case ELF.EM_386:
            targetProcessor = ProcessorType.X86;
            break;
        default:
            targetProcessor = null;
        }

        // Always
        targetSubsystem = SubsystemType.LINUX;

        // Unused
        compTimestamp = 0;
    }

    @Override
    public long getCompilationTimestamp() {
        return compTimestamp;
    }

    @Override
    public long getEntryPoint() {
        return entryPoint;
    }

    @Override
    public int getFlags() {
        return flags;
    }

    @Override
    public long getImageBase() {
        return imageBase;
    }

    @Override
    public long getImageSize() {
        return imageSize;
    }

    @Override
    public ProcessorType getTargetProcessor() {
        return targetProcessor;
    }

    @Override
    public SubsystemType getTargetSubsystem() {
        return targetSubsystem;
    }

    @Override
    public int getWordSize() {
        return wordSize;
    }

    public boolean isLibraryFile() {
        return true;
    }

    public boolean isLittleEndian() {
        return true;
    }

    @Override
    public String getVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Endianness getEndianness() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getOverlayOffset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
