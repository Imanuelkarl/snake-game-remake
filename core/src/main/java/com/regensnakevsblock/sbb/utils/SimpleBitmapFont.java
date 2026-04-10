package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * A clean, reliable bitmap font implementation that works with your custom font textures.
 * Features: scaling, coloring, glyph layout, text wrapping, and performance caching.
 */
public class SimpleBitmapFont implements Disposable {

    // ==================== GLYPH DATA ====================

    /**
     * Represents a single character glyph in the font
     */
    public static class GlyphInfo {
        public char character;
        public int id;

        // Texture coordinates (pixels in source image)
        public int srcX, srcY;
        public int width, height;

        // Rendering offsets and advance
        public int xOffset, yOffset;
        public int xAdvance;

        // Texture page index (for multi-page fonts, always 0 for now)
        public int page;

        // Runtime texture region
        public TextureRegion region;

        // Cached scaled values for performance
        public float scaledWidth, scaledHeight;
        public float scaledXOffset, scaledYOffset;
        public float scaledXAdvance;

        public GlyphInfo() {}

        public void updateScale(float scaleX, float scaleY) {
            scaledWidth = width * scaleX;
            scaledHeight = height * scaleY;
            scaledXOffset = xOffset * scaleX;
            scaledYOffset = yOffset * scaleY;
            scaledXAdvance = xAdvance * scaleX;
        }

        @Override
        public String toString() {
            return String.format("'%c' [%d]: pos=(%d,%d) size=(%dx%d) offset=(%d,%d) advance=%d",
                character, id, srcX, srcY, width, height, xOffset, yOffset, xAdvance);
        }
    }

    /**
     * Result of text layout calculation
     */
    public static class TextLayout {
        public float width;
        public float height;
        public Array<LineInfo> lines = new Array<>();
        public Array<GlyphPosition> glyphPositions = new Array<>();

        public void clear() {
            width = 0;
            height = 0;
            lines.clear();
            glyphPositions.clear();
        }
    }

    public static class LineInfo {
        public String text;
        public float width;
        public float y;
        public Array<GlyphInfo> glyphs = new Array<>();
        public FloatArray xPositions = new FloatArray();

        public void clear() {
            text = null;
            width = 0;
            y = 0;
            glyphs.clear();
            xPositions.clear();
        }
    }

    public static class GlyphPosition {
        public GlyphInfo glyph;
        public float x, y;
        public int index;
    }

    // ==================== FONT PROPERTIES ====================

    private Texture texture;
    private Map<Integer, GlyphInfo> glyphs = new HashMap<>();
    private GlyphInfo missingGlyph;
    private GlyphInfo spaceGlyph; // Explicitly store space glyph

    // Font metrics
    private int lineHeight;
    private int base;
    private String fontName;
    private int spaceWidth = 0; // Default space width in pixels

    // Rendering properties
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private Color tint = new Color(Color.WHITE);
    private boolean integerPositions = true;

    // Cached layout for reuse
    private TextLayout cachedLayout = new TextLayout();
    private StringBuilder tempBuilder = new StringBuilder();

    // Debug options
    private boolean debugMode = false;
    private Color debugColor = new Color(1, 1, 0, 0.5f);

    // Performance cache for frequently used strings
    private ObjectMap<String, TextLayout> layoutCache = new ObjectMap<>();
    private int maxCacheSize = 50;

    // ==================== CONSTRUCTORS ====================

    /**
     * Create font from .fnt file and texture
     */
    public SimpleBitmapFont(FileHandle fontFile, FileHandle textureFile) {
        this.texture = new Texture(textureFile);
        this.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loadFontData(fontFile);
        createTextureRegions();
        updateScaling();
    }

    /**
     * Create font from .fnt file and existing texture
     */
    public SimpleBitmapFont(FileHandle fontFile, Texture texture) {
        this.texture = texture;
        this.texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        loadFontData(fontFile);
        createTextureRegions();
        updateScaling();
    }

    // ==================== FONT LOADING ====================

