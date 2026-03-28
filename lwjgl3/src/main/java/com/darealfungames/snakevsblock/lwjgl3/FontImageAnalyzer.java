package com.darealfungames.snakevsblock.lwjgl3;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FontImageAnalyzer {

    private static class CharacterInfo {
        char character;
        int x, y, width, height;
        int xoffset, yoffset, xadvance;

        @Override
        public String toString() {
            return String.format("%c: pos=(%d,%d) size=(%dx%d) advance=%d",
                character, x, y, width, height, xadvance);
        }
    }

    public static void main(String[] args) {
        try {
            // Configuration
            String imagePath = "assets/fonts/ImageFonts/FontsHDOne.png";
            String outputPath = "assets/fonts/ImageFonts/FontsHDOne.fnt";
            String characterOrder = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" +
                "\"!`?'.;,;:()[]{}<>|/@\\^$-%+=#_&~*₦¥€";

            // Analyze the image
            analyzeFontImage(imagePath, outputPath, characterOrder);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void analyzeFontImage(String imagePath, String outputPath, String characterOrder)
        throws IOException {

        System.out.println("=== Font Image Analyzer ===\n");

        // Load the image
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            throw new IOException("Image not found: " + imageFile.getAbsolutePath());
        }

        BufferedImage image = ImageIO.read(imageFile);
        System.out.println("Loaded image: " + image.getWidth() + "x" + image.getHeight());

        // Find all character regions
        List<Rectangle> characterRegions = findCharacterRegions(image);
        System.out.println("Found " + characterRegions.size() + " character regions\n");

        // Sort regions by position (top to bottom, left to right)
        characterRegions.sort((a, b) -> {
            if (Math.abs(a.y - b.y) < 20) { // Same row
                return Integer.compare(a.x, b.x);
            }
            return Integer.compare(a.y, b.y);
        });

        // Map regions to characters
        List<CharacterInfo> characters = new ArrayList<>();
        int charIndex = 0;

        for (Rectangle rect : characterRegions) {
            if (charIndex >= characterOrder.length()) {
                System.err.println("Warning: More character regions than expected characters");
                break;
            }

            char c = characterOrder.charAt(charIndex);
            CharacterInfo info = new CharacterInfo();
            info.character = c;
            info.x = rect.x;
            info.y = rect.y;
            info.width = rect.width;
            info.height = rect.height;

            // Calculate offsets and advance
            calculateMetrics(info, image);

            characters.add(info);
            System.out.println(info);
            charIndex++;
        }

        // Generate the font file
        generateFontFile(outputPath, characters, image.getWidth(), image.getHeight());

        // Create a debug visualization
        createDebugVisualization(image, characters, outputPath.replace(".fnt", "_debug.png"));

        System.out.println("\n✓ Font file generated: " + new File(outputPath).getAbsolutePath());
        System.out.println("✓ Debug visualization saved to: " + outputPath.replace(".fnt", "_debug.png"));
        System.out.println("\nIMPORTANT: Check the debug image to verify character detection!");
    }

    private static List<Rectangle> findCharacterRegions(BufferedImage image) {
        List<Rectangle> regions = new ArrayList<>();
        boolean[][] visited = new boolean[image.getWidth()][image.getHeight()];

        // Threshold for considering a pixel as "content" (not background)
        int threshold = 250; // Assuming white background (255)

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int brightness = (rgb >> 16) & 0xFF; // Red channel (assuming grayscale or colored)

                // If this is a content pixel and not visited
                if (brightness < threshold && !visited[x][y]) {
                    // Flood fill to find connected component
                    Rectangle bounds = floodFill(image, visited, x, y, threshold);

                    // Add padding around the character
                    int padding = 2;
                    bounds.x = Math.max(0, bounds.x - padding);
                    bounds.y = Math.max(0, bounds.y - padding);
                    bounds.width = Math.min(image.getWidth() - bounds.x, bounds.width + padding * 2);
                    bounds.height = Math.min(image.getHeight() - bounds.y, bounds.height + padding * 2);

                    regions.add(bounds);
                }
            }
        }

        return regions;
    }

    private static Rectangle floodFill(BufferedImage image, boolean[][] visited,
                                       int startX, int startY, int threshold) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));
        visited[startX][startY] = true;

        int minX = startX, minY = startY;
        int maxX = startX, maxY = startY;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);

            // Check 4-directional neighbors
            int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
            for (int[] dir : dirs) {
                int nx = p.x + dir[0];
                int ny = p.y + dir[1];

                if (nx >= 0 && nx < image.getWidth() && ny >= 0 && ny < image.getHeight() && !visited[nx][ny]) {
                    int rgb = image.getRGB(nx, ny);
                    int brightness = (rgb >> 16) & 0xFF;

                    if (brightness < threshold) {
                        visited[nx][ny] = true;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }

        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    private static void calculateMetrics(CharacterInfo info, BufferedImage image) {
        // Default xadvance is width (most characters)
        info.xadvance = info.width;
        info.xoffset = 0;
        info.yoffset = 0;

        // Adjust for narrow characters
        char c = info.character;
        if (c == '.' || c == ',' || c == ';' || c == ':' || c == '!' || c == '?' ||
            c == '\'' || c == '"' || c == '`' || c == 'i' || c == 'j' || c == 'l' || c == '|') {
            info.xadvance = Math.max(info.width / 2, 20);
            info.xoffset = (info.width - info.xadvance) / 2;
        }

        // Adjust for wide characters
        if (c == 'W' || c == 'w' || c == 'M' || c == 'm') {
            info.xadvance = info.width;
        }

        // Calculate vertical offset for descenders
        if (c == 'g' || c == 'j' || c == 'p' || c == 'q' || c == 'y' ||
            c == ',' || c == ';' || c == '?' || c == '!') {
            info.yoffset = info.height / 5;
        }

        // Analyze actual character bounds for more accurate metrics
        analyzeCharacterBounds(info, image);
    }

    private static void analyzeCharacterBounds(CharacterInfo info, BufferedImage image) {
        // Find actual character bounds (remove padding)
        int minX = info.width, minY = info.height, maxX = 0, maxY = 0;
        boolean foundPixel = false;

        for (int y = 0; y < info.height; y++) {
            for (int x = 0; x < info.width; x++) {
                int px = info.x + x;
                int py = info.y + y;
                if (px < image.getWidth() && py < image.getHeight()) {
                    int rgb = image.getRGB(px, py);
                    int brightness = (rgb >> 16) & 0xFF;
                    if (brightness < 250) { // Content pixel
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                        foundPixel = true;
                    }
                }
            }
        }

        if (foundPixel) {
            // Update bounds to actual character area
            int actualWidth = maxX - minX + 1;
            int actualHeight = maxY - minY + 1;

            // Adjust xadvance based on actual width
            if (actualWidth < info.width * 0.7) {
                info.xadvance = actualWidth + 8; // Add small padding
            }
        }
    }

    private static void generateFontFile(String outputPath, List<CharacterInfo> characters,
                                         int textureWidth, int textureHeight) throws IOException {
        FileWriter writer = new FileWriter(outputPath);

        // Calculate common values
        int maxHeight = characters.stream().mapToInt(c -> c.height).max().orElse(64);
        int baseLine = (int)(maxHeight * 0.75);

        // Header
        writer.write("info face=\"CustomFont\" size=" + maxHeight +
            " bold=0 italic=0 charset=\"\" unicode=1 stretchH=100 smooth=1 aa=1 " +
            "padding=0,0,0,0 spacing=1,1\n");
        writer.write("common lineHeight=" + maxHeight +
            " base=" + baseLine +
            " scaleW=" + textureWidth +
            " scaleH=" + textureHeight +
            " pages=1 packed=0\n");
        writer.write("page id=0 file=\"FontsHDOne.png\"\n");

        // Characters
        writer.write("chars count=" + characters.size() + "\n");

        for (CharacterInfo c : characters) {
            writer.write(String.format("char id=%d x=%d y=%d width=%d height=%d xoffset=%d yoffset=%d xadvance=%d page=0 chnl=0\n",
                (int)c.character, c.x, c.y, c.width, c.height, c.xoffset, c.yoffset, c.xadvance));
        }

        // Optional: Add kerning if needed
        // writer.write("kernings count=0\n");

        writer.close();
    }

    private static void createDebugVisualization(BufferedImage image, List<CharacterInfo> characters,
                                                 String outputPath) throws IOException {
        // Create a copy of the image
        BufferedImage debugImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = debugImage.createGraphics();
        g.drawImage(image, 0, 0, null);

        // Draw bounding boxes and character info
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));

        for (CharacterInfo info : characters) {
            // Draw rectangle around character
            g.drawRect(info.x, info.y, info.width, info.height);

            // Draw text label
            g.setColor(Color.GREEN);
            String label = String.valueOf(info.character);
            g.drawString(label, info.x + 2, info.y + 15);

            // Draw xadvance line
            g.setColor(Color.BLUE);
            int advanceX = info.x + info.xoffset + info.xadvance;
            g.drawLine(advanceX, info.y, advanceX, info.y + info.height);
        }

        g.dispose();

        // Save debug image
        ImageIO.write(debugImage, "png", new File(outputPath));
        System.out.println("Debug image saved with " + characters.size() + " detected characters");
    }
}
