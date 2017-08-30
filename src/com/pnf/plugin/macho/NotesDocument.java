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

import com.pnf.plugin.macho.internal.NoteSection;
import com.pnf.plugin.macho.internal.NoteSectionEntry;
import com.pnf.plugin.macho.internal.SectionHeader;
import com.pnfsoftware.jeb.core.events.JebEventSource;
import com.pnfsoftware.jeb.core.output.text.IAnchor;
import com.pnfsoftware.jeb.core.output.text.ICoordinates;
import com.pnfsoftware.jeb.core.output.text.ILine;
import com.pnfsoftware.jeb.core.output.text.ITextDocument;
import com.pnfsoftware.jeb.core.output.text.ITextDocumentPart;
import com.pnfsoftware.jeb.core.output.text.impl.Anchor;
import com.pnfsoftware.jeb.core.output.text.impl.Line;
import com.pnfsoftware.jeb.core.output.text.impl.TextDocumentPart;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class NotesDocument extends JebEventSource implements ITextDocument {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(NotesDocument.class);

    private List<ILine> lines;
    private List<IAnchor> anchors;

    public NotesDocument(SectionHeader section) {
        lines = new ArrayList<>();
        anchors = new ArrayList<>();

        anchors.add(new Anchor(0, 0));
        for(NoteSectionEntry entry : ((NoteSection)(section.getSection())).getEntries()) {
            for(String line1 : entry.toString().split("\n")) {
                for(String line2 : line1.split("\r")) {
                    lines.add(new Line(line2));
                }
            }
        }
    }

    @Override
    public ICoordinates addressToCoordinates(String address) {
        return null;
    }

    @Override
    public String coordinatesToAddress(ICoordinates coordinates) {
        return null;
    }

    @Override
    public long getAnchorCount() {
        return anchors.size();
    }

    @Override
    public ITextDocumentPart getDocumentPart(long anchorId, int linesAfter) {
        return getDocumentPart(anchorId, linesAfter, 0);
    }

    @Override
    public ITextDocumentPart getDocumentPart(long anchorId, int linesAfter, int linesBefore) {
        return new TextDocumentPart(lines, anchors);
    }

    @Override
    public void dispose() {
    }

    @Override
    public long getInitialAnchor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getFirstAnchor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
