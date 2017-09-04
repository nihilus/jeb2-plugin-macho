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

import java.nio.ByteBuffer;

/**
 * Class to hold ELF constants.
 */
public class MachO {

    // SHT (Section Header Type) constants
    public static final int SHT_NULL = 0;
    public static final int SHT_PROGBITS = 1;
    public static final int SHT_SYMTAB = 2;
    public static final int SHT_STRTAB = 3;
    public static final int SHT_RELA = 4;
    public static final int SHT_HASH = 5;
    public static final int SHT_DYNAMIC = 6;
    public static final int SHT_NOTE = 7;
    public static final int SHT_NOBITS = 8;
    public static final int SHT_REL = 9;
    public static final int SHT_SHLIB = 10;
    public static final int SHT_DYNSYM = 11;
    public static final int SHT_INIT_ARRAY = 14;
    public static final int SHT_FINI_ARRAY = 15;
    public static final int SHT_PREINIT_ARRAY = 16;
    public static final int SHT_GROUP = 17;
    public static final int SHT_SYMTAB_SHNDX = 18;
    public static final int SHT_LOOS = 0x60000000;
    public static final int SHT_HIOS = 0x6fffffff;
    public static final int SHT_LOPROC = 0x70000000;
    public static final int SHT_HIPROC = 0x7fffffff;
    public static final int SHT_LOUSER = 0x80000000;
    public static final int SHT_HIUSER = 0xffffffff;

    public static final int MH_MAGIC = 0xfeedface;
    public static final int MH_MAGIC64 = 0xfeedfacf;
    public static final int FAT_MAGIC = 0xcafebabe; /* This indicates a FAT Mach-O archive! */
  
    public static String getSHTString(int id) {
        switch(id) {
        case SHT_NULL:
            return "NULL";
        case SHT_PROGBITS:
            return "PROGBITS";
        case SHT_SYMTAB:
            return "SYMTAB";
        case SHT_STRTAB:
            return "STRTAB";
        case SHT_RELA:
            return "RELA";
        case SHT_HASH:
            return "HASH";
        case SHT_DYNAMIC:
            return "DYNAMIC";
        case SHT_NOTE:
            return "NOTE";
        case SHT_NOBITS:
            return "NOBITS";
        case SHT_REL:
            return "REL";
        case SHT_SHLIB:
            return "SHLIB";
        case SHT_DYNSYM:
            return "DYNSYM";
        case SHT_INIT_ARRAY:
            return "INIT_ARRAY";
        case SHT_FINI_ARRAY:
            return "FINI_ARRAY";
        case SHT_PREINIT_ARRAY:
            return "PREINIT_ARRAY";
        case SHT_GROUP:
            return "GROUP";
        case SHT_SYMTAB_SHNDX:
            return "SYMTAB_SHNDX";
        case SHT_LOOS:
            return "LOOS";
        case SHT_HIOS:
            return "HIOS";
        case SHT_LOPROC:
            return "LOPROC";
        case SHT_HIPROC:
            return "HIPROC";
        case SHT_LOUSER:
            return "LOUSER";
        case SHT_HIUSER:
            return "HIUSER";
        default:
            break;
        }
        return Integer.toHexString(id);
    }

    // Arm specific types
    public static final int SHT_ARM_EXIDX = 0x70000001;
    public static final int SHT_ARM_PREEMPTMAP = 0x70000002;
    public static final int SHT_ARM_ATTRIBUTES = 0x70000003;
    public static final int SHT_ARM_DEBUGOVERLAY = 0x70000004;
    public static final int SHT_ARM_OVERLAYSECTION = 0x70000005;

    public static final int SHF_WRITE = 1;
    public static final int SHF_ALLOC = 2;
    public static final int SHF_EXECINSTR = 4;
    public static final int SHF_MERGE = 0x10;
    public static final int SHF_STRINGS = 0x20;
    public static final int SHF_INFO_LINK = 0x40;
    public static final int SHF_LINK_ORDER = 0x80;
    public static final int SHF_OS_NONCONFORMING = 0x100;
    public static final int SHF_GROUP = 0x200;
    public static final int SHF_TLS = 0x400;
    public static final int SHF_COMPRESSED = 0x800;
    public static final int SHF_MASKOS = 0x0ff00000;
    public static final int SHF_MASKPROC = 0xf0000000;

    public static String getSHFString(int id) {
        switch(id) {
        case SHF_WRITE:
            return "WRITE";
        case SHF_ALLOC:
            return "ALLOC";
        case SHF_EXECINSTR:
            return "EXECINSTR";
        case SHF_MERGE:
            return "MERGE";
        case SHF_STRINGS:
            return "STRINGS";
        case SHF_INFO_LINK:
            return "INFO_LINK";
        case SHF_LINK_ORDER:
            return "LINK_ORDER";
        case SHF_OS_NONCONFORMING:
            return "OS_NONCONFORMING";
        case SHF_GROUP:
            return "GROUP";
        case SHF_TLS:
            return "TLS";
        case SHF_COMPRESSED:
            return "COMPRESSED";
        case SHF_MASKOS:
            return "MASKOS";
        case SHF_MASKPROC:
            return "MASKPROC";
        default:
            return null;
        }
    }

    // Elf data constants
    public static final byte ELFDATANONE = 0;
    // 2's complement little endian
    public static final byte ELFDATA2LSB = 1;
    // 2's complement big endian
    public static final byte ELFDATA2MSB = 2;

    public static String getELFDataString(int id) {
        switch(id) {
        case MachO.ELFDATANONE:
            return "NONE";
        case MachO.ELFDATA2LSB:
            return "2's complement, little endian";
        case MachO.ELFDATA2MSB:
            return "2's complement, big endian";
        default:
            return Integer.toHexString(id);
        }
    }

    // Elf version constants
    public static final int EV_NONE = 0;
    public static final int EV_CURRENT = 1;

    public static String getEVString(int id) {
        switch(id) {
        case EV_NONE:
            return "NONE";
        case EV_CURRENT:
            return "1 (CURRENT)";
        default:
            return Integer.toHexString(id);
        }
    }

