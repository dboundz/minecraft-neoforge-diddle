#!/usr/bin/env python3
"""Convert the sunglasses portrait into a 96x128 Minecraft block grid."""

from __future__ import annotations

import csv
import math
from collections import Counter
from pathlib import Path

import colorsys

from PIL import Image, ImageDraw, ImageFilter

WIDTH = 96
HEIGHT = 128
ROOT = Path(__file__).resolve().parents[1]
SOURCE = Path(
    "/Users/darren/.cursor/projects/Users-darren-Development-spawn-portrait/assets/"
    "diddle-d0124835-83ea-4852-add4-bf900357bcac.png"
)
JAVA_OUT = ROOT / "src/main/java/com/spawnportrait/PortraitPattern.java"
CSV_OUT = ROOT / "docs/portrait-coordinates.csv"
PNG_OUT = ROOT / "docs/portrait-preview.png"
GRID_OUT = ROOT / "docs/portrait-grid.txt"

# glyph -> (java expression, csv id, preview RGB)
PALETTE: dict[str, tuple[str, str, tuple[int, int, int]]] = {
    ".": ("Blocks.AIR", "air", (0, 0, 0)),
    "Q": ("Blocks.SMOOTH_QUARTZ", "smooth_quartz", (235, 229, 222)),
    "O": ("Blocks.BONE_BLOCK", "bone_block", (229, 224, 197)),
    "N": ("Blocks.SAND", "sand", (219, 207, 163)),
    "A": ("Blocks.SMOOTH_SANDSTONE", "smooth_sandstone", (224, 214, 170)),
    "B": ("Blocks.BIRCH_PLANKS", "birch_planks", (192, 175, 121)),
    "F": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.WHITE)", "white_terracotta", (209, 176, 161)),
    "T": ("Blocks.TERRACOTTA", "terracotta", (152, 94, 67)),
    "o": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.ORANGE)", "orange_terracotta", (161, 83, 37)),
    "J": ("Blocks.JUNGLE_PLANKS", "jungle_planks", (160, 115, 80)),
    "r": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.RED)", "red_terracotta", (143, 61, 46)),
    "b": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.BROWN)", "brown_terracotta", (77, 51, 35)),
    "g": ("Blocks.GRANITE", "granite", (149, 103, 86)),
    "X": ("Blocks.CONCRETE.pick(DyeColor.BLACK)", "black_concrete", (8, 10, 15)),
    "V": ("Blocks.OBSIDIAN", "obsidian", (15, 10, 24)),
    "k": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.BLACK)", "black_terracotta", (37, 22, 16)),
    "G": ("Blocks.CONCRETE.pick(DyeColor.GRAY)", "gray_concrete", (54, 57, 61)),
    "L": ("Blocks.CONCRETE.pick(DyeColor.LIGHT_GRAY)", "light_gray_concrete", (125, 125, 115)),
    "W": ("Blocks.CONCRETE.pick(DyeColor.WHITE)", "white_concrete", (207, 213, 214)),
    "S": ("Blocks.SPRUCE_PLANKS", "spruce_planks", (114, 84, 48)),
    "D": ("Blocks.DARK_OAK_PLANKS", "dark_oak_planks", (66, 43, 20)),
    "d": ("Blocks.DEEPSLATE", "deepslate", (80, 80, 82)),
    "l": ("Blocks.WOOL.pick(DyeColor.LIGHT_GRAY)", "light_gray_wool", (142, 142, 134)),
    "i": ("Blocks.DIORITE", "diorite", (188, 188, 188)),
    "w": ("Blocks.WOOL.pick(DyeColor.WHITE)", "white_wool", (233, 236, 236)),
    "c": ("Blocks.CALCITE", "calcite", (223, 224, 220)),
    "p": ("Blocks.DYED_TERRACOTTA.pick(DyeColor.PINK)", "pink_terracotta", (161, 78, 78)),
}

SKIN = set("NAFBToJrgb")
BEARD_DARK = set("SDdkG")
BEARD_SALT = set("liwcL")
FRAME = set("XV")
LENS = set("kGLW")


def dist(a: tuple[int, int, int], b: tuple[int, int, int]) -> float:
    return math.sqrt(sum((x - y) ** 2 for x, y in zip(a, b)))


def is_backdrop(r: int, g: int, b: int) -> bool:
    return r < 28 and g < 28 and b < 32


def hls(r: int, g: int, b: int) -> tuple[float, float, float]:
    hue, light, sat = colorsys.rgb_to_hls(r / 255, g / 255, b / 255)
    return hue * 360, light, sat


def is_warm_skin(r: int, g: int, b: int) -> bool:
    hue, light, sat = hls(r, g, b)
    return 6 < hue < 45 and sat > 0.32 and light > 0.40


def skin_glyph(r: int, g: int, b: int) -> str:
    brightness = (r + g + b) / 3
    if brightness > 205:
        return "A"
    if brightness > 175:
        return "F"
    if brightness > 145:
        return "F" if r > 160 else "B"
    if brightness > 110:
        return "J"
    if brightness > 85:
        return "T"
    if r > g + 18:
        return "o"
    if brightness > 72:
        return "g"
    return "b"


