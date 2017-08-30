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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pnf.plugin.macho.internal.MachO.*;

import com.pnf.plugin.macho.internal.MachOFile;
import com.pnf.plugin.macho.internal.Header;
import com.pnf.plugin.macho.internal.ProgramHeader;
import com.pnf.plugin.macho.internal.SectionHeader;
import com.pnf.plugin.macho.internal.SymbolTableEntry;
import com.pnf.plugin.macho.internal.SymbolTableSection;
import com.pnfsoftware.jeb.core.IUnitCreator;
import com.pnfsoftware.jeb.core.actions.ActionContext;
import com.pnfsoftware.jeb.core.actions.IActionData;
import com.pnfsoftware.jeb.core.input.BytesInput;
import com.pnfsoftware.jeb.core.input.FileInputLocation;
import com.pnfsoftware.jeb.core.input.IInput;
import com.pnfsoftware.jeb.core.input.IInputLocation;
import com.pnfsoftware.jeb.core.output.AbstractUnitRepresentation;
import com.pnfsoftware.jeb.core.output.IGenericDocument;
import com.pnfsoftware.jeb.core.output.IUnitFormatter;
import com.pnfsoftware.jeb.core.output.UnitFormatterAdapter;
import com.pnfsoftware.jeb.core.properties.IPropertyDefinitionManager;
import com.pnfsoftware.jeb.core.units.AbstractBinaryUnit;
import com.pnfsoftware.jeb.core.units.IInteractiveUnit;
import com.pnfsoftware.jeb.core.units.IMetadataManager;
import com.pnfsoftware.jeb.core.units.IUnit;
import com.pnfsoftware.jeb.core.units.IUnitNotification;
import com.pnfsoftware.jeb.core.units.IUnitProcessor;
import com.pnfsoftware.jeb.core.units.code.asm.memory.IVirtualMemory;
import com.pnfsoftware.jeb.core.units.codeobject.ICodeObjectUnit;
import com.pnfsoftware.jeb.core.units.codeobject.ILoaderInformation;
import com.pnfsoftware.jeb.core.units.codeobject.ISegmentInformation;
import com.pnfsoftware.jeb.core.units.codeobject.ISymbolInformation;
import com.pnfsoftware.jeb.core.units.codeobject.SymbolInformation;
import com.pnfsoftware.jeb.core.units.codeobject.SymbolType;
import com.pnfsoftware.jeb.util.io.*;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class MachOUnit extends AbstractBinaryUnit implements ICodeObjectUnit, IInteractiveUnit {

    private static final ILogger logger = GlobalLog.getLogger(MachOUnit.class);
    private MachOFile elf;
    private List<ISymbolInformation> symbols;
    private List<MachOSectionInfo> sections;
    private List<MachOSectionInfo> segments;
    @SuppressWarnings("unused")
    private MachOLoaderInformation loaderInfo;
    private byte[] data;

    public MachOUnit(String name, IInput input, IUnitProcessor unitProcessor, IUnitCreator parent, IPropertyDefinitionManager pdm) {
        super(null, input, MachOPlugin.TYPE, name, unitProcessor, parent, pdm);
        try (InputStream stream = input.getStream()) {
            data = IO.readInputStream(stream);
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    @Override
    public boolean process() {
        if (isProcessed()) {
            return true;
        }
        
        setStatus("Unprocessed");
        elf = new MachOFile(data);
        symbols = new ArrayList<>();
        sections = new ArrayList<>();
        segments = new ArrayList<>();
        SymbolTableSection symtab = null;
        SymbolType symType;
        for(SectionHeader header : elf.getSectionHeaderTable().getHeaders()) {
            if(header.getType() == SHT_DYNSYM || header.getType() == SHT_SYMTAB) {
                symtab = (SymbolTableSection)(header.getSection());
                for(SymbolTableEntry entry : symtab.getEntries()) {
                    symType = null;
                    switch(entry.getType()) {
                    case STT_FUNC:
                        symType = SymbolType.FUNCTION;
                        break;
                    case STT_SECTION:
                        symType = SymbolType.SECTION;
                        break;
                    case STT_FILE:
                        symType = SymbolType.FILE;
                        break;
                    case STT_OBJECT:
                        symType = SymbolType.OBJECT;
                        break;
                    default:
                        break;
                    }
                    symbols.add(new SymbolInformation(symType, 0, (long)entry.getValue(), entry.getName(), (long)entry.getValue(),
                            (long)entry.getValue(), entry.getSize()));
                }
            }
            // sections.add(new MachOSectionInfo(header));
            if((header.getFlags() & SHF_ALLOC) != 0) {
                sections.add(new MachOSectionInfo(header));
            }
        }
        for(ProgramHeader header : elf.getProgramHeaderTable().getHeaders()) {
            if((header.getType() & PT_LOAD) != 0 && header.getSizeInMemory() > 0) {
                segments.add(new MachOSectionInfo(header));
            }
        }
        addNotification((IUnitNotification) elf.getNotifications());

        byte[] processImage;
        long minAddr = Long.MAX_VALUE;
        long maxAddr = Long.MIN_VALUE;
        for(ISegmentInformation segment : segments) {
            if(segment.getOffsetInMemory() < minAddr) {
                minAddr = segment.getOffsetInMemory();
            }
            if(segment.getOffsetInMemory() + segment.getSizeInMemory() > maxAddr) {
                maxAddr = segment.getOffsetInMemory() + segment.getSizeInMemory();
            }
        }
        if(maxAddr > Integer.MAX_VALUE || minAddr > Integer.MAX_VALUE) {
            throw new RuntimeException(String.format("Can't pass IInput larger than Integer.MAX_VALUE", maxAddr));
        }
        processImage = new byte[(int)(maxAddr - minAddr)];
        for(ISegmentInformation segment : segments) {
            if(segment.getSizeInFile() > 0) {
                System.arraycopy(data, (int)segment.getOffsetInFile(), processImage, (int)(segment.getOffsetInMemory() - minAddr),
                        (int)segment.getSizeInFile());
            }
        }
        loaderInfo = new MachOLoaderInformation(elf);
        IUnit target = null;
        String targetType;
        switch(elf.getArch()) {
        case EM_MIPS:
            targetType = "MIPS";
            break;
        default:
            targetType = null;
        }
        if(targetType != null) {
            target = getUnitProcessor().process(getName(), new BytesInput(processImage), this, targetType, true);
            if(target != null) {
                addChild(target);
            }
        }

        // Perform the soft delegation of each section
        for(SectionHeader header : elf.getSectionHeaderTable().getHeaders()) {
            if(header.getSection() != null) {
                try {
                    target = getUnitProcessor().process(header.getName(), new BytesInput(header.getSection().getBytes()), this, null, true);
                    if(target != null) {
                        addChild(target);
                    }
                }
                catch(Exception e) {
                    logger.info("%s", e.getMessage());
                    setStatus("Error:" + e.getMessage());
                }
            }
        }
        setStatus("Processed");
        setProcessed(true);
        return true;
    }

    public MachOFile getElf() {
        return elf;
    }

    @Override
    public List<ISymbolInformation> getExportedSymbols() {
        return symbols;
    }

    @Override
    public List<ISymbolInformation> getImportedSymbols() {
        return null;
    }

    @Override
    public ILoaderInformation getLoaderInformation() {
        return new MachOLoaderInformation(elf);
    }

    @Override
    public List<MachOSectionInfo> getSections() {
        return sections;
    }

    @Override
    public List<MachOSectionInfo> getSegments() {
        return segments;
    }

    @Override
    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        Header header = elf.getHeader();

        desc.append("Mach-O Header:\n");
        desc.append(String.format("%-40s%-20s\n", "Magic:", header.getMagicString()));
        desc.append(String.format("%-40s%-20s\n", "Class:", header.getClassString()));
        desc.append(String.format("%-40s%-20s\n", "Data:", header.getDataString()));
        desc.append(String.format("%-40s%-20s\n", "Version:", header.getVersionString()));
        desc.append(String.format("%-40s%-20s\n", "OS/ABI:", header.getOSABIString()));
        desc.append(String.format("%-40s%-20s\n", "ABI version:", "" + header.getABIVersion()));
        desc.append(String.format("%-40s%-20s\n", "Type:", header.getTypeString()));
        desc.append(String.format("%-40s%-20s\n", "Machine:", header.getMachineString()));
        desc.append(String.format("%-40s%-20s\n", "Version:", "0x" + Integer.toHexString(header.getVersion())));
        desc.append(String.format("%-40s%-20s\n", "Entry point address:", "0x" + Integer.toHexString(header.getEntryPoint())));
        desc.append(String.format("%-40s%-20s\n", "Start of program headers:", "0x" + Integer.toHexString(header.getPHOffset())));
        desc.append(String.format("%-40s%-20s\n", "Start of section headers:", "0x" + Integer.toHexString(header.getShoff())));
        desc.append(String.format("%-40s%-20s\n", "Flags:", "0x" + Integer.toHexString(header.getFlags())));
        desc.append(String.format("%-40s%-20s\n", "Size of this header:", header.getHeaderSize()));
        desc.append(String.format("%-40s%-20s\n", "Size of program headers:", header.getPHEntrySize()));
        desc.append(String.format("%-40s%-20s\n", "Number of program headers:", header.getPHNumber()));
        desc.append(String.format("%-40s%-20s\n", "Size of section headers:", header.getSHEntrySize()));
        desc.append(String.format("%-40s%-20s\n", "Number of section headers:", header.getSHNumber()));
        desc.append(String.format("%-40s%-20s\n", "Section header string table index:", header.getSHStringIndex()));
        return desc.toString();
    }

    @Override
    public IUnitFormatter getFormatter() {
        List<SectionHeader> sectionHeaders = elf.getSectionHeaderTable().getHeaders();
        UnitFormatterAdapter formatter = new UnitFormatterAdapter();
        formatter.addDocumentPresentation(new AbstractUnitRepresentation("Section Header Table", true) {
            @Override
            public IGenericDocument getDocument() {
                return new SectionHeaderTableDocument(elf.getSectionHeaderTable());
            }
        });

        formatter.addDocumentPresentation(new AbstractUnitRepresentation("Program Header Table", false) {
            @Override
            public IGenericDocument getDocument() {
                return new ProgramHeaderTableDocument(elf.getProgramHeaderTable());
            }
        });
        for (SectionHeader section : sectionHeaders) {
            final SectionHeader section0 = section;
            switch (section.getType()) {
                case SHT_STRTAB:
                    formatter.addDocumentPresentation(new AbstractUnitRepresentation(section.getName(), false) {
                        @Override
                        public IGenericDocument getDocument() {
                            return new StringTableDocument(section0);
                        }
                    });
                    break;
                /*
             * Can't see this info being useful case SHT_HASH:
             * formatter.addDocumentPresentation(new
             * AbstractUnitRepresentation(section.getName(), false) {
             * 
             * @Override public IGenericDocument getDocument() { return new
             * HashTableDocument(section0); } }); break;
                 */
                case SHT_NOTE:
                    formatter.addDocumentPresentation(new AbstractUnitRepresentation(section.getName(), false) {
                        @Override
                        public IGenericDocument getDocument() {
                            return new NotesDocument(section0);
                        }
                    });
                    break;

                case SHT_DYNSYM:
                case SHT_SYMTAB:
                    formatter.addDocumentPresentation(new AbstractUnitRepresentation(section.getName(), false) {
                        @Override
                        public IGenericDocument getDocument() {
                            return new SymbolTableDocument(section0);
                        }
                    });
                    break;
                case SHT_DYNAMIC:
                    formatter.addDocumentPresentation(new AbstractUnitRepresentation(section.getName(), false) {
                        @Override
                        public IGenericDocument getDocument() {
                            return new DynamicSectionDocument(section0);
                        }
                    });
                    break;
                /*
             * Not useful to display yet case SHT_RELA: case SHT_REL:
             * formatter.addDocumentPresentation(new
             * AbstractUnitRepresentation(section.getName(), false) {
             * 
             * @Override public IGenericDocument getDocument() { return new
             * RelocationSectionDocument(section0); } }); break;
                 */
                default:
                    break;
            }
        }

        return formatter;
    }

    // No actions available at the moment
    @Override
    public boolean executeAction(ActionContext context, IActionData data) {
        return false;
    }

    @Override
    public boolean prepareExecution(ActionContext context, IActionData data) {
        return false;
    }

    @Override
    public boolean canExecuteAction(ActionContext context) {
        return false;
    }

    @Override
    public List<Integer> getItemActions(long id) {
        return new ArrayList<>();
    }

    @Override
    public long getItemAtAddress(String address) {
        return 1L;
    }

    @Override
    public String getAddressOfItem(long id) {
        return null;
    }

    @Override
    public List<Integer> getGlobalActions() {
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getAddressActions(String address) {
        return new ArrayList<>();
    }

    @Override
    public long convertFileOffsetToRelativeAddress(long offset) {
        return 0;
    }

    @Override
    public long convertRelativeAddressToFileOffset(long offset) {
        return 0;
    }

    @Override
    public String getComment(String address) {
        return null;
    }

    @Override
    public Map<String, String> getComments() {
        return null;
    }

    @Override
    public String getAddressLabel(String address) {
        return null;
    }

    @Override
    public Map<String, String> getAddressLabels() {
        return null;
    }

    @Override
    public String locationToAddress(IInputLocation location) {
        if (!(location instanceof FileInputLocation)) {
            throw new IllegalArgumentException("Unrecognized IInputLocationInformation implementor");
        }
        return "" + ((FileInputLocation) location).getOffset();
    }

    @Override
    public IInputLocation addressToLocation(String address) {
        long offset = Long.parseLong(address);
        // long size = -1;
        for (ISegmentInformation section : sections) {
            if (offset == section.getOffsetInFile()) {
                /* size = */
                section.getSizeInFile();
            }
        }
        return new FileInputLocation(offset);
    }

    @Override
    public ISegmentInformation getSegment(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSegmentCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ISegmentInformation getSection(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSectionCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<? extends ISymbolInformation> getSymbols() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IVirtualMemory getRawMemoryMappedImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean map(IVirtualMemory ivm, long l, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getItemObject(long l) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidAddress(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean executeAction(ActionContext ac, IActionData iad, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IMetadataManager getMetadataManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