    // Elf type constants
    public static final short ET_NONE = 0;
    public static final short ET_REL = 1;
    public static final short ET_EXEC = 2;
    public static final short ET_DYN = 3;
    public static final short ET_CORE = 4;
    public static final short ET_LOPROC = (short)0xff00;
    public static final short ET_HIPROC = (short)0xffff;

    public static String getETString(int id) {
        switch(id) {
        case ET_NONE:
            return "NONE (None)";
        case ET_REL:
            return "REL (Relocatable file)";
        case ET_EXEC:
            return "EXEC (Executable file)";
        case ET_DYN:
            return "DYN (Shared object file)";
        case ET_CORE:
            return "CORE (Core file)";
        default:
            return Integer.toHexString(id);
        }
    }

    // OS Application Binary Interfaces
    public static final int ELFOSABI_NONE = 0; // UNIX System V ABI
    public static final int ELFOSABI_HPUX = 1; // HP-UX operating system
    public static final int ELFOSABI_NETBSD = 2; // NetBSD
    public static final int ELFOSABI_GNU = 3; // GNU/Linux
    public static final int ELFOSABI_LINUX = 3; // Historical alias for
                                                // ELFOSABI_GNU.
    public static final int ELFOSABI_HURD = 4; // GNU/Hurd
    public static final int ELFOSABI_SOLARIS = 6; // Solaris
    public static final int ELFOSABI_AIX = 7; // AIX
    public static final int ELFOSABI_IRIX = 8; // IRIX
    public static final int ELFOSABI_FREEBSD = 9; // FreeBSD
    public static final int ELFOSABI_TRU64 = 10; // TRU64 UNIX
    public static final int ELFOSABI_MODESTO = 11; // Novell Modesto
    public static final int ELFOSABI_OPENBSD = 12; // OpenBSD
    public static final int ELFOSABI_OPENVMS = 13; // OpenVMS
    public static final int ELFOSABI_NSK = 14; // Hewlett-Packard Non-Stop
                                               // Kernel
    public static final int ELFOSABI_AROS = 15; // AROS
    public static final int ELFOSABI_FENIXOS = 16; // FenixOS
    public static final int ELFOSABI_C6000_ELFABI = 64; // Bare-metal
                                                        // TMS320C6000
    public static final int ELFOSABI_C6000_LINUX = 65; // Linux TMS320C6000
    public static final int ELFOSABI_ARM = 97; // ARM
    public static final int ELFOSABI_STANDALONE = 255; // Standalone (embedded)
                                                       // application

    public static String getOSABIString(int osabi) {
        switch(osabi) {
        case ELFOSABI_NONE:
            return "UNIX - System V";
        case ELFOSABI_HPUX:
            return "UNIX - HP-UX";
        case ELFOSABI_NETBSD:
            return "UNIX - NetBSD";
        case ELFOSABI_GNU:
            return "UNIX - GNU";
        case ELFOSABI_SOLARIS:
            return "UNIX - Solaris";
        case ELFOSABI_AIX:
            return "UNIX - AIX";
        case ELFOSABI_IRIX:
            return "UNIX - IRIX";
        case ELFOSABI_FREEBSD:
            return "UNIX - FreeBSD";
        case ELFOSABI_TRU64:
            return "UNIX - TRU64";
        case ELFOSABI_MODESTO:
            return "Novell - Modesto";
        case ELFOSABI_OPENBSD:
            return "UNIX - OpenBSD";
        case ELFOSABI_OPENVMS:
            return "VMS - OpenVMS";
        case ELFOSABI_NSK:
            return "HP - Non-Stop Kernel";
        case ELFOSABI_AROS:
            return "AROS";
        case ELFOSABI_FENIXOS:
            return "FenixOS";
        default:
            return "Unknown";
        }
    }

