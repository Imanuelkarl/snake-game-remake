package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Bitmap Font that gives full control over rendering
 */
public class CustomBitmapFont implements Disposable {

    // Character glyph data
    public static class Glyph {
        public char character;
        public int id;
        public int x, y;           // Position in texture
        public int width, height;   // Size of character
        public int xoffset, yoffset; // Offset when drawing
        public int xadvance;         // How much to advance after drawing

        // Texture region for this glyph
        public TextureRegion region;

        public Glyph() {}

        @Override
        public String toString() {
            return String.format("Glyph[%c]: pos=(%d,%d) size=(%dx%d) advance=%d",
                character, x, y, width, height, xadvance);
        }
    }

    private Texture texture;
    private Map<Integer, Glyph> glyphs = new HashMap<>();
    private int lineHeight;
    private int base;
    private int scaleW, scaleH;
    private String fontName;

    // Debug information
    private boolean debugMode = false;

    /**
     * Load font from .fnt file and texture
     */
    public CustomBitmapFont(FileHandle fontFile, FileHandle textureFile) {
        loadFont(fontFile, textureFile);
    }

    /**
     * Load font from .fnt file and existing texture
     */
    public CustomBitmapFont(FileHandle fontFile, Texture texture) {
        this.texture = texture;
        loadFontData(fontFile);
        createTextureRegions();
    }

    private void loadFont(FileHandle fontFile, FileHandle textureFile) {
        // Load texture
        texture = new Texture(textureFile);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Load font data
        loadFontData(fontFile);

        // Create texture regions
        createTextureRegions();
    }

