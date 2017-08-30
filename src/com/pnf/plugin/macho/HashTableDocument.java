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

import com.pnf.plugin.macho.internal.HashTableSection;
import com.pnf.plugin.macho.internal.SectionHeader;
import com.pnfsoftware.jeb.core.events.JebEventSource;
import com.pnfsoftware.jeb.core.output.table.ITableDocument;
import com.pnfsoftware.jeb.core.output.table.ITableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.Cell;
import com.pnfsoftware.jeb.core.output.table.impl.TableDocumentPart;
import com.pnfsoftware.jeb.core.output.table.impl.TableRow;
import com.pnfsoftware.jeb.core.output.table.ICellCoordinates;

public class HashTableDocument extends JebEventSource implements ITableDocument {

    SectionHeader header;
    HashTableSection section;

    List<TableRow> rows;

    public HashTableDocument(SectionHeader header) {
        this.header = header;

        rows = new ArrayList<>();

        section = (HashTableSection)(header.getSection());

        rows.add(new TableRow(new Cell("Buckets")));
        for(int index = 0; index < section.getNBuckets(); index++) {
            rows.add(new TableRow(new Cell("" + section.getBucket(index))));
        }
        rows.add(new TableRow(new Cell("Chains")));
        for(int index = 0; index < section.getNChains(); index++) {
            rows.add(new TableRow(new Cell("" + section.getChain(index))));
        }
    }

    @Override
    public List<String> getColumnLabels() {
        ArrayList<String> output = new ArrayList<>();
        output.add("Value");
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
