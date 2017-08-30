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

import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class StreamReader {

    protected static final ILogger logger = GlobalLog.getLogger(StreamReader.class);

    // Use a probable default endianness
    protected static ByteOrder endianness = ByteOrder.LITTLE_ENDIAN;

    protected static int readInt(ByteArrayInputStream stream, int offset) {
        stream.mark(0);
        stream.skip(offset);
        int output = readInt(stream);
        stream.reset();
        return output;
    }

    protected static int readShort(ByteArrayInputStream stream, int offset) {
        stream.mark(0);
        stream.skip(offset);
        short output = readShort(stream);
        stream.reset();
        return output;
    }

    protected static int readInt(ByteArrayInputStream stream) {
        byte[] temp = new byte[4];
        stream.read(temp, 0, 4);
        return ByteBuffer.wrap(temp).order(endianness).getInt();
    }

    protected static short readShort(ByteArrayInputStream stream) {
        byte[] temp = new byte[2];
        stream.read(temp, 0, 2);
        return ByteBuffer.wrap(temp).order(endianness).getShort();
    }

    protected static String readString(ByteArrayInputStream stream) {
        String output = "";
        char character;
        while(stream.available() > 0) {
            character = (char)stream.read();

            if(character == 0)
                break;
            output = output + character;

        }
        return output;
    }

    protected static String getStringFromTable(ByteArrayInputStream stream, int offset) {
        String output = "";
        stream.mark(0);
        stream.skip(offset);
        char character;
        while(stream.available() > 0) {
            character = (char)stream.read();

            if(character == 0)
                break;
            output = output + character;

        }
        stream.reset();
        return output;
    }

    protected static boolean checkBytes(byte[] data, int offset, byte... checkBytes) {
        for(int index = 0; index < checkBytes.length; index++) {
            if(data[offset + index] != checkBytes[index])
                return false;
        }
        return true;
    }
}
