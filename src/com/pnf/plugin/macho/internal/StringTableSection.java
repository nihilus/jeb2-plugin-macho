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

public class StringTableSection extends Section {
    private String text;

    public StringTableSection(byte[] data, int sh_size, int sh_offset) {
        super(data, sh_size, sh_offset);
        text = new String(getBytes());
    }

    public String getString(int index) {
        return getStringFromTable(stream, index);
    }

    public String getText() {
        return text;
    }

    public String[] getEntries() {
        // Split on regex that is every null character
        return getText().split("[" + (char)0 + "]");
    }
}