    @SuppressWarnings("DefaultLocale")
    private void loadFontData(FileHandle fontFile) {
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

        // Set up space character handling
        setupSpaceCharacter();

        // Setup missing glyph fallback
        if (missingGlyph == null && !glyphs.isEmpty()) {
            // Try to find a reasonable fallback (like '?' or first available)
            missingGlyph = glyphs.get((int) '?');
            if (missingGlyph == null) {
                missingGlyph = glyphs.values().iterator().next();
            }
        }

        Gdx.app.log("SimpleFont", String.format("Loaded %d glyphs, lineHeight=%d, base=%d, spaceWidth=%d",
            glyphs.size(), lineHeight, base, spaceWidth));
    }

    private void parseInfo(String line) {
        // Simple parsing - extract name if present
        int nameStart = line.indexOf("face=\"");
        if (nameStart != -1) {
            nameStart += 6;
            int nameEnd = line.indexOf("\"", nameStart);
            if (nameEnd != -1) {
                fontName = line.substring(nameStart, nameEnd);
            }
        }
    }

    private void parseCommon(String line) {
        String[] parts = line.split(" ");
        for (String part : parts) {
            if (part.startsWith("lineHeight=")) {
                lineHeight = Integer.parseInt(part.substring(11));
            } else if (part.startsWith("base=")) {
                base = Integer.parseInt(part.substring(5));
            }
        }
    }

    private void parseChar(String line) {
        GlyphInfo glyph = new GlyphInfo();
        String[] parts = line.split(" ");

        for (String part : parts) {
            String[] kv = part.split("=");
            if (kv.length != 2) continue;

            try {
                switch (kv[0]) {
                    case "id":
                        glyph.id = Integer.parseInt(kv[1]);
                        glyph.character = (char) glyph.id;
                        break;
                    case "x":
                        glyph.srcX = Integer.parseInt(kv[1]);
                        break;
                    case "y":
                        glyph.srcY = Integer.parseInt(kv[1]);
                        break;
                    case "width":
                        glyph.width = Integer.parseInt(kv[1]);
                        break;
                    case "height":
                        glyph.height = Integer.parseInt(kv[1]);
                        break;
                    case "xoffset":
                        glyph.xOffset = Integer.parseInt(kv[1]);
                        break;
                    case "yoffset":
                        glyph.yOffset = Integer.parseInt(kv[1]);
                        break;
                    case "xadvance":
                        glyph.xAdvance = Integer.parseInt(kv[1]);
                        break;
                    case "page":
                        glyph.page = Integer.parseInt(kv[1]);
                        break;
                }
            } catch (NumberFormatException e) {
                // Skip malformed entries
            }
        }

        if (glyph.id > 0) {
            glyphs.put(glyph.id, glyph);
        } else if (glyph.id == -1) {
            missingGlyph = glyph;
        }
    }

    private void setupSpaceCharacter() {
        // Try to get space character (ASCII 32)
        spaceGlyph = glyphs.get(32);

        if (spaceGlyph == null) {
            // If space glyph doesn't exist in the font file, create a virtual one
            spaceGlyph = new GlyphInfo();
            spaceGlyph.id = 32;
            spaceGlyph.character = ' ';
            spaceGlyph.width = 0;
            spaceGlyph.height = 0;
            spaceGlyph.xOffset = 0;
            spaceGlyph.yOffset = 0;

            // Calculate space width based on average character width
            int totalAdvance = 0;
            int charCount = 0;
            for (GlyphInfo glyph : glyphs.values()) {
                if (glyph.id >= 33 && glyph.id <= 126) { // Printable ASCII excluding space
                    totalAdvance += glyph.xAdvance;
                    charCount++;
                }
            }
            spaceWidth = charCount > 0 ? totalAdvance / charCount / 2 : lineHeight / 2;
            spaceGlyph.xAdvance = spaceWidth;

            // Add to glyphs map for consistency
            glyphs.put(32, spaceGlyph);

            Gdx.app.log("SimpleFont", "Created virtual space character with width: " + spaceWidth);
        } else {
            spaceWidth = spaceGlyph.xAdvance;
        }
    }

