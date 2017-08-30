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
import com.pnf.plugin.macho.internal.SectionHeaderTable;
import com.pnfsoftware.jeb.core.events.JebEventSource;
import com.pnfsoftware.jeb.core.output.table.ICellCoordinates;
import com.pnfsoftware.jeb.core.output.table.ITableDocument;
import com.pnfsoftware.jeb.core.output.table.ITableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.Cell;
import com.pnfsoftware.jeb.core.output.table.impl.TableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.TableRow;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class SectionHeaderTableDocument extends JebEventSource implements ITableDocument {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(StringTableDocument.class);

    SectionHeaderTable headerTable;

    List<TableRow> rows;

    public SectionHeaderTableDocument(SectionHeaderTable headerTable) {
        this.headerTable = headerTable;

        rows = new ArrayList<>();
        List<SectionHeader> headers = headerTable.getHeaders();
        List<Cell> cells;
        SectionHeader header;
        for(int index = 0; index < headers.size(); index++) {
            cells = new ArrayList<>();
            header = headers.get(index);
            cells.add(new Cell("" + index));
            cells.add(new Cell("" + header.getName()));
            cells.add(new Cell("" + header.getType_s()));
            cells.add(new Cell(String.format("%h", header.getAddress())));
            cells.add(new Cell(String.format("%h", header.getOffset())));
            cells.add(new Cell(String.format("%h", header.getSize())));
            cells.add(new Cell("" + header.getFlagsString()));
            cells.add(new Cell("" + header.getLink()));
            cells.add(new Cell("" + header.getInfo()));
            cells.add(new Cell("" + header.getAddressAlign()));
            rows.add(new TableRow(cells));
        }
    }

    @Override
    public List<String> getColumnLabels() {
        ArrayList<String> output = new ArrayList<>();
        output.add("Index");
        output.add("Name");
        output.add("Type");
        output.add("Address");
        output.add("Offset");
        output.add("Size");
        output.add("Flags");
        output.add("Link");
        output.add("Info");
        output.add("Addr Align");
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