def beard_glyph(r: int, g: int, b: int) -> str:
    """Salt-and-pepper: white/gray salt, dark gray/black pepper. Not skin tan."""
    brightness = (r + g + b) / 3
    _, light, sat = hls(r, g, b)
    if light > 0.62 or brightness > 190:
        return "w"
    if light > 0.50 or brightness > 155:
        return "i"
    if sat < 0.28 and light > 0.38:
        return "l"
    if brightness > 115:
        return "L"
    if brightness > 80:
        return "G"
    if brightness > 50:
        return "d"
    return "X"


def classify(r: int, g: int, b: int, row: int, col: int) -> str:
    if is_backdrop(r, g, b):
        return "."
    brightness = (r + g + b) / 3
    # Sunglasses / near-black
    if r < 38 and g < 38 and b < 45:
        return "X" if row < int(HEIGHT * 0.70) else "d"
    if r < 58 and g < 52 and b < 58:
        return "k" if 0.28 * HEIGHT < row < 0.60 * HEIGHT else "d"
    # Lens reflections (cool gray in the glasses band)
    if 0.30 * HEIGHT < row < 0.56 * HEIGHT and brightness < 130 and abs(r - b) < 22:
        return "G" if brightness < 75 else "L"
    # Lips: only a thin mouth band
    if 0.62 * HEIGHT < row < 0.68 * HEIGHT and WIDTH * 0.38 < col < WIDTH * 0.62:
        if r > g + 18 and 80 < r < 180 and g < 120:
            return "p"
    # Below the glasses: warm peach/tan is skin (same as forehead).
    # Everything else in that region is salt-and-pepper beard.
    if row > HEIGHT * 0.55:
        hue, light, sat = hls(r, g, b)
        in_mustache = (
            HEIGHT * 0.58 < row < HEIGHT * 0.72
            and WIDTH * 0.34 < col < WIDTH * 0.66
        )
        # Mustache and dense beard stay hair even when the photo is still warm.
        if in_mustache and light < 0.46:
            return beard_glyph(r, g, b)
        if is_warm_skin(r, g, b) or (6 < hue < 45 and sat > 0.28 and light > 0.36):
            return skin_glyph(r, g, b)
        return beard_glyph(r, g, b)
    return skin_glyph(r, g, b)


def nearest_palette(r: int, g: int, b: int) -> str:
    best = "."
    best_d = 1e9
    for glyph, (_, _, rgb) in PALETTE.items():
        if glyph == ".":
            continue
        d = dist((r, g, b), rgb)
        if d < best_d:
            best_d = d
            best = glyph
    return best


def load_scaled() -> Image.Image:
    image = Image.open(SOURCE).convert("RGBA").filter(ImageFilter.SMOOTH)
    return image.resize((WIDTH, HEIGHT), Image.Resampling.BOX)


def build_grid(scaled: Image.Image) -> list[list[str]]:
    grid = []
    for row in range(HEIGHT):
        line = []
        for col in range(WIDTH):
            r, g, b, a = scaled.getpixel((col, row))
            if a < 20 or is_backdrop(r, g, b):
                line.append(".")
            else:
                mapped = classify(r, g, b, row, col)
                # If classify is unsure-looking, snap to nearest palette color.
                if mapped == ".":
                    mapped = nearest_palette(r, g, b)
                line.append(mapped)
        grid.append(line)
    return grid


def subject_mask(grid: list[list[str]]) -> list[list[bool]]:
    return [[cell != "." for cell in row] for row in grid]


def tune_outline(grid: list[list[str]]) -> None:
    mask = subject_mask(grid)
    for row in range(HEIGHT):
        for col in range(WIDTH):
            if mask[row][col]:
                continue
            cardinal = 0
            for dy, dx in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                rr, cc = row + dy, col + dx
                if 0 <= rr < HEIGHT and 0 <= cc < WIDTH and mask[rr][cc]:
                    cardinal += 1
            if cardinal >= 1:
                grid[row][col] = "Q"