    // ELF machine
    public static final int EM_NONE = 0; // No machine
    public static final int EM_M32 = 1; // AT&T WE 32100
    public static final int EM_SPARC = 2; // SPARC
    public static final int EM_386 = 3; // Intel 386
    public static final int EM_68K = 4; // Motorola 68000
    public static final int EM_88K = 5; // Motorola 88000
    public static final int EM_486 = 6; // Intel 486 (deprecated)
    public static final int EM_860 = 7; // Intel 80860
    public static final int EM_MIPS = 8; // MIPS R3000
    public static final int EM_S370 = 9; // IBM System/370
    public static final int EM_MIPS_RS3_LE = 10; // MIPS RS3000 Little-endian
    public static final int EM_PARISC = 15; // Hewlett-Packard PA-RISC
    public static final int EM_VPP500 = 17; // Fujitsu VPP500
    public static final int EM_SPARC32PLUS = 18; // Enhanced instruction set
                                                 // SPARC
    public static final int EM_960 = 19; // Intel 80960
    public static final int EM_PPC = 20; // PowerPC
    public static final int EM_PPC64 = 21; // PowerPC64
    public static final int EM_S390 = 22; // IBM System/390
    public static final int EM_SPU = 23; // IBM SPU/SPC
    public static final int EM_V800 = 36; // NEC V800
    public static final int EM_FR20 = 37; // Fujitsu FR20
    public static final int EM_RH32 = 38; // TRW RH-32
    public static final int EM_RCE = 39; // Motorola RCE
    public static final int EM_ARM = 40; // ARM
    public static final int EM_ALPHA = 41; // DEC Alpha
    public static final int EM_SH = 42; // Hitachi SH
    public static final int EM_SPARCV9 = 43; // SPARC V9
    public static final int EM_TRICORE = 44; // Siemens TriCore
    public static final int EM_ARC = 45; // Argonaut RISC Core
    public static final int EM_H8_300 = 46; // Hitachi H8/300
    public static final int EM_H8_300H = 47; // Hitachi H8/300H
    public static final int EM_H8S = 48; // Hitachi H8S
    public static final int EM_H8_500 = 49; // Hitachi H8/500
    public static final int EM_IA_64 = 50; // Intel IA-64 processor architecture
    public static final int EM_MIPS_X = 51; // Stanford MIPS-X
    public static final int EM_COLDFIRE = 52; // Motorola ColdFire
    public static final int EM_68HC12 = 53; // Motorola M68HC12
    public static final int EM_MMA = 54; // Fujitsu MMA Multimedia Accelerator
    public static final int EM_PCP = 55; // Siemens PCP
    public static final int EM_NCPU = 56; // Sony nCPU embedded RISC processor
    public static final int EM_NDR1 = 57; // Denso NDR1 microprocessor
    public static final int EM_STARCORE = 58; // Motorola Star*Core processor
    public static final int EM_ME16 = 59; // Toyota ME16 processor
    public static final int EM_ST100 = 60; // STMicroelectronics ST100 processor
    public static final int EM_TINYJ = 61; // Advanced Logic Corp. TinyJ
                                           // embedded processor family
    public static final int EM_X86_64 = 62; // AMD x86-64 architecture
    public static final int EM_PDSP = 63; // Sony DSP Processor
    public static final int EM_PDP10 = 64; // Digital Equipment Corp. PDP-10
    public static final int EM_PDP11 = 65; // Digital Equipment Corp. PDP-11
    public static final int EM_FX66 = 66; // Siemens FX66 microcontroller
    public static final int EM_ST9PLUS = 67; // STMicroelectronics ST9+ 8/16 bit
                                             // microcontroller
    public static final int EM_ST7 = 68; // STMicroelectronics ST7 8-bit
                                         // microcontroller
    public static final int EM_68HC16 = 69; // Motorola MC68HC16 Microcontroller
    public static final int EM_68HC11 = 70; // Motorola MC68HC11 Microcontroller
    public static final int EM_68HC08 = 71; // Motorola MC68HC08 Microcontroller
    public static final int EM_68HC05 = 72; // Motorola MC68HC05 Microcontroller
    public static final int EM_SVX = 73; // Silicon Graphics SVx
    public static final int EM_ST19 = 74; // STMicroelectronics ST19 8-bit
                                          // microcontroller
    public static final int EM_VAX = 75; // Digital VAX
    public static final int EM_CRIS = 76; // Axis Communications 32-bit embedded
                                          // processor
    public static final int EM_JAVELIN = 77; // Infineon Technologies 32-bit
                                             // embedded processor
    public static final int EM_FIREPATH = 78; // Element 14 64-bit DSP Processor
    public static final int EM_ZSP = 79; // LSI Logic 16-bit DSP Processor
    public static final int EM_MMIX = 80; // Donald Knuth's educational 64-bit
                                          // processor
    public static final int EM_HUANY = 81; // Harvard University
                                           // machine-independent object files
    public static final int EM_PRISM = 82; // SiTera Prism
    public static final int EM_AVR = 83; // Atmel AVR 8-bit microcontroller
    public static final int EM_FR30 = 84; // Fujitsu FR30
    public static final int EM_D10V = 85; // Mitsubishi D10V
    public static final int EM_D30V = 86; // Mitsubishi D30V
    public static final int EM_V850 = 87; // NEC v850
    public static final int EM_M32R = 88; // Mitsubishi M32R
    public static final int EM_MN10300 = 89; // Matsushita MN10300
    public static final int EM_MN10200 = 90; // Matsushita MN10200
    public static final int EM_PJ = 91; // picoJava
    public static final int EM_OPENRISC = 92; // OpenRISC 32-bit embedded
                                              // processor
    public static final int EM_ARC_COMPACT = 93; // ARC International ARCompact
                                                 // processor (old
                                                 // spelling/synonym:
                                                 // EM_ARC_A5)
    public static final int EM_XTENSA = 94; // Tensilica Xtensa Architecture
    public static final int EM_VIDEOCORE = 95; // Alphamosaic VideoCore
                                               // processor
    public static final int EM_TMM_GPP = 96; // Thompson Multimedia General
                                             // Purpose Processor
    public static final int EM_NS32K = 97; // National Semiconductor 32000
                                           // series
    public static final int EM_TPC = 98; // Tenor Network TPC processor
    public static final int EM_SNP1K = 99; // Trebia SNP 1000 processor
    public static final int EM_ST200 = 100; // STMicroelectronics (www.st.com)
                                            // ST200
    public static final int EM_IP2K = 101; // Ubicom IP2xxx microcontroller
                                           // family
    public static final int EM_MAX = 102; // MAX Processor
    public static final int EM_CR = 103; // National Semiconductor CompactRISC
                                         // microprocessor
    public static final int EM_F2MC16 = 104; // Fujitsu F2MC16
    public static final int EM_MSP430 = 105; // Texas Instruments embedded
                                             // microcontroller msp430
    public static final int EM_BLACKFIN = 106; // Analog Devices Blackfin (DSP)
                                               // processor
    public static final int EM_SE_C33 = 107; // S1C33 Family of Seiko Epson
                                             // processors
    public static final int EM_SEP = 108; // Sharp embedded microprocessor
    public static final int EM_ARCA = 109; // Arca RISC Microprocessor
    public static final int EM_UNICORE = 110; // Microprocessor series from
                                              // PKU-Unity Ltd. and MPRC of
                                              // Peking University
    public static final int EM_EXCESS = 111; // eXcess: 16/32/64-bit
                                             // configurable embedded CPU
    public static final int EM_DXP = 112; // Icera Semiconductor Inc. Deep
                                          // Execution Processor
    public static final int EM_ALTERA_NIOS2 = 113; // Altera Nios II soft-core
                                                   // processor
    public static final int EM_CRX = 114; // National Semiconductor CompactRISC
                                          // CRX
    public static final int EM_XGATE = 115; // Motorola XGATE embedded processor
    public static final int EM_C166 = 116; // Infineon C16x/XC16x processor
    public static final int EM_M16C = 117; // Renesas M16C series
                                           // microprocessors
    public static final int EM_DSPIC30F = 118; // Microchip Technology dsPIC30F
                                               // Digital Signal Controller
    public static final int EM_CE = 119; // Freescale Communication Engine RISC
                                         // core
    public static final int EM_M32C = 120; // Renesas M32C series
                                           // microprocessors
    public static final int EM_TSK3000 = 131; // Altium TSK3000 core
    public static final int EM_RS08 = 132; // Freescale RS08 embedded processor
    public static final int EM_SHARC = 133; // Analog Devices SHARC family of
                                            // 32-bit DSP processors
    public static final int EM_ECOG2 = 134; // Cyan Technology eCOG2
                                            // microprocessor
    public static final int EM_SCORE7 = 135; // Sunplus S+core7 RISC processor
    public static final int EM_DSP24 = 136; // New Japan Radio (NJR) 24-bit DSP
                                            // Processor
    public static final int EM_VIDEOCORE3 = 137; // Broadcom VideoCore III
                                                 // processor
    public static final int EM_LATTICEMICO32 = 138; // RISC processor for
                                                    // Lattice FPGA architecture
    public static final int EM_SE_C17 = 139; // Seiko Epson C17 family
    public static final int EM_TI_C6000 = 140; // The Texas Instruments
                                               // TMS320C6000 DSP family
    public static final int EM_TI_C2000 = 141; // The Texas Instruments
                                               // TMS320C2000 DSP family
    public static final int EM_TI_C5500 = 142; // The Texas Instruments
                                               // TMS320C55x DSP family
    public static final int EM_MMDSP_PLUS = 160; // STMicroelectronics 64bit
                                                 // VLIW Data Signal
                                                 // Processor
    public static final int EM_CYPRESS_M8C = 161; // Cypress M8C microprocessor
    public static final int EM_R32C = 162; // Renesas R32C series
                                           // microprocessors
    public static final int EM_TRIMEDIA = 163; // NXP Semiconductors TriMedia
                                               // architecture family
    public static final int EM_HEXAGON = 164; // Qualcomm Hexagon processor
    public static final int EM_8051 = 165; // Intel 8051 and variants
    public static final int EM_STXP7X = 166; // STMicroelectronics STxP7x family
                                             // of configurable and
                                             // extensible RISC processors
    public static final int EM_NDS32 = 167; // Andes Technology compact code
                                            // size embedded RISC processor
                                            // family
    public static final int EM_ECOG1 = 168; // Cyan Technology eCOG1X family
    public static final int EM_ECOG1X = 168; // Cyan Technology eCOG1X family
    public static final int EM_MAXQ30 = 169; // Dallas Semiconductor MAXQ30 Core
                                             // Micro-controllers
    public static final int EM_XIMO16 = 170; // New Japan Radio (NJR) 16-bit DSP
                                             // Processor
    public static final int EM_MANIK = 171; // M2000 Reconfigurable RISC
                                            // Microprocessor
    public static final int EM_CRAYNV2 = 172; // Cray Inc. NV2 vector
                                              // architecture
    public static final int EM_RX = 173; // Renesas RX family
    public static final int EM_METAG = 174; // Imagination Technologies META
                                            // processor architecture
    public static final int EM_MCST_ELBRUS = 175; // MCST Elbrus general purpose
                                                  // hardware architecture
    public static final int EM_ECOG16 = 176; // Cyan Technology eCOG16 family
    public static final int EM_CR16 = 177; // National Semiconductor CompactRISC
                                           // CR16 16-bit microprocessor
    public static final int EM_ETPU = 178; // Freescale Extended Time Processing
                                           // Unit
    public static final int EM_SLE9X = 179; // Infineon Technologies SLE9X core
    public static final int EM_L10M = 180; // Intel L10M
    public static final int EM_K10M = 181; // Intel K10M
    public static final int EM_AARCH64 = 183; // ARM AArch64
    public static final int EM_AVR32 = 185; // Atmel Corporation 32-bit
                                            // microprocessor family
    public static final int EM_STM8 = 186; // STMicroeletronics STM8 8-bit
                                           // microcontroller
    public static final int EM_TILE64 = 187; // Tilera TILE64 multicore
                                             // architecture family
    public static final int EM_TILEPRO = 188; // Tilera TILEPro multicore
                                              // architecture family
    public static final int EM_CUDA = 190; // NVIDIA CUDA architecture
    public static final int EM_TILEGX = 191; // Tilera TILE-Gx multicore
                                             // architecture family
    public static final int EM_CLOUDSHIELD = 192; // CloudShield architecture
                                                  // family
    public static final int EM_COREA_1ST = 193; // KIPO-KAIST Core-A 1st
                                                // generation processor family
    public static final int EM_COREA_2ND = 194; // KIPO-KAIST Core-A 2nd
                                                // generation processor family
    public static final int EM_ARC_COMPACT2 = 195; // Synopsys ARCompact V2
    public static final int EM_OPEN8 = 196; // Open8 8-bit RISC soft processor
                                            // core
    public static final int EM_RL78 = 197; // Renesas RL78 family
    public static final int EM_VIDEOCORE5 = 198; // Broadcom VideoCore V
                                                 // processor
    public static final int EM_78KOR = 199; // Renesas 78KOR family
    public static final int EM_56800EX = 200; // Freescale 56800EX Digital
                                              // Signal Controller (DSCc)

