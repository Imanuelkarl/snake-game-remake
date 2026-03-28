package com.darealfungames.snakevsblock.lwjgl3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BitmapFontGenerator {

    public static void main(String[] args) throws IOException {

        // Target path in your libGDX project
        String targetPath = "assets/fonts/ImageFonts/";

        // Create the output file paths
        String outputFile = targetPath + "FontsHDOne.fnt";
        String textureFile = targetPath+"FontsHDOne.png"; // This is referenced in the .fnt file, so keep just the filename

        int cellWidth = 64;
        int cellHeight = 64;

        int cols = 26; // max columns in any row
        int rows = 4;

        int textureWidth = cols * cellWidth;
        int textureHeight = rows * cellHeight;

        // Create the directory structure if it doesn't exist
        File outputDir = new File(targetPath);
        if (!outputDir.exists()) {
            if (outputDir.mkdirs()) {
                System.out.println("Created directory: " + outputDir.getAbsolutePath());
            } else {
                System.err.println("Failed to create directory: " + outputDir.getAbsolutePath());
                return;
            }
        }

        // Get the absolute path where the files will be created
        File outputPath = new File(outputFile);
        File texturePath = new File(targetPath + textureFile);

        // Get the current working directory
        String currentDir = System.getProperty("user.dir");

        System.out.println("Current working directory: " + currentDir);
        System.out.println("Font file will be created at: " + outputPath.getAbsolutePath());
        System.out.println("Texture file will be created at: " + texturePath.getAbsolutePath());
        System.out.println();

        FileWriter writer = new FileWriter(outputFile);

        // Header - note that texture file reference is just the filename (no path)
        writer.write("info face=\"CustomFont\" size=" + cellHeight + " bold=0 italic=0 charset=\"\" unicode=1 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1\n");
        writer.write("common lineHeight=" + cellHeight + " base=" + (cellHeight - 12) + " scaleW=" + textureWidth + " scaleH=" + textureHeight + " pages=1 packed=0\n");
        writer.write("page id=0 file=\"" + textureFile + "\"\n");

        String[] rowsData = new String[]{
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "0123456789",
            "\"!`?'.;,;:()[]{}<>|/@\\^$-%+=#_&~*₦¥€"
        };

        int totalChars = 0;
        for (String row : rowsData) totalChars += row.length();

        writer.write("chars count=" + totalChars + "\n");

        int y = 0;

        for (int rowIndex = 0; rowIndex < rowsData.length; rowIndex++) {
            String rowChars = rowsData[rowIndex];
            int x = 0;

            for (int i = 0; i < rowChars.length(); i++) {
                char c = rowChars.charAt(i);
                int charId = (int) c;

                writer.write("char id=" + charId +
                    " x=" + x +
                    " y=" + y +
                    " width=" + cellWidth +
                    " height=" + cellHeight +
                    " xoffset=0 yoffset=0 xadvance=" + cellWidth +
                    " page=0 chnl=0\n");

                x += cellWidth;
            }

            y += cellHeight;
        }

        writer.close();

        System.out.println("Font file generated successfully!");
        System.out.println("Output location: " + outputPath.getAbsolutePath());
        System.out.println("\nIMPORTANT: Don't forget to copy your texture file (FontsHDOne.png) to:");
        System.out.println(texturePath.getAbsolutePath());
        System.out.println("The .fnt file references this texture by filename only, so they must be in the same folder!");
    }
}
