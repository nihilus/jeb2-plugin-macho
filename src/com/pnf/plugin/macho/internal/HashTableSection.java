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

public class HashTableSection extends Section {
    private int numBuckets;
    private int numChain;
    private int[] buckets;
    private int[] chains;

    public HashTableSection(byte[] data, int s_size, int s_offset) {
        super(data, s_size, s_offset);

        numBuckets = readInt(stream);
        numChain = readInt(stream);

        buckets = new int[numBuckets];
        for(int index = 0; index < numBuckets; index++)
            buckets[index] = readInt(stream);
        chains = new int[numChain];
        for(int index = 0; index < numChain; index++)
            chains[index] = readInt(stream);
    }

    public int getBucket(int index) {
        return buckets[index];
    }

    public int getChain(int index) {
        return chains[index];
    }

    public int getNBuckets() {
        return numBuckets;
    }

    public int getNChains() {
        return numChain;
    }
}