    public static String getEMString(int id) {
        switch(id) {
        case EM_NONE:
            return "None";
        case EM_AARCH64:
            return "AArch64";
        case EM_M32:
            return "WE32100";
        case EM_SPARC:
            return "Sparc";
        case EM_SPU:
            return "SPU";
        case EM_386:
            return "Intel 80386";
        case EM_68K:
            return "MC68000";
        case EM_88K:
            return "MC88000";
        case EM_486:
            return "Intel 80486";
        case EM_860:
            return "Intel 80860";
        case EM_MIPS:
            return "MIPS R3000";
        case EM_S370:
            return "IBM System/370";
        case EM_MIPS_RS3_LE:
            return "MIPS R4000 big-endian";
        case EM_PARISC:
            return "HPPA";
        case EM_SPARC32PLUS:
            return "Sparc v8+";
        case EM_960:
            return "Intel 90860";
        case EM_PPC:
            return "PowerPC";
        case EM_PPC64:
            return "PowerPC64";
        case EM_FR20:
            return "Fujitsu FR20";
        case EM_RH32:
            return "TRW RH32";
        case EM_ARM:
            return "ARM";
        case EM_SH:
            return "Renesas / SuperH SH";
        case EM_SPARCV9:
            return "Sparc v9";
        case EM_TRICORE:
            return "Siemens Tricore";
        case EM_ARC:
            return "ARC";
        case EM_H8_300:
            return "Renesas H8/300";
        case EM_H8_300H:
            return "Renesas H8/300H";
        case EM_H8S:
            return "Renesas H8S";
        case EM_H8_500:
            return "Renesas H8/500";
        case EM_IA_64:
            return "Intel IA-64";
        case EM_MIPS_X:
            return "Stanford MIPS-X";
        case EM_COLDFIRE:
            return "Motorola Coldfire";
        case EM_ALPHA:
            return "Alpha";
        case EM_D10V:
            return "d10v";
        case EM_D30V:
            return "d30v";
        case EM_M32R:
            return "Renesas M32R (formerly Mitsubishi M32r)";
        case EM_V800:
            return "Renesas V850 (using RH850 ABI)";
        case EM_V850:
            return "Renesas V850";
        case EM_MN10300:
            return "mn10300";
        case EM_MN10200:
            return "mn10200";
        case EM_FR30:
            return "Fujitsu FR30";
        case EM_PJ:
            return "picoJava";
        case EM_MMA:
            return "Fujitsu Multimedia Accelerator";
        case EM_PCP:
            return "Siemens PCP";
        case EM_NCPU:
            return "Sony nCPU embedded RISC processor";
        case EM_NDR1:
            return "Denso NDR1 microprocesspr";
        case EM_STARCORE:
            return "Motorola Star*Core processor";
        case EM_ME16:
            return "Toyota ME16 processor";
        case EM_ST100:
            return "STMicroelectronics ST100 processor";
        case EM_TINYJ:
            return "Advanced Logic Corp. TinyJ embedded processor";
        case EM_PDSP:
            return "Sony DSP processor";
        case EM_PDP10:
            return "Digital Equipment Corp. PDP-10";
        case EM_PDP11:
            return "Digital Equipment Corp. PDP-11";
        case EM_FX66:
            return "Siemens FX66 microcontroller";
        case EM_ST9PLUS:
            return "STMicroelectronics ST9+ 8/16 bit microcontroller";
        case EM_ST7:
            return "STMicroelectronics ST7 8-bit microcontroller";
        case EM_68HC16:
            return "Motorola MC68HC16 Microcontroller";
        case EM_68HC12:
            return "Motorola MC68HC12 Microcontroller";
        case EM_68HC11:
            return "Motorola MC68HC11 Microcontroller";
        case EM_68HC08:
            return "Motorola MC68HC08 Microcontroller";
        case EM_68HC05:
            return "Motorola MC68HC05 Microcontroller";
        case EM_SVX:
            return "Silicon Graphics SVx";
        case EM_ST19:
            return "STMicroelectronics ST19 8-bit microcontroller";
        case EM_VAX:
            return "Digital VAX";
        case EM_AVR:
            return "Atmel AVR 8-bit microcontroller";
        case EM_CRIS:
            return "Axis Communications 32-bit embedded processor";
        case EM_JAVELIN:
            return "Infineon Technologies 32-bit embedded cpu";
        case EM_FIREPATH:
            return "Element 14 64-bit DSP processor";
        case EM_ZSP:
            return "LSI Logic's 16-bit DSP processor";
        case EM_MMIX:
            return "Donald Knuth's educational 64-bit processor";
        case EM_HUANY:
            return "Harvard Universitys's machine-independent object format";
        case EM_PRISM:
            return "Vitesse Prism";
        case EM_S390:
            return "IBM S/390";
        case EM_XTENSA:
            return "Tensilica Xtensa Processor";
        case EM_VIDEOCORE:
            return "Alphamosaic VideoCore processor";
        case EM_TMM_GPP:
            return "Thompson Multimedia General Purpose Processor";
        case EM_NS32K:
            return "National Semiconductor 32000 series";
        case EM_TPC:
            return "Tenor Network TPC processor";
        case EM_ST200:
            return "STMicroelectronics ST200 microcontroller";
        case EM_MAX:
            return "MAX Processor";
        case EM_CR:
            return "National Semiconductor CompactRISC";
        case EM_F2MC16:
            return "Fujitsu F2MC16";
        case EM_MSP430:
            return "Texas Instruments msp430 microcontroller";
        case EM_LATTICEMICO32:
            return "Lattice Mico32";
        case EM_M32C:
            return "Renesas M32c";
        case EM_BLACKFIN:
            return "Analog Devices Blackfin";
        case EM_SE_C33:
            return "S1C33 Family of Seiko Epson processors";
        case EM_SEP:
            return "Sharp embedded microprocessor";
        case EM_ARCA:
            return "Arca RISC microprocessor";
        case EM_UNICORE:
            return "Unicore";
        case EM_EXCESS:
            return "eXcess 16/32/64-bit configurable embedded CPU";
        case EM_DXP:
            return "Icera Semiconductor Inc. Deep Execution Processor";
        case EM_ALTERA_NIOS2:
            return "Altera Nios II";
        case EM_C166:
        case EM_M16C:
            return "Renesas M16C series microprocessors";
        case EM_DSPIC30F:
            return "Microchip Technology dsPIC30F Digital Signal Controller";
        case EM_CE:
            return "Freescale Communication Engine RISC core";
        case EM_TSK3000:
            return "Altium TSK3000 core";
        case EM_RS08:
            return "Freescale RS08 embedded processor";
        case EM_ECOG2:
            return "Cyan Technology eCOG2 microprocessor";
        case EM_DSP24:
            return "New Japan Radio (NJR) 24-bit DSP Processor";
        case EM_VIDEOCORE3:
            return "Broadcom VideoCore III processor";
        case EM_SE_C17:
            return "Seiko Epson C17 family";
        case EM_TI_C6000:
            return "Texas Instruments TMS320C6000 DSP family";
        case EM_TI_C2000:
            return "Texas Instruments TMS320C2000 DSP family";
        case EM_TI_C5500:
            return "Texas Instruments TMS320C55x DSP family";
        case EM_MMDSP_PLUS:
            return "STMicroelectronics 64bit VLIW Data Signal Processor";
        case EM_CYPRESS_M8C:
            return "Cypress M8C microprocessor";
        case EM_R32C:
            return "Renesas R32C series microprocessors";
        case EM_TRIMEDIA:
            return "NXP Semiconductors TriMedia architecture family";
        case EM_8051:
            return "Intel 8051 and variants";
        case EM_STXP7X:
            return "STMicroelectronics STxP7x family";
        case EM_NDS32:
            return "Andes Technology compact code size embedded RISC processor family";
        case EM_ECOG1X:
            return "Cyan Technology eCOG1X family";
        case EM_MAXQ30:
            return "Dallas Semiconductor MAXQ30 Core microcontrollers";
        case EM_XIMO16:
            return "New Japan Radio (NJR) 16-bit DSP Processor";
        case EM_MANIK:
            return "M2000 Reconfigurable RISC Microprocessor";
        case EM_CRAYNV2:
            return "Cray Inc. NV2 vector architecture";
        case EM_CR16:
        case EM_RL78:
            return "Renesas RL78";
        case EM_RX:
            return "Renesas RX";
        case EM_METAG:
            return "Imagination Technologies Meta processor architecture";
        case EM_MCST_ELBRUS:
            return "MCST Elbrus general purpose hardware architecture";
        case EM_ECOG16:
            return "Cyan Technology eCOG16 family";
        case EM_ETPU:
            return "Freescale Extended Time Processing Unit";
        case EM_SLE9X:
            return "Infineon Technologies SLE9X core";
        case EM_AVR32:
            return "Atmel Corporation 32-bit microprocessor family";
        case EM_STM8:
            return "STMicroeletronics STM8 8-bit microcontroller";
        case EM_TILE64:
            return "Tilera TILE64 multicore architecture family";
        case EM_TILEPRO:
            return "Tilera TILEPro multicore architecture family";
        case EM_TILEGX:
            return "Tilera TILE-Gx multicore architecture family";
        case EM_CUDA:
            return "NVIDIA CUDA architecture";
        case EM_XGATE:
            return "Motorola XGATE embedded processor";
        default:
            return "Unknown";
        }
    }

