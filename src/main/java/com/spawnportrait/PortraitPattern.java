package com.spawnportrait;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 96x128 block-pixel portrait.
 * Row 0 is the top of the mural; column 0 is the viewer's left.
 * Generated from docs/portrait-coordinates.csv — do not hand-edit both.
 */
public final class PortraitPattern {
    private PortraitPattern() {
    }

    public static final int WIDTH = 96;
    public static final int HEIGHT = 128;

    private static final String[] ROWS = {
            ".................................QQQQXbTTJFFFFFFFFFFFFJJTbXQQQQ.................................",
            "..............................QQQbJFAAAAAAAAAAAAAAAAAAAAAAAFFJdQQQ..............................",
            "...........................QQQbFFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFFbQQQ...........................",
            ".........................QQdJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFJdQQ.........................",
            ".......................QQdFAAAAAAAAAAFFFFFFFFFJFFFFFFFFFFFFAAAAAAAAAAFdQ........................",
            "......................QXJAAAAAAAAFFFFFFFFJJJJJJJJFFJJFFJJFFFFFFAAAAAAAAJQQ......................",
            ".....................QTFAAAAAAFFFFFFJFJJJJJJJJJFJFJJJJJJJJJJJJFFFFAAAAAAFgQ.....................",
            "....................QJAAAAAAFFFFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFAAAAAAJQ....................",
            "...................QFAAAAAFFFFFFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJFJJJFFFFAAAAAFQ...................",
            ".................QQFAAAAFFFFFFFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFAAAAAFQ..................",
            "................QXFAAAAFFFFFFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFJFJJFFFFAAAAFQ.................",
            "................QFAAAAFFFJFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFAAAAFQ................",
            "...............QFAAAAFFJJJJFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFJJJJJFJJFFFFAAAAFQ...............",
            "..............QJAAAAFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFJJJJFFJJFFFFAAAAJQ..............",
            ".............QgAAAAFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFJJJFFFFJJJJFJFJJJFFFAAAAbQ.............",
            ".............QFAAAFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFJJJJJJFJJJFFFFAAAFQ.............",
            "............QFAAAFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFJJFJJJFJJJFFFFAAAJQ............",
            "...........QgAAAAFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFJJJJJFJFFFFAAAAbQ...........",
            "...........QFAAAFFFJJJJJJJJJJJJFFJJJJJJJJJJJJJJJJJJJJJFFJFFFFFFFFFFJJJJJFJFFFFFFAAAFQ...........",
            "..........QJAAAFFJJJJJJJJJJJJJJFJJJJJJJJJJJJJJFFJFFFFFFFJJFFFFFFFFFFJJJJJJJJFFFFFAAATQ..........",
            "..........QAAAAFJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJFFFFAAAAQ..........",
            ".........QJAAAFFFJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFJFFFFFJJFJJJJJJJJJJFFFFAAAJQ.........",
            "........QXAAAAFFJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJFFFFAAAFQ.........",
            "........QJAAAFFJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFJJJFJJJJJJJJJJJJJJFFFFAAATQ........",
            "........QFAAAFFJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJFFFFAAAFQ........",
            ".......QgAAAFJJJJJJJJJJJJJJJJJJJJJJJFFJFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJJFFFFAAAgQ.......",
            ".......QFAAAFJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJFFFAAAFQ.......",
            "......QdAAAFFJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJFFFFAAAXQ......",
            "......QTAAAFJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJFFFAAATQ......",
            "......QFAAAFJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFJFJFJJJJJJJJJJJJJJJJFFAAAFQ......",
            "......QAAAFFJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJJJJJJFFFAAAQ......",
            ".....QbAAAFJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJJJJFFFAAAbQ.....",
            ".....QTAAAFJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJFJJJJJJJJJJJJJJJFFFAAATQ.....",
            ".....QJAAAFJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJFFJJJJJJJJJJJJJJFFAAAJQ.....",
            ".....QFAAAFJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJFJJJJJJJJJJJFFAAAFQ.....",
            ".....QFAAFFJJJJJJJJJJJJJJJJJJJJJJJFFFJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJFJJJJJJJJJJJJJJFFFAAFQ.....",
            ".....QAAAFFJJJJJJJJJJJJJJJJJJJJJJFJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJFFJJJJJJJJJJJJJJFFFAAAQ.....",
            ".....QAAAFFJJJJJJJJJJJJJJJJJFJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJJFFFFAAAQ.....",
            "....QkAAAFFJJJJJJJJJJJJJJJJJFJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJJJFFFFAAAkQ....",
            "....QGAAAFFJJJJJJJJJJJJJJJFJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJJFFFFFAAAkQ....",
            "....QXAAAFFJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJFFFFFFFAAAXQ....",
            "....QXAAAFFJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJFFFFFFFAAAXQ....",
            "....QXAAAFFJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJFFFFFFFAAAXQ....",
            "....QXAAAFFFJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJFFFFFFFFFAAAXQ....",
            "....QJAAAFFFFJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJFFFFFFFFAAAXQ....",
            "....QJAAAFFFFJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFXXXXXXXFFFFFFFFFFFFFFFFFFFFFFFFJFFFFFFFFFFAAAJQ....",
            "....QJAAAFFFFFJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAAAJQ....",
            "....QJAAAFFFFFJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFXXXXXXXFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAAAJQ....",
            "....QJAAAFFFFFJJJJJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFJJJJJJJJFFFFFFFFFFFFAAAJQ....",
            "....QJAAAFFFFFJJJJJJTJTTTJJJJJJJJJJJJJJJJFFFFFJFFFFFJFJJJJJJJJJJJTTJJJJTTTTTJJFFFFFFFFFAAAJQ....",
            "....QJAAAFFFFFFFJTXQQQQQXXXXXXooWoooooooooTTTTTTTTTToooooooXXXWXXXXXQQQQQQQQQXTFFFFFFFFAAAJQ....",
            "...QQJAAAFFFJFFFoQQXXXXXLLLXXXXXQQQQQQQQXXQQQQQQQQQQQQQXXQQQQQXXkkXXXXXXXXXXXQQbFFFFFFFAAABQQQ..",
            ".QQJAAAAAFFFFFFXQQXXLLLLLXXFAAABXXXXXXXQQQ..QQQQQXXQ..QXkXXXXTJJXXJBFBJXLLXJBBQQXFFFFFFAAAAAJXQ.",
            "QXFAAAAAAFJJFJXQQXXgXLLLXBFFAAFJXXXQQQXXJXQQXooooooXQQXLXQXQXgJJTWXBFABXLXBJXXXQQXJFFFFAAAAAAFQ.",
            "QFAAAAAAAFJJoQQQXkXXkkXLLXXXJBWXXQQQ.QXXJXQQXoooTooQ.QXLXQQ.QQXbTTXXXBXkkXXXXXkXQQQTFFFAAAAAAAJQ",
            "QAAAAFFAFFFXQ..QXXQQXXQXXXXXXXXXXXXXQ.QQTQ..QooTTToQQXQXkXQQQXgbbbbXXXXXXQXQQQXXQ..QXFFFAFFAAAAQ",
            "XAAAFJJJTTJXQ...QQ..QQ.QQQQQQQQQQQXXXQQXJXQ.QoTTTToQQXQXkXXXbXQXXQQQQQQQQ.Q...QQ...QXFTTJJJFAAAX",
            "JAAAJJJJTJJgQ.............QQQXQQ..QQQ.QXQQ..QooTTooQ.QQXXQQQQQ.QQQQQQQ.............QoJJTJJJJAAAJ",
            "FAAFToTJTJJJQ..QQ........QXXXkXXQ......Q.....QQXQQQ.QQQXQ.....QXXXXXXXQ.......QQQ.QXJJJJJJTTFAAF",
            "FAAFTTTJJJJJoQQXXQ........QQQXXXQ..............QQ..QXXXXQ.....QXXXQQQQ......QQXXXQQJFFJJJTTTFAAF",
            "FAAFTTTJJJJFJQQXkXQ..........QQQ..............QXXQ..QQXQ.......QQQ.........QXXkkXQQFFFJJJJTTFAAA",
            "FAAFTTTJJJJFJQQXkkXQ.........................QooooQ...Q....................QXkkkXQXFFFFJJJTTFAAF",
            "FAAFTTTJJJJFFXQXkkXQ.......QQ...............QXoJJoXQ...............Q.......QXkkkXQbFFFFJJJTTFAAF",
            "FAAFTTTJJJJFFoQXkkXQ......QXXQQ.............QoJFFJoQ............QQQXQ......QXkkXQQTFFFJJJJTTFAAF",
            "JAAAJTTJJJJJFJQQXXQQ.......QXXXQQ...........QTFFFFJXQ.Q......QQQXXXQ........QXXXQQJFFFJJJJTJAAAJ",
            "XAAAJTJJJJJFFJQQXXQXQ.......QQXXXQ.QQQQ.Q..QoJFFFFFoQQXQ.QQQQXXXQQQ........QXQbXQXFFFFJJJJTJAAAX",
            "XAAAFTJJJTJFFJXQQbXXQ.........QXQ.QXXXXQXQ.QTJFFFFFTQ.Q.QXXXXkXQ..........QQXXbQQoFFFFTJJJTJAAAX",
            "QFAAFTJJTTJFFJTQXbXkXQQQ.......Q..QXXXXXXQQXJJFFFFFJoQ.QXLLLLLXQ........QQXXXbbQQJFFFFJJJJTFAAAQ",
            "QJAAAJJTTTJFJJJQQXkkkXXXQ........QXXJFBXXQXoJJFFFFFJTXQXXLLLXXQ........QXXkkXbbQQJFFFFTTTJJFAAFQ",
            "QXAAAJJJTTJFJJJoQXXXkkkkXQ........QQXBBXXXXJJJFFFFFJJXXQQXLXQQ........QXkkkXbbXQoJFFFFTTTJJAAAJQ",
            "QXAAAFJJTJJFJJJTQQbbXikkkXQ........QXXXXQXoJJFFFFFFJJoXXXXXXQ........QXkkkkXbbQQJJFFFFJTTJJAAAXQ",
            ".QFAAFJJTTJFFJJJXQXXikkkkXQ.........QQQQXXTJJFFFFFFFJJXkXQQQ........QXkkkkkkXXQXJJFFFFJTTJFAAAXQ",
            ".QiAAAJJTTFFJJJJTQQXkXkkkkXQ........Q.QXXJJJJFFFFFFFFFJXkXQQ........QXkkkkkkXkXJJJFFFFJXJJFAAwQ.",
            ".QXAAAJJToJFJJJJJJXXXQXXXXXQ......QQXQQXoJJJJFFFFFFFFFFTXQXXQQ......QXXXXXXXQXJJJFFFFFTXJJAAAlQ.",
            ".QkAAAFJJTJFJFJJJJJGXQXXXXXQ.QQQQQXXQQXdTJJJJFFFFFFFFFFJTdQQXXQQQQQQXXXXXXkwTJJJJFFFFJTJJJAAAdQ.",
            "..QFAAFTJJTFJFJJJJJJJJTdXkXXQXXXXQQXXdiGJJJFFFFFFFFFFFJJGGikXQQXXXXXXXidGJJFFJJJFFFFFJJJTFAAAXQ.",
            "..QiAAFJJJJFJFJJJJJJJJJJJJTTToddTGGwGGdJJJJFFFFFFFFFFFJJJiiGGddddoTTTJJJJFFFJJJJJFFFFJJJJFAAFQ..",
            "..QlAAAJJJJFJFJJJJJJJJJJJJJJJJJJTTJJwdGJJJJFFFFFFFFFFFJFJGGTJTJJJJJJJJJJJFFFJFFJJFFFFJJJJFAAiQ..",
            "..QdAAAFJJJFJFJJJJJJJJJJJJJJJJJJJJJJGiJJJJFFFFFFFFFFFFFJFJGwJJJJJJJJJJJJFFFFFFJJJFFFFJJFJAAAlQ..",
            "...QFAAFJJJFFFJJJJJJJJJJJJJJJJJJJJJJJGJJJFFFFFFFFFFFFFFFFJTTJJJJJJJJJJFFFFFJJFJFFFFFFFJJFAAAdQ..",
            "...QiAAAJJJFFJFJJJJJJJJJJJJJJFJJJJJJJTJJJFFFFFFFFFFFFFFFFFTJJJJFFFFFJFFFFJFFJJFJFFFFFJJJFAAFQ...",
            "...QGAAAFJJFFJFJJJJJJJJJJJJFJJJJJJJJJJJFFFFFFFFFFFFFFFFFFFJJJJJFFFFFFFFFFFFFFJJJFFFFFJJJAAAiQ...",
            "....QFAAFJJJFJJJJJJJJJJJJJJJJJJJJJJJJJJFFJFFFFFFFFFFFFFFFJJJJJJJFFFJFFFJFFJJFFFJFFFFFJJFAAAwQ...",
            "....QlAAAFJFFFJJJJJJJJJJJJJJJJJJJJJJJJJJJJJFFFFFFFFFFJJJFJJJJJJJJFFFFFFFFFFJJJFFFFFFFJFAAAwQ....",
            ".....QFAAAJFFJJJJJJJJJJJJJJJJFFJJJJJJJJpppppJFFFFFFJppppTJJJJJJJFFFFFFJJJJJJJJJFFFFFFJFAAAdQ....",
            ".....QlAAAFJFJJJJJJJJJJJJJJJJJJJJJJJJJJJpppppJJJJJJpppppJJJJJJJJJFFJFFFJJJJJJJJJFFFFFFAAAiQ.....",
            "......QFAAAFFJJJJJJJJJJJJJJJJJJJJJJJJJpppppppppppppppppppJJJJJJJJJFFFFFJJJJJJJJJJJJFFAAAFXQ.....",
            "......QdFAAAFFJJJJJJJJJJJJJJJJJJTJTGipppppppXpppppppXpppppppTJJJJJFFFFFJJJJJJJJJJJFFAAAAdQ......",
            ".......QlAAAFFJJTJJJJJJJJJJJJJJTTiGwdGddXXXXXXXddXXXXddddddGGdiGTJJFFFJJJJJJJJJJJJFFAAAlQ.......",
            ".......QdAAAFJJJTTJJJJJJJJJJJJTddGdddddXXXXXXXdXXdXXXdddddddGddddTJFFJJJJJJJJJJJJJFFAAAdQ.......",
            "........QFAAFFJJTTTTJJJJJJJJJJdXXdXXXXXdXXdXXXdXXdXXXXdXXXdwdXddidJJJFJJJJJJJJJJJJFAAAAQ........",
            "........QiAAAFJJJJTTTJJJJJJJJTiXXXidddiddXXXddiXXdXdXXiXddiddXidXdTJJJJJJJJJTTJJJFFAAAwQ........",
            "........QiAAAFJJTJTwGJJJJJJTTTXXXXdXdddddiXXdXdXdXdXXdXddiidddXdXdTTJJJJJJJTTTJJJJFAAAlQ........",
            ".........QAAAFJiTJTTwJJJJJJdwddXwdXXwdddwdddwdddwdddwdddwTddwXXXXXddTJJJJJJTJJTJiFFAAAdQ........",
            ".........QiAAAFilTTTTJTTJJJwiddXdddXdidTTTTTTTJJTJJJTTTTTTGwiXXdXXdGdJJJJJJTJJTliFAAAiQ.........",
            ".........QdAAAFJlTJTGTJJTJTdTXXdiddXdXdXXdddGTTTTTTGTdXdiddXXdddXXXTTJJJJJJTGJTllFAAAGQ.........",
            "..........QFAAAFlTTJTJTTJJJTGddXXXdTddoGGGdddddddidwddiGGTooTdXXXXXwTJJJJJJTTTlliAAAFQ..........",
            "..........QGAAAFllTJTTJJTJJTddXXddTTTJTTJJJJJGGGGGiiiJJJJJJJJTdddXddJJJJJJJTTTiiFAAAlQ..........",
            "...........QFAAAFiTTTiTJJTGTddddTTJTJJJJJJJJJJJJJJJJJJJJJJJJJJJTdXddTTJJTTTJTliiAAAFQ...........",
            "...........QGAAAFiliTTJJJTGGddXdTJTTJJJJJJJJJJJJJJJJJJJJJJJJJJJTdXddTTJJJTTJlliFAAAlQ...........",
            "............QFAAAFliTTGTJTTdGdddTTJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJGdXTTTTJJTdGilFAAAFQ............",
            "............QdAAAFliTdTTJTTdXdwdTTJJJJTTTTTTTTTTTTTTTTJJJJJJJJJTddXdGTJFJJwTilFAAAlQ............",
            ".............QFAAAFiliTTTGdwXdddTTJJTTTTTddwddddddddTTTTTTTJTJTTdddXiTTlJlTliFAAAFQ.............",
            ".............QdAAAFiiTTdGddGiGGTTTTJTTTTddddidXdddddiddGTJTJTJTTGGddiGGGdlTTiFAAAdQ.............",
            "..............QiAAAFTTldddXTdGiTdiTTTTTTddddXXXXXXXwddddTJTTTTTTTTGXdddddllJFAAAwQ..............",
            "..............QdFAAAiTlddddXGlGlddddGTTdddGdXXXXXXXdddddTTTGdddGlTGGXXlGdlGiAAAAdQ..............",
            "...............QiAAAFldldGdXdlGdXddddTTdTTTwXXXXXXXXdTTGTddTddddilGddiddGdiFAAAlQ...............",
            "................QiAAAFiGGdiGdlidddiddGTTJTidddidXXiddTTJTTidddXXGllddXidGGFAAAFQ................",
            "................QdFAAAildiidGlddXddwdTdTTTTTdGdddddwTTTTGiTdXddddGlwlGGGliAAAAdQ................",
            ".................QdAAAAlwGlGldGdwdXXwddTTTTTTTddwddTTTTTTdddwdddwdGGllGdlAAAAlQ.................",
            "..................QiFAAFldlwldXddddddiTJJTTTiTTTdoJdTTTJTGGwiGddddllGiGGFAAAiQ..................",
            "...................QwAAAFdGddddXiGddlTTJTTTTdGTTTTodTTTTTTTJddGdidddlGGFAAAFQ...................",
            "...................QdFAAAFGGdXXdlillJTTGGTTGdTGddidwGTiGGTJTTGGGdXdwdGFAAAFdQ...................",
            "....................QdAAAAwGddddlGlTTJTTddTdGddddddddTddTTJGGllldddddwAAAAGQ....................",
            ".....................QGAAAAFddddGGGTJiTTiGdwddddddddXiGdiTlTllGGddddiAAAAlQ.....................",
            "......................QlAFAAFddXdGGlilGTdTddddXXXddXddTdTTlllddGXXdwAAAAlQ......................",
            ".......................QlFFAAwXddliwlGGGGiddXXXXXXXXddGddilGGGGdddiAAAAiQ.......................",
            "........................QlAAAAFddXwGlGllddwXXXXdXXXXXdwdGGwlldwddFAAAAiQ........................",
            ".........................QlAAAAFGddGilldddXwXdddddddXidddllwGddGFAAAAiQ.........................",
            "..........................QGAAAAAiddiXdddddXiddXddddXddGddddXdiAAAAAlQ..........................",
            "...........................QdFAAAAFlddddddXXddiddiXwdXdGdXXXlFAAAAFwQ...........................",
            "............................QdwAAAAAFGdXXddXdddddddddddddXGFAAAAAFdQ............................",
            ".............................QQlFAAAAAFlXddwddddiddddiddlFAAAAAAlQQ.............................",
            "...............................QdiAAAAAAFFiGXQidXdidGiFAAAAAAAwdQ...............................",
            "................................QQdwAAAAAAAAAFFFFFFAAAAAAAAAFdQQ................................",
            "..................................QQwwAAAAAAAAAAAAAAAAAAAAFGQQ..................................",
            "....................................QQdlwFAAAAAAAAAAAAAFldQQ....................................",
            "......................................QQQQdliwFFFFwildQQQQ......................................"
    };

