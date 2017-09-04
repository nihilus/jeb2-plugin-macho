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

import com.pnf.plugin.macho.internal.MachO;
import com.pnfsoftware.jeb.core.IUnitCreator;
import com.pnfsoftware.jeb.core.PluginInformation;
import com.pnfsoftware.jeb.core.Version;
import com.pnfsoftware.jeb.core.input.IInput;
import com.pnfsoftware.jeb.core.properties.IPropertyDefinitionManager;
import com.pnfsoftware.jeb.core.units.AbstractUnitIdentifier;
import com.pnfsoftware.jeb.core.units.IUnit;
import com.pnfsoftware.jeb.core.units.IUnitProcessor;
import com.pnfsoftware.jeb.core.units.WellKnownUnitTypes;
import static com.pnfsoftware.jeb.util.collect.ArrayUtil.swap;
import com.pnfsoftware.jeb.util.logging.GlobalLog;
import com.pnfsoftware.jeb.util.logging.ILogger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;



public class MachOPlugin extends AbstractUnitIdentifier {
    @SuppressWarnings("unused")
    private static final ILogger logger = GlobalLog.getLogger(MachOPlugin.class);
    
    public static final String TYPE = WellKnownUnitTypes.typeAppleMacho;

    public MachOPlugin() {
        super(TYPE, 0);
    }

    @Override
    public PluginInformation getPluginInformation() {
        return new PluginInformation("Mach-O File Unit", "MacOS Mach-O parser plugin", "Markus Gothe", Version.create(0, 9, 9));
    }

    @Override
    public void initialize(IPropertyDefinitionManager parent) {
        super.initialize(parent);
    }

    @Override
    public boolean canIdentify(IInput input, IUnitCreator parent) {
        ByteBuffer magicbf = ByteBuffer.allocate(4);
        magicbf.putInt(MachO.MH_MAGIC);
        int magic;
        magic = magicbf.getInt();
        logger.info("%s", magic);
        return (checkBytes(input, 0, magic) /*|| checkBytes(input, 0, magic64)*/);
    }

    @Override
    public IUnit prepare(String name, IInput input, IUnitProcessor unitProcessor, IUnitCreator parent) {
        MachOUnit unit = new MachOUnit(name, input, unitProcessor, parent, pdm);
        unit.process();
        return unit;
    }
}