    // Dynamic array tags
    public static final int DT_NULL = 0;
    public static final int DT_NEEDED = 1;
    public static final int DT_PLTRELSZ = 2;
    public static final int DT_PLTGOT = 3;
    public static final int DT_HASH = 4;
    public static final int DT_STRTAB = 5;
    public static final int DT_SYMTAB = 6;
    public static final int DT_RELASZ = 8;
    public static final int DT_RELAENT = 9;
    public static final int DT_RELA = 7;
    public static final int DT_STRSZ = 10;
    public static final int DT_SYMENT = 11;
    public static final int DT_INIT = 12;
    public static final int DT_FINI = 13;
    public static final int DT_SONAME = 14;
    public static final int DT_RPATH = 15;
    public static final int DT_SYMBOLIC = 16;
    public static final int DT_REL = 17;
    public static final int DT_RELSZ = 18;
    public static final int DT_RELENT = 19;
    public static final int DT_PLTREL = 20;
    public static final int DT_DEBUG = 21;
    public static final int DT_TEXTREL = 22;
    public static final int DT_JMPREL = 23;
    public static final int DT_LOPROC = 0x70000000;
    public static final int DT_HIPROC = 0x7fffffff;

    // Mips specific

    private static final int DT_MIPS_RLD_VERSION = 0x70000001;
    private static final int DT_MIPS_TIME_STAMP = 0x70000002;
    private static final int DT_MIPS_ICHECKSUM = 0x70000003;
    private static final int DT_MIPS_IVERSION = 0x70000004;
    private static final int DT_MIPS_FLAGS = 0x70000005;
    private static final int DT_MIPS_BASE_ADDRESS = 0x70000006;
    private static final int DT_MIPS_MSYM = 0x70000007;
    private static final int DT_MIPS_CONFLICT = 0x70000008;
    private static final int DT_MIPS_LIBLIST = 0x70000009;
    private static final int DT_MIPS_LOCAL_GOTNO = 0x7000000a;
    private static final int DT_MIPS_CONFLICTNO = 0x7000000b;
    private static final int DT_MIPS_LIBLISTNO = 0x70000010;
    private static final int DT_MIPS_SYMTABNO = 0x70000011;
    private static final int DT_MIPS_UNREFEXTNO = 0x70000012;
    private static final int DT_MIPS_GOTSYM = 0x70000013;
    private static final int DT_MIPS_HIPAGENO = 0x70000014;
    private static final int DT_MIPS_RLD_MAP = 0x70000016;
    private static final int DT_MIPS_DELTA_CLASS = 0x70000017;
    private static final int DT_MIPS_DELTA_CLASS_NO = 0x70000018;
    private static final int DT_MIPS_DELTA_INSTANCE = 0x70000019;
    private static final int DT_MIPS_DELTA_INSTANCE_NO = 0x7000001a;
    private static final int DT_MIPS_DELTA_RELOC = 0x7000001b;
    private static final int DT_MIPS_DELTA_RELOC_NO = 0x7000001c;
    private static final int DT_MIPS_DELTA_SYM = 0x7000001d;
    private static final int DT_MIPS_DELTA_SYM_NO = 0x7000001e;
    private static final int DT_MIPS_DELTA_CLASSSYM = 0x70000020;
    private static final int DT_MIPS_DELTA_CLASSSYM_NO = 0x70000021;
    private static final int DT_MIPS_CXX_FLAGS = 0x70000022;
    private static final int DT_MIPS_PIXIE_INIT = 0x70000023;
    private static final int DT_MIPS_SYMBOL_LIB = 0x70000024;
    private static final int DT_MIPS_LOCALPAGE_GOTIDX = 0x70000025;
    private static final int DT_MIPS_LOCAL_GOTIDX = 0x70000026;
    private static final int DT_MIPS_HIDDEN_GOTIDX = 0x70000027;
    private static final int DT_MIPS_PROTECTED_GOTIDX = 0x70000028;
    private static final int DT_MIPS_OPTIONS = 0x70000029;
    private static final int DT_MIPS_INTERFACE = 0x7000002a;
    private static final int DT_MIPS_DYNSTR_ALIGN = 0x7000002b;
    private static final int DT_MIPS_INTERFACE_SIZE = 0x7000002c;
    private static final int DT_MIPS_RLD_TEXT_RESOLVE_ADDR = 0x7000002d;
    private static final int DT_MIPS_PERF_SUFFIX = 0x7000002e;
    private static final int DT_MIPS_COMPACT_SIZE = 0x7000002f;
    private static final int DT_MIPS_GP_VALUE = 0x70000030;
    private static final int DT_MIPS_AUX_DYNAMIC = 0x70000031;
    private static final int DT_MIPS_PLTGOT = 0x70000032;
    private static final int DT_MIPS_RWPLT = 0x70000034;