def tune_sunglasses(grid: list[list[str]]) -> None:
    """Sharpen real dark pixels into frames; keep existing reflections."""
    top = int(HEIGHT * 0.32)
    bottom = int(HEIGHT * 0.58)
    mid = WIDTH // 2
    dark_cells = [
        (row, col)
        for row in range(top, bottom)
        for col in range(WIDTH)
        if grid[row][col] in FRAME | LENS | set("Dd")
    ]
    if not dark_cells:
        return
    min_c = min(col for _, col in dark_cells)
    max_c = max(col for _, col in dark_cells)
    # Outline the actual glasses silhouette
    dark = set(dark_cells)
    for row, col in dark_cells:
        if grid[row][col] in "QO.":
            continue
        edge = False
        for dy, dx in ((-1, 0), (1, 0), (0, -1), (0, 1)):
            if (row + dy, col + dx) not in dark:
                edge = True
                break
        if edge or abs(col - mid) <= 1:
            grid[row][col] = "X"
        elif grid[row][col] not in "WL":
            grid[row][col] = "k"
    # Double bridge over the nose
    for col in range(mid - 3, mid + 4):
        for row in (top + 5, top + 7):
            if grid[row][col] not in ".QO":
                grid[row][col] = "X"
    # Hinge glints near the temples
    for row in range(top + 8, top + 12):
        if grid[row][min_c + 2] not in ".QO":
            grid[row][min_c + 2] = "L"
        if grid[row][max_c - 2] not in ".QO":
            grid[row][max_c - 2] = "L"
    # A few white reflection pixels inside each lens
    for row, col in (
        (top + 10, mid - 16),
        (top + 11, mid - 15),
        (top + 14, mid - 18),
        (top + 10, mid + 14),
        (top + 13, mid + 17),
    ):
        if 0 <= row < HEIGHT and 0 <= col < WIDTH and grid[row][col] not in ".QO":
            grid[row][col] = "W"


def tune_beard(grid: list[list[str]]) -> None:
    """Add salt flecks only on hair pixels. Never recolor cheeks."""
    start = int(HEIGHT * 0.55)
    for row in range(start, HEIGHT):
        for col in range(WIDTH):
            glyph = grid[row][col]
            if glyph in ".QO" or glyph in SKIN:
                continue
            speck = ((row * 47) ^ (col * 113) ^ (row * col)) & 15
            if glyph in BEARD_DARK and speck in (1, 6):
                grid[row][col] = "i"
            elif glyph in BEARD_DARK and speck == 3:
                grid[row][col] = "w"


def write_png(grid: list[list[str]]) -> None:
    scale = 8
    image = Image.new("RGB", (WIDTH * scale, HEIGHT * scale), (12, 12, 14))
    draw = ImageDraw.Draw(image)
    for row, line in enumerate(grid):
        for col, glyph in enumerate(line):
            color = PALETTE[glyph][2]
            if glyph == ".":
                continue
            x0, y0 = col * scale, row * scale
            draw.rectangle((x0, y0, x0 + scale - 1, y0 + scale - 1), fill=color)
    PNG_OUT.parent.mkdir(parents=True, exist_ok=True)
    image.save(PNG_OUT)


def write_csv(grid: list[list[str]]) -> int:
    CSV_OUT.parent.mkdir(parents=True, exist_ok=True)
    count = 0
    with CSV_OUT.open("w", newline="") as handle:
        writer = csv.writer(handle)
        writer.writerow(["x", "y", "block"])
        for y, line in enumerate(grid):
            for x, glyph in enumerate(line):
                if glyph == ".":
                    continue
                writer.writerow([x, y, PALETTE[glyph][1]])
                count += 1
    return count


def write_java(grid: list[list[str]]) -> None:
    rows = ["".join(line) for line in grid]
    quoted = ",\n".join(f'            "{row}"' for row in rows)
    cases = []
    id_cases = []
    for glyph, (expr, csv_id, _rgb) in PALETTE.items():
        if glyph == ".":
            continue
        cases.append(f"            case '{glyph}' -> {expr};")
        id_cases.append(f'            case \'{glyph}\' -> "{csv_id}";')
    case_block = "\n".join(cases)
    id_block = "\n".join(id_cases)
    JAVA_OUT.write_text(
        f"""package com.spawnportrait;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * 96x128 block-pixel portrait.
 * Row 0 is the top of the mural; column 0 is the viewer's left.
 * Generated from docs/portrait-coordinates.csv — do not hand-edit both.
 */
public final class PortraitPattern {{
    private PortraitPattern() {{
    }}

    public static final int WIDTH = {WIDTH};
    public static final int HEIGHT = {HEIGHT};

    private static final String[] ROWS = {{
{quoted}
    }};

    public static Block blockAt(int row, int col) {{
        return switch (ROWS[row].charAt(col)) {{
{case_block}
            default -> Blocks.AIR;
        }};
    }}

    public static String blockId(int row, int col) {{
        return switch (ROWS[row].charAt(col)) {{
{id_block}
            default -> "air";
        }};
    }}
}}
"""
    )


def write_grid_text(grid: list[list[str]]) -> None:
    GRID_OUT.write_text("\n".join("".join(row) for row in grid) + "\n")


def main() -> None:
    scaled = load_scaled()
    grid = build_grid(scaled)
    tune_outline(grid)
    tune_sunglasses(grid)
    tune_beard(grid)
    write_png(grid)
    placed = write_csv(grid)
    write_java(grid)
    write_grid_text(grid)
    counts = Counter(glyph for row in grid for glyph in row if glyph != ".")
    print(f"Wrote {PNG_OUT}")
    print(f"Wrote {CSV_OUT} ({placed} blocks)")
    print(f"Wrote {JAVA_OUT}")
    print("Block counts:")
    for glyph, n in counts.most_common():
        print(f"  {PALETTE[glyph][1]:20s} {n:5d}")


if __name__ == "__main__":
    main()