    private void createTextureRegions() {
        float invTexWidth = 1.0f / texture.getWidth();
        float invTexHeight = 1.0f / texture.getHeight();

        for (GlyphInfo glyph : glyphs.values()) {
            if (glyph.width > 0 && glyph.height > 0) { // Only create regions for actual glyphs
                // Create texture region for this glyph
                TextureRegion region = new TextureRegion(texture,
                    glyph.srcX, glyph.srcY, glyph.width, glyph.height);
                glyph.region = region;
            } else if (glyph.id == 32) {
                // Space character - no texture region needed
                glyph.region = null;
            }
        }
    }

    private void updateScaling() {
        for (GlyphInfo glyph : glyphs.values()) {
            glyph.updateScale(scaleX, scaleY);
        }
        if (missingGlyph != null) {
            missingGlyph.updateScale(scaleX, scaleY);
        }
    }

    // ==================== RENDERING ====================

    /**
     * Draw text at position
     */
    public void draw(Batch batch, CharSequence text, float x, float y) {
        draw(batch, text, x, y, 0, text.length());
    }

    /**
     * Draw text at position with start and end indices
     */
    public void draw(Batch batch, CharSequence text, float x, float y, int start, int end) {
        if (start >= end) return;

        float currentX = x;
        float currentY = y;
        Color originalColor = batch.getColor();

        batch.setColor(tint);

        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                currentX = x;
                currentY -= getLineHeight();
                continue;
            }

            // Handle space character separately for better performance
            if (c == ' ') {
                currentX += getSpaceWidth();
                continue;
            }

            GlyphInfo glyph = glyphs.get((int) c);
            if (glyph == null) {
                glyph = missingGlyph;
            }

            if (glyph == null || glyph.region == null) continue;

            // Calculate draw position with offsets
            float drawX = currentX + glyph.scaledXOffset;
            float drawY = currentY + glyph.scaledYOffset - glyph.scaledHeight + getBaseline();

            if (integerPositions) {
                drawX = (int) drawX;
                drawY = (int) drawY;
            }

            // Draw the glyph
            batch.draw(glyph.region, drawX, drawY, glyph.scaledWidth, glyph.scaledHeight);

            // Debug visualization
            if (debugMode) {
                drawDebugBox(batch, drawX, drawY, glyph.scaledWidth, glyph.scaledHeight);
            }