    public static String getDT(int id) {
        switch(id) {
        case DT_NULL:
            return "NULL";
        case DT_NEEDED:
            return "NEEDED";
        case DT_PLTRELSZ:
            return "PLTRELSZ";
        case DT_PLTGOT:
            return "PLTGOT";
        case DT_HASH:
            return "HASH";
        case DT_STRTAB:
            return "STRTAB";
        case DT_SYMTAB:
            return "SYMTAB";
        case DT_RELASZ:
            return "RELASZ";
        case DT_RELAENT:
            return "RELAENT";
        case DT_RELA:
            return "RELA";
        case DT_STRSZ:
            return "STRSZ";
        case DT_SYMENT:
            return "SYMENT";
        case DT_INIT:
            return "INIT";
        case DT_FINI:
            return "FINI";
        case DT_SONAME:
            return "SONAME";
        case DT_RPATH:
            return "RPATH";
        case DT_SYMBOLIC:
            return "SYMBOLIC";
        case DT_REL:
            return "REL";
        case DT_RELSZ:
            return "RELSZ";
        case DT_RELENT:
            return "RELENT";
        case DT_PLTREL:
            return "PLTREL";
        case DT_DEBUG:
            return "DEBUG";
        case DT_TEXTREL:
            return "TEXTREL";
        case DT_JMPREL:
            return "JMPREL";
        case DT_LOPROC:
            return "LOPROC";
        case DT_HIPROC:
            return "HIPROC";

            // MIPS Specific
        case DT_MIPS_RLD_VERSION:
            return "MIPS_RLD_VERSION";
        case DT_MIPS_TIME_STAMP:
            return "MIPS_TIME_STAMP";
        case DT_MIPS_ICHECKSUM:
            return "MIPS_ICHECKSUM";
        case DT_MIPS_IVERSION:
            return "MIPS_IVERSION";
        case DT_MIPS_FLAGS:
            return "MIPS_FLAGS";
        case DT_MIPS_BASE_ADDRESS:
            return "MIPS_BASE_ADDRESS";
        case DT_MIPS_MSYM:
            return "MIPS_MSYM";
        case DT_MIPS_CONFLICT:
            return "MIPS_CONFLICT";
        case DT_MIPS_LIBLIST:
            return "MIPS_LIBLIST";
        case DT_MIPS_LOCAL_GOTNO:
            return "MIPS_LOCAL_GOTNO";
        case DT_MIPS_CONFLICTNO:
            return "MIPS_CONFLICTNO";
        case DT_MIPS_LIBLISTNO:
            return "MIPS_LIBLISTNO";
        case DT_MIPS_SYMTABNO:
            return "MIPS_SYMTABNO";
        case DT_MIPS_UNREFEXTNO:
            return "MIPS_UNREFEXTNO";
        case DT_MIPS_GOTSYM:
            return "MIPS_GOTSYM";
        case DT_MIPS_HIPAGENO:
            return "MIPS_HIPAGENO";
        case DT_MIPS_RLD_MAP:
            return "MIPS_RLD_MAP";
        case DT_MIPS_DELTA_CLASS:
            return "MIPS_DELTA_CLASS";
        case DT_MIPS_DELTA_CLASS_NO:
            return "MIPS_DELTA_CLASS_NO";
        case DT_MIPS_DELTA_INSTANCE:
            return "MIPS_DELTA_INSTANCE";
        case DT_MIPS_DELTA_INSTANCE_NO:
            return "MIPS_DELTA_INSTANCE_NO";
        case DT_MIPS_DELTA_RELOC:
            return "MIPS_DELTA_RELOC";
        case DT_MIPS_DELTA_RELOC_NO:
            return "MIPS_DELTA_RELOC_NO";
        case DT_MIPS_DELTA_SYM:
            return "MIPS_DELTA_SYM";
        case DT_MIPS_DELTA_SYM_NO:
            return "MIPS_DELTA_SYM_NO";
        case DT_MIPS_DELTA_CLASSSYM:
            return "MIPS_DELTA_CLASSSYM";
        case DT_MIPS_DELTA_CLASSSYM_NO:
            return "MIPS_DELTA_CLASSSYM_NO";
        case DT_MIPS_CXX_FLAGS:
            return "MIPS_CXX_FLAGS";
        case DT_MIPS_PIXIE_INIT:
            return "MIPS_PIXIE_INIT";
        case DT_MIPS_SYMBOL_LIB:
            return "MIPS_SYMBOL_LIB";
        case DT_MIPS_LOCALPAGE_GOTIDX:
            return "MIPS_LOCALPAGE_GOTIDX";
        case DT_MIPS_LOCAL_GOTIDX:
            return "MIPS_LOCAL_GOTIDX";
        case DT_MIPS_HIDDEN_GOTIDX:
            return "MIPS_HIDDEN_GOTIDX";
        case DT_MIPS_PROTECTED_GOTIDX:
            return "MIPS_PROTECTED_GOTIDX";
        case DT_MIPS_OPTIONS:
            return "MIPS_OPTIONS";
        case DT_MIPS_INTERFACE:
            return "MIPS_INTERFACE";
        case DT_MIPS_DYNSTR_ALIGN:
            return "MIPS_DYNSTR_ALIGN";
        case DT_MIPS_INTERFACE_SIZE:
            return "MIPS_INTERFACE_SIZE";
        case DT_MIPS_RLD_TEXT_RESOLVE_ADDR:
            return "MIPS_RLD_TEXT_RESOLVE_ADDR";
        case DT_MIPS_PERF_SUFFIX:
            return "MIPS_PERF_SUFFIX";
        case DT_MIPS_COMPACT_SIZE:
            return "MIPS_COMPACT_SIZE";
        case DT_MIPS_GP_VALUE:
            return "MIPS_GP_VALUE";
        case DT_MIPS_AUX_DYNAMIC:
            return "MIPS_AUX_DYNAMIC";
        case DT_MIPS_PLTGOT:
            return "MIPS_PLTGOT";
        case DT_MIPS_RWPLT:
            return "MIPS_RWPLT";
        default:
            return Integer.toHexString(id);
        }
    }