    private void loadFontData(FileHandle fontFile) {
        Gdx.app.log("CustomFont", "Loading font from: " + fontFile.path());

        String content = fontFile.readString();
        String[] lines = content.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("info")) {
                parseInfo(line);
            } else if (line.startsWith("common")) {
                parseCommon(line);
            } else if (line.startsWith("char")) {
                parseChar(line);
            }
        }

        Gdx.app.log("CustomFont", "Loaded " + glyphs.size() + " glyphs");
    }

    private void parseInfo(String line) {
        // Parse: info face="CustomFont" size=64 bold=0 ...
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.startsWith("face=")) {
                fontName = part.substring(6, part.length() - 1);
            }
        }
    }

    private void parseCommon(String line) {
        // Parse: common lineHeight=64 base=52 scaleW=1664 scaleH=256 ...
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.startsWith("lineHeight=")) {
                lineHeight = Integer.parseInt(part.substring(11));
            } else if (part.startsWith("base=")) {
                base = Integer.parseInt(part.substring(5));
            } else if (part.startsWith("scaleW=")) {
                scaleW = Integer.parseInt(part.substring(7));
            } else if (part.startsWith("scaleH=")) {
                scaleH = Integer.parseInt(part.substring(7));
            }
        }

        Gdx.app.log("CustomFont", String.format("Font metrics: lineHeight=%d, base=%d, texture=%dx%d",
            lineHeight, base, scaleW, scaleH));
    }

    private void parseChar(String line) {
        // Parse: char id=65 x=0 y=0 width=64 height=64 xoffset=0 yoffset=0 xadvance=64 ...
        Glyph glyph = new Glyph();

        String[] parts = line.split(" ");
        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length != 2) continue;

            String key = kv[0];
            String value = kv[1];

            try {
                switch (key) {
                    case "id":
                        glyph.id = Integer.parseInt(value);
                        glyph.character = (char) glyph.id;
                        break;
                    case "x":
                        glyph.x = Integer.parseInt(value);
                        break;
                    case "y":
                        glyph.y = Integer.parseInt(value);
                        break;
                    case "width":
                        glyph.width = Integer.parseInt(value);
                        break;
                    case "height":
                        glyph.height = Integer.parseInt(value);
                        break;
                    case "xoffset":
                        glyph.xoffset = Integer.parseInt(value);
                        break;
                    case "yoffset":
                        glyph.yoffset = Integer.parseInt(value);
                        break;
                    case "xadvance":
                        glyph.xadvance = Integer.parseInt(value);
                        break;
                }
            } catch (NumberFormatException e) {
                Gdx.app.error("CustomFont", "Failed to parse: " + part);
            }
        }

        glyphs.put(glyph.id, glyph);

        if (debugMode) {
            Gdx.app.log("CustomFont", "Parsed: " + glyph);
        }
    }

    private void createTextureRegions() {
        for (Glyph glyph : glyphs.values()) {
            // Create texture region for this glyph
            glyph.region = new TextureRegion(texture, glyph.x, glyph.y, glyph.width, glyph.height);
        }
    }

    /**
     * Draw text at specified position
     */
    public void draw(Batch batch, CharSequence text, float x, float y) {
        draw(batch, text, x, y, Color.WHITE);
    }

    /**
     * Draw text with color
     */
    public void draw(Batch batch, CharSequence text, float x, float y, Color color) {
        float currentX = x;
        float currentY = y;

        // Draw debug background if needed
        if (debugMode) {
            drawDebugBackground(batch, text, x, y);
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            // Handle newlines
            if (c == '\n') {
                currentX = x;
                currentY -= lineHeight;
                continue;
            }

            Glyph glyph = glyphs.get((int) c);

            if (glyph == null) {
                // Character not found - draw placeholder
                if (debugMode) {
                    drawMissingChar(batch, currentX, currentY, c);
                }
                currentX += 20; // Default advance for missing char
                continue;
            }

            // Calculate position with offsets
            float drawX = currentX + glyph.xoffset;
            float drawY = currentY + glyph.yoffset - (glyph.height - base);

            // Draw the character
            batch.setColor(color);
            batch.draw(glyph.region, drawX, drawY);

            // Draw debug box around character
            if (debugMode) {
                drawDebugBox(batch, drawX, drawY, glyph.width, glyph.height, c);
            }

            // Advance cursor
            currentX += glyph.xadvance;
        }
    }

    /**
     * Draw text with width constraints (simple word wrapping)
     */
    public void drawWrapped(Batch batch, CharSequence text, float x, float y, float maxWidth) {
        String[] words = text.toString().split(" ");
        float currentX = x;
        float currentY = y;

        for (String word : words) {
            float wordWidth = getWidth(word);

            if (currentX + wordWidth > x + maxWidth) {
                currentX = x;
                currentY -= lineHeight;
            }

            draw(batch, word, currentX, currentY);
            currentX += getWidth(word) + getWidth(" ");
        }
    }

    /**
     * Get the width of a text string
     */
    public float getWidth(CharSequence text) {
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Glyph glyph = glyphs.get((int) c);
            if (glyph != null) {
                width += glyph.xadvance;
            } else {
                width += 20; // Default advance for missing char
            }
        }
        return width;
    }

    /**
     * Get the height of the font
     */
    public float getHeight() {
        return lineHeight;
    }

    /**
     * Get the baseline position
     */
    public float getBaseline() {
        return base;
    }

    /**
     * Check if a character exists in the font
     */
    public boolean hasCharacter(char c) {
        return glyphs.containsKey((int) c);
    }

    /**
     * Get all missing characters in a text string
     */
    public String getMissingCharacters(CharSequence text) {
        StringBuilder missing = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!glyphs.containsKey((int) c) && missing.indexOf(String.valueOf(c)) == -1) {
                missing.append(c);
            }
        }
        return missing.toString();
    }

    /**
     * Debug methods
     */
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    private void drawDebugBackground(Batch batch, CharSequence text, float x, float y) {
        float width = getWidth(text);
        float height = lineHeight;

        // Draw semi-transparent background
        batch.setColor(0, 0, 0, 0.3f);
        batch.draw(getWhitePixel(), x, y - height, width, height);
        batch.setColor(Color.WHITE);

        // Draw baseline
        batch.setColor(Color.RED);
        batch.draw(getWhitePixel(), x, y - base, width, 1);
        batch.setColor(Color.WHITE);
    }

    private void drawDebugBox(Batch batch, float x, float y, float width, float height, char c) {
        // Draw outline
        batch.setColor(Color.YELLOW);
        batch.draw(getWhitePixel(), x, y, width, 1); // Top
        batch.draw(getWhitePixel(), x, y, 1, height); // Left
        batch.draw(getWhitePixel(), x + width, y, 1, height); // Right
        batch.draw(getWhitePixel(), x, y + height, width, 1); // Bottom
        batch.setColor(Color.WHITE);

        // Draw character info
        if (debugMode) {
            // In a real implementation, you'd need a small font to draw this
            // For now, just log it
            Gdx.app.log("CustomFont", "Drawing char: " + c + " at (" + x + "," + y + ") size=" + width + "x" + height);
        }
    }

    private void drawMissingChar(Batch batch, float x, float y, char c) {
        batch.setColor(Color.RED);
        // Draw a red rectangle for missing character
        batch.draw(getWhitePixel(), x, y - lineHeight + 10, 20, lineHeight - 10);

        // Draw question mark (simplified)
        batch.draw(getWhitePixel(), x + 8, y - lineHeight + 15, 4, 15);
        batch.setColor(Color.WHITE);

        Gdx.app.log("CustomFont", "Missing character: " + c + " (ID: " + (int)c + ")");
    }

    private static Texture whitePixel;
    private static Texture getWhitePixel() {
        if (whitePixel == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            whitePixel = new Texture(pixmap);
            pixmap.dispose();
        }
        return whitePixel;
    }

    /**
     * Print all loaded glyphs for debugging
     */
    public void printGlyphs() {
        Gdx.app.log("CustomFont", "=== Loaded Glyphs ===");
        for (Glyph glyph : glyphs.values()) {
            Gdx.app.log("CustomFont", glyph.toString());
        }
        Gdx.app.log("CustomFont", "Total: " + glyphs.size() + " glyphs");
    }

    /**
     * Test all characters in a range
     */
    public void testCharacterRange(int start, int end) {
        StringBuilder found = new StringBuilder();
        StringBuilder missing = new StringBuilder();

        for (int i = start; i <= end; i++) {
            if (glyphs.containsKey(i)) {
                found.append((char) i);
            } else {
                missing.append((char) i);
            }
        }

        Gdx.app.log("CustomFont", "Characters " + start + "-" + end + ":");
        Gdx.app.log("CustomFont", "  Found: " + found.length() + " chars");
        Gdx.app.log("CustomFont", "  Missing: " + missing.length() + " chars");
        if (missing.length() > 0 && missing.length() < 50) {
            Gdx.app.log("CustomFont", "  Missing chars: " + missing.toString());
        }
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        glyphs.clear();
    }

    // Getters
    public Texture getTexture() { return texture; }
    public Map<Integer, Glyph> getGlyphs() { return glyphs; }
    public int getLineHeight() { return lineHeight; }
    public int getBase() { return base; }
}