            // Advance cursor
            currentX += glyph.scaledXAdvance;
        }

        batch.setColor(originalColor);
    }

    /**
     * Draw text with custom color
     */
    public void draw(Batch batch, CharSequence text, float x, float y, Color color) {
        Color oldColor = tint;
        tint = color;
        draw(batch, text, x, y);
        tint = oldColor;
    }

    /**
     * Draw centered text
     */
    public void drawCentered(Batch batch, CharSequence text, float x, float y) {
        float width = getWidth(text);
        draw(batch, text, x - width / 2, y);
    }

    /**
     * Draw text with shadow effect
     */
    public void drawShadow(Batch batch, CharSequence text, float x, float y, float offsetX, float offsetY, Color shadowColor) {
        Color original = tint;

        // Draw shadow
        tint = shadowColor;
        draw(batch, text, x + offsetX, y - offsetY);

        // Draw main text
        tint = original;
        draw(batch, text, x, y);
    }

    /**
     * Draw text with outline effect (simple version)
     */
    public void drawOutlined(Batch batch, CharSequence text, float x, float y, Color outlineColor, float thickness) {
        Color original = tint;

        // Draw outline in 4 directions
        tint = outlineColor;
        draw(batch, text, x - thickness, y);
        draw(batch, text, x + thickness, y);
        draw(batch, text, x, y - thickness);
        draw(batch, text, x, y + thickness);

        // Draw main text
        tint = original;
        draw(batch, text, x, y);
    }

    /**
     * Draw wrapped text
     */
    public void drawWrapped(Batch batch, CharSequence text, float x, float y, float wrapWidth) {
        TextLayout layout = getWrappedLayout(text, wrapWidth);
        float currentY = y;

        for (LineInfo line : layout.lines) {
            draw(batch, line.text, x, currentY);
            currentY -= getLineHeight();
        }
    }

    // ==================== TEXT MEASUREMENT ====================

    /**
     * Get width of text
     */
    public float getWidth(CharSequence text) {
        return getWidth(text, 0, text.length());
    }

    public float getWidth(CharSequence text, int start, int end) {
        float width = 0;
        float lineWidth = 0;

        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                width = Math.max(width, lineWidth);
                lineWidth = 0;
                continue;
            }

            if (c == ' ') {
                lineWidth += getSpaceWidth();
                continue;
            }

            GlyphInfo glyph = glyphs.get((int) c);
            if (glyph == null) glyph = missingGlyph;
            if (glyph != null) {
                lineWidth += glyph.scaledXAdvance;
            }
        }

        return Math.max(width, lineWidth);
    }

    /**
     * Get height of text (number of lines * line height)
     */
    public float getHeight(CharSequence text) {
        int lines = 1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') lines++;
        }
        return lines * getLineHeight();
    }

    /**
     * Get bounds of text
     */
    public Rectangle getBounds(CharSequence text) {
        return new Rectangle(0, 0, getWidth(text), getHeight(text));
    }

    /**
     * Get layout for text (for advanced positioning)
     */
    public TextLayout getLayout(CharSequence text) {
        return getLayout(text, 0, text.length());
    }

    public TextLayout getLayout(CharSequence text, int start, int end) {
        cachedLayout.clear();

        float currentX = 0;
        float maxWidth = 0;
        LineInfo currentLine = null;

        for (int i = start; i < end; i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                if (currentLine != null) {
                    currentLine.width = currentX;
                    maxWidth = Math.max(maxWidth, currentX);
                }
                currentX = 0;
                currentLine = null;
                continue;
            }

            if (currentLine == null) {
                currentLine = new LineInfo();
                cachedLayout.lines.add(currentLine);
            }

            if (c == ' ') {
                currentX += getSpaceWidth();
                continue;
            }

            GlyphInfo glyph = glyphs.get((int) c);
            if (glyph == null) glyph = missingGlyph;
            if (glyph == null) continue;

            currentLine.glyphs.add(glyph);
            currentLine.xPositions.add(currentX);
            currentX += glyph.scaledXAdvance;
        }

        if (currentLine != null) {
            currentLine.width = currentX;
            maxWidth = Math.max(maxWidth, currentX);
        }

        cachedLayout.width = maxWidth;
        cachedLayout.height = cachedLayout.lines.size * getLineHeight();

        return cachedLayout;
    }

    /**
     * Get wrapped text layout
     */
    public TextLayout getWrappedLayout(CharSequence text, float wrapWidth) {
        TextLayout layout = new TextLayout();

        // Simple word wrapping
        String[] words = text.toString().split(" ");
        StringBuilder currentLine = new StringBuilder();
        float currentWidth = 0;
        float spaceWidth = getSpaceWidth();

        for (String word : words) {
            float wordWidth = getWidth(word);

            if (currentWidth + wordWidth > wrapWidth && currentLine.length() > 0) {
                // Add current line to layout
                LineInfo line = new LineInfo();
                line.text = currentLine.toString();
                line.width = currentWidth;
                layout.lines.add(line);

                // Start new line
                currentLine = new StringBuilder(word);
                currentWidth = wordWidth;
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(' ');
                    currentWidth += spaceWidth;
                }
                currentLine.append(word);
                currentWidth += wordWidth;
            }
        }

        // Add last line
        if (currentLine.length() > 0) {
            LineInfo line = new LineInfo();
            line.text = currentLine.toString();
            line.width = currentWidth;
            layout.lines.add(line);
        }

        layout.width = wrapWidth;
        layout.height = layout.lines.size * getLineHeight();

        return layout;
    }

    /**
     * Get individual glyph positions (for click detection or animations)
     */
    public Array<GlyphPosition> getGlyphPositions(CharSequence text, float x, float y) {
        Array<GlyphPosition> positions = new Array<>();
        float currentX = x;
        float currentY = y;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\n') {
                currentX = x;
                currentY -= getLineHeight();
                continue;
            }

            if (c == ' ') {
                // Skip adding position for spaces
                currentX += getSpaceWidth();
                continue;
            }

            GlyphInfo glyph = glyphs.get((int) c);
            if (glyph == null) glyph = missingGlyph;
            if (glyph == null || glyph.region == null) continue;

            GlyphPosition pos = new GlyphPosition();
            pos.glyph = glyph;
            pos.x = currentX + glyph.scaledXOffset;
            pos.y = currentY + glyph.scaledYOffset - glyph.scaledHeight + getBaseline();
            pos.index = i;

            positions.add(pos);
            currentX += glyph.scaledXAdvance;
        }

        return positions;
    }

    /**
     * Find character at screen position (for click detection)
     */
    public int getCharacterIndexAt(CharSequence text, float x, float y, float textX, float textY) {
        Array<GlyphPosition> positions = getGlyphPositions(text, textX, textY);

        for (GlyphPosition pos : positions) {
            if (x >= pos.x && x <= pos.x + pos.glyph.scaledWidth &&
                y <= pos.y + pos.glyph.scaledHeight && y >= pos.y) {
                return pos.index;
            }
        }

        return -1;
    }

    // ==================== FONT METRICS ====================

    public float getLineHeight() {
        return lineHeight * scaleY;
    }

    public float getBaseline() {
        return base * scaleY;
    }

    public float getSpaceWidth() {
        if (spaceGlyph != null) {
            return spaceGlyph.scaledXAdvance;
        }
        // Fallback: use average character width
        float avgWidth = getWidth("n") * 0.5f;
        return avgWidth > 0 ? avgWidth : lineHeight * scaleX * 0.5f;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public Color getColor() {
        return tint;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean hasCharacter(char c) {
        return glyphs.containsKey((int) c);
    }

    // ==================== FONT MODIFICATION ====================

    /**
     * Set font scale
     */
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        updateScaling();

        // Clear layout cache when scale changes
        layoutCache.clear();
    }

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    /**
     * Set text color
     */
    public void setColor(Color color) {
        this.tint.set(color);
    }

    public void setColor(float r, float g, float b, float a) {
        this.tint.set(r, g, b, a);
    }

    /**
     * Enable/disable integer positioning (prevents blurry text)
     */
    public void setUseIntegerPositions(boolean integer) {
        this.integerPositions = integer;
    }

    /**
     * Enable debug mode to see character bounds
     */
    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    /**
     * Clear the layout cache to free memory
     */
    public void clearCache() {
        layoutCache.clear();
    }

    // ==================== UTILITY METHODS ====================

    private void drawDebugBox(Batch batch, float x, float y, float width, float height) {
        Color original = batch.getColor();
        batch.setColor(debugColor);

        // Draw outline
        batch.draw(getWhitePixel(), x, y, width, 1); // Bottom
        batch.draw(getWhitePixel(), x, y + height, width, 1); // Top
        batch.draw(getWhitePixel(), x, y, 1, height); // Left
        batch.draw(getWhitePixel(), x + width, y, 1, height); // Right

        batch.setColor(original);
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
        Gdx.app.log("SimpleFont", "=== Loaded Glyphs ===");
        Gdx.app.log("SimpleFont", String.format("Font: %s, LineHeight: %d, Base: %d",
            fontName != null ? fontName : "Unknown", lineHeight, base));

        for (GlyphInfo glyph : glyphs.values()) {
            if (glyph.character >= 32 && glyph.character <= 126) { // Printable ASCII
                Gdx.app.log("SimpleFont", glyph.toString());
            }
        }

        Gdx.app.log("SimpleFont", "Total: " + glyphs.size() + " glyphs");
    }

    /**
     * Test if all characters in a string exist in the font
     */
    public String findMissingCharacters(CharSequence text) {
        StringBuilder missing = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!hasCharacter(c) && missing.indexOf(String.valueOf(c)) == -1) {
                missing.append(c);
            }
        }
        return missing.toString();
    }

    @Override
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
        glyphs.clear();
        layoutCache.clear();
    }
}
