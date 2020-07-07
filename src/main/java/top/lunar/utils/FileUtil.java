package top.lunar.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class FileUtil {
    public static File encode(String text) throws IOException {
        long textLen = text.length();
        int width = (int)Math.sqrt(textLen)+1;
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);

        int x = 0, y = 0;
        for(int i = 0; i < textLen; i++) {
            char s = text.charAt(i);
            Color color = new Color(0, ((int)s & 0xFF00) >> 8, (int)s & 0xFF);
            image.setRGB(x, y, color.getRGB());
            if (x==(width-1)) {
                x = 0;
                ++y;
            } else {
                ++x;
            }
        }
        File file = new File(System.currentTimeMillis() + "output.bmp");
        ImageIO.write(image, "BMP", file);
        return file;
    }

    public static File decode(BufferedImage img) {
        int width = img.getWidth(), height = img.getHeight();
        StringBuilder sb = new StringBuilder();
        PrintStream ps = null;

        for(int y = 0; y<height; y++) {
            for(int x = 0; x<width; x++) {
                Color color = new Color(img.getRGB(x, y));
                if(color.getGreen()==0&&color.getBlue()==0&&color.getRed()==0) {
                    break;
                }
                int index = (color.getGreen() << 8) + color.getBlue();
                sb.append((char) index);
            }
        }

        File outFile = new File(System.currentTimeMillis() + "output.txt");
        try {
            ps = new PrintStream(new FileOutputStream(outFile));
            ps.append(sb.toString());
            return outFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            ps.close();
        }
        return null;
    }
}