    public static Block blockAt(int row, int col) {
        return switch (ROWS[row].charAt(col)) {
            case 'Q' -> Blocks.SMOOTH_QUARTZ;
            case 'O' -> Blocks.BONE_BLOCK;
            case 'N' -> Blocks.SAND;
            case 'A' -> Blocks.SMOOTH_SANDSTONE;
            case 'B' -> Blocks.BIRCH_PLANKS;
            case 'F' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE);
            case 'T' -> Blocks.TERRACOTTA;
            case 'o' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE);
            case 'J' -> Blocks.JUNGLE_PLANKS;
            case 'r' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.RED);
            case 'b' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN);
            case 'g' -> Blocks.GRANITE;
            case 'X' -> Blocks.CONCRETE.pick(DyeColor.BLACK);
            case 'V' -> Blocks.OBSIDIAN;
            case 'k' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK);
            case 'G' -> Blocks.CONCRETE.pick(DyeColor.GRAY);
            case 'L' -> Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY);
            case 'W' -> Blocks.CONCRETE.pick(DyeColor.WHITE);
            case 'S' -> Blocks.SPRUCE_PLANKS;
            case 'D' -> Blocks.DARK_OAK_PLANKS;
            case 'd' -> Blocks.DEEPSLATE;
            case 'l' -> Blocks.WOOL.pick(DyeColor.LIGHT_GRAY);
            case 'i' -> Blocks.DIORITE;
            case 'w' -> Blocks.WOOL.pick(DyeColor.WHITE);
            case 'c' -> Blocks.CALCITE;
            case 'p' -> Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK);
            default -> Blocks.AIR;
        };
    }

    public static String blockId(int row, int col) {
        return switch (ROWS[row].charAt(col)) {
            case 'Q' -> "smooth_quartz";
            case 'O' -> "bone_block";
            case 'N' -> "sand";
            case 'A' -> "smooth_sandstone";
            case 'B' -> "birch_planks";
            case 'F' -> "white_terracotta";
            case 'T' -> "terracotta";
            case 'o' -> "orange_terracotta";
            case 'J' -> "jungle_planks";
            case 'r' -> "red_terracotta";
            case 'b' -> "brown_terracotta";
            case 'g' -> "granite";
            case 'X' -> "black_concrete";
            case 'V' -> "obsidian";
            case 'k' -> "black_terracotta";
            case 'G' -> "gray_concrete";
            case 'L' -> "light_gray_concrete";
            case 'W' -> "white_concrete";
            case 'S' -> "spruce_planks";
            case 'D' -> "dark_oak_planks";
            case 'd' -> "deepslate";
            case 'l' -> "light_gray_wool";
            case 'i' -> "diorite";
            case 'w' -> "white_wool";
            case 'c' -> "calcite";
            case 'p' -> "pink_terracotta";
            default -> "air";
        };
    }
}
