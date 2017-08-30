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

import com.pnf.plugin.macho.ELFUnit;
import com.pnf.plugin.macho.internal.ELF;
import com.pnfsoftware.jeb.core.IUnitCreator;
import com.pnfsoftware.jeb.core.PluginInformation;
import com.pnfsoftware.jeb.core.Version;
import com.pnfsoftware.jeb.core.input.IInput;
import com.pnfsoftware.jeb.core.properties.IPropertyDefinitionManager;
import com.pnfsoftware.jeb.core.units.AbstractUnitIdentifier;
import com.pnfsoftware.jeb.core.units.IUnit;
import com.pnfsoftware.jeb.core.units.IUnitProcessor;
import com.pnfsoftware.jeb.core.units.WellKnownUnitTypes;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;

public class ELFPlugin extends AbstractUnitIdentifier {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(ELFPlugin.class);

    public static final String TYPE = WellKnownUnitTypes.typeLinuxElf;

    public ELFPlugin() {
        super(TYPE, 0);
    }

    @Override
    public PluginInformation getPluginInformation() {
        return new PluginInformation("ELF File Unit", "Linux ELF parser plugin", "PNF Software", Version.create(1, 0, 1));
    }

    @Override
    public void initialize(IPropertyDefinitionManager parent) {
        super.initialize(parent);
    }

    @Override
    public boolean canIdentify(IInput input, IUnitCreator parent) {
        return checkBytes(input, 0, (int)ELF.ElfMagic[0], (int)ELF.ElfMagic[1], (int)ELF.ElfMagic[2], (int)ELF.ElfMagic[3]) &&
                // Ensure a 32 bit elf file
                checkBytes(input, 4, (byte)0x1);
    }

    @Override
    public IUnit prepare(String name, IInput input, IUnitProcessor unitProcessor, IUnitCreator parent) {
        ELFUnit unit = new ELFUnit(name, input, unitProcessor, parent, pdm);
        unit.process();
        return unit;
    }
}