    public static final int PT_NULL = 0;
    public static final int PT_LOAD = 1;
    public static final int PT_DYNAMIC = 2;
    public static final int PT_INTERP = 3;
    public static final int PT_NOTE = 4;
    public static final int PT_SHLIB = 5;
    public static final int PT_PHDR = 6;
    public static final int PT_TLS = 7;
    public static final int PT_LOOS = 0x60000000;
    public static final int PT_HIOS = 0x6fffffff;
    public static final int PT_LOPROC = 0x70000000;
    public static final int PT_HIPROC = 0x7fffffff;
    // GNU extensions
    public static final int PT_GNU_EH_FRAME = (PT_LOOS + 0x474e550);
    public static final int PT_GNU_RELRO = (PT_LOOS + 0x474e552);
    public static final int PT_GNU_STACK = PT_LOOS + 0x474e551;

    public static String getPTString(int id) {
        switch(id) {
        case PT_NULL:
            return "NULL";
        case PT_LOAD:
            return "LOAD";
        case PT_DYNAMIC:
            return "DYNAMIC";
        case PT_INTERP:
            return "INTERP";
        case PT_NOTE:
            return "NOTE";
        case PT_SHLIB:
            return "SHLIB";
        case PT_PHDR:
            return "PHDR";
        case PT_TLS:
            return "TLS";
        case PT_LOOS:
            return "LOOS";
        case PT_HIOS:
            return "HIOS";
        case PT_LOPROC:
            return "LOPROC";
        case PT_HIPROC:
            return "HIPROC";
        case PT_GNU_EH_FRAME:
            return "GNU_EH_FRAME";
        case PT_GNU_RELRO:
            return "GNU_RELRO";
        case PT_GNU_STACK:
            return "GNU_STACK";
        default:
            return Integer.toHexString(id);
        }
    }

