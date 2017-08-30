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

import java.util.ArrayList;
import java.util.List;

import com.pnf.plugin.macho.internal.SectionHeader;
import com.pnf.plugin.macho.internal.SymbolTableEntry;
import com.pnf.plugin.macho.internal.SymbolTableSection;
import com.pnfsoftware.jeb.core.events.JebEventSource;
import com.pnfsoftware.jeb.core.output.table.ICellCoordinates;
import com.pnfsoftware.jeb.core.output.table.ITableDocument;
import com.pnfsoftware.jeb.core.output.table.ITableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.Cell;
import com.pnfsoftware.jeb.core.output.table.impl.TableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.TableRow;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class SymbolTableDocument extends JebEventSource implements ITableDocument {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(SymbolTableEntry.class);

    SectionHeader header;
    SymbolTableSection section;
    List<TableRow> rows;

    public SymbolTableDocument(SectionHeader header) {
        this.header = header;

        rows = new ArrayList<>();

        section = (SymbolTableSection)(header.getSection());

        List<Cell> cells;
        List<SymbolTableEntry> entries = section.getEntries();
        SymbolTableEntry entry;
        for(int index = 0; index < entries.size(); index++) {
            cells = new ArrayList<>();
            entry = entries.get(index);
            cells.add(new Cell("" + entry.getName()));
            cells.add(new Cell(String.format("%h", entry.getValue())));
            cells.add(new Cell(String.format("%h", entry.getSize())));
            cells.add(new Cell("" + entry.getTypeString()));
            cells.add(new Cell("" + entry.getBindString()));
            cells.add(new Cell("" + entry.getSectionHeaderIndex()));
            rows.add(new TableRow(cells));
        }
    }

    @Override
    public List<String> getColumnLabels() {
        ArrayList<String> output = new ArrayList<>();
        output.add("Name");
        output.add("Value");
        output.add("Size");
        output.add("Type");
        output.add("Bind");
        output.add("Index");
        return output;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public ITableDocumentPart getTable() {
        return getTablePart(0, rows.size());
    }

    @Override
    public ITableDocumentPart getTablePart(int start, int count) {
        return new TableDocumentPart(start, rows.subList(start, start + count));
    }

    @Override
    public ICellCoordinates addressToCoordinates(String address) {
        return null;
    }

    @Override
    public String coordinatesToAddress(ICellCoordinates coordinates) {
        return null;
    }

    @Override
    public void dispose() {
    }

}
