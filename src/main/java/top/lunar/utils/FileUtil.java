package top.lunar.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
            } else
                ++x;
        }
        File file = new File("output.bmp");
        ImageIO.write(image, "BMP", file);
        return file;
    }
}