    public static final int PF_X = 0x1;
    public static final int PF_W = 0x2;
    public static final int PF_R = 0x4;
    public static final int PF_MASKOS = 0x0ff00000;
    public static final int PF_MASKPROC = 0xf0000000;

    // Symbol table entry bind
    public static final int STB_LOCAL = 0;
    public static final int STB_GLOBAL = 1;
    public static final int STB_WEAK = 2;
    public static final int STB_LOPROC = 13;
    public static final int STB_HIPROC = 15;

    public static String getSTBString(int id) {
        switch(id) {
        case MachO.STB_LOCAL:
            return "LOCAL";
        case MachO.STB_GLOBAL:
            return "GLOBAL";
        case MachO.STB_WEAK:
            return "WEAK";
        case MachO.STB_LOPROC:
            return "LOPROC";
        case MachO.STB_HIPROC:
            return "HIPROC";
        default:
            return Integer.toHexString(id);
        }
    }

    // Symbol table entry type
    public static final int STT_NOTYPE = 0;
    public static final int STT_OBJECT = 1;
    public static final int STT_FUNC = 2;
    public static final int STT_SECTION = 3;
    public static final int STT_FILE = 4;
    public static final int STT_LOPROC = 13;
    public static final int STT_HIPROC = 15;

    public static String getSTTString(int id) {
        switch(id) {
        case MachO.STT_NOTYPE:
            return "NOTYPE";
        case MachO.STT_OBJECT:
            return "OBJECT";
        case MachO.STT_FUNC:
            return "FUNC";
        case MachO.STT_SECTION:
            return "SECTION";
        case MachO.STT_FILE:
            return "FILE";
        case MachO.STT_LOPROC:
            return "LOPROC";
        case MachO.STT_HIPROC:
            return "HIPROC";
        default:
            return Integer.toHexString(id);
        }
    }

    public static enum R_SYMBOL {
        LOCAL, EXTERNAL
    };

    public static final int R_MIPS_NONE = 0;
    public static final int R_MIPS_REL_32 = 3;
    public static final int R_386_JUMP_SLOT = 7;

    public static int relocate(int id, int A, int ABitCount, int AHL, int P, int S, int G, int GP, int GP0, int EA, int L, R_SYMBOL sym) {
        // int DTP_OFFSET = 0x8000;
        // int TP_OFFSET = 0x8000;

        switch(id) {
        case R_MIPS_NONE:
            return 0;
        case R_MIPS_REL_32:
            return S + A - EA;
        default:
            break;
        }
        throw new RuntimeException(String.format("Relocation type %d not recognized. Please add it", id));
    }

    // Sign extension function
    public static int SE(int operand, int opSize) {
        return operand | (0xFFFFFFFF << opSize);
    }

    // Get the high 16 bits
    public static int high(int x) {
        return (x - (short)x) >> 16;
    }
}
