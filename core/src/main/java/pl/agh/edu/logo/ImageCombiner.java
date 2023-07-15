package pl.agh.edu.logo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// klasa stack - doczytaj

public class ImageCombiner {
    public ImageCombiner(String path1, String path2, String path3) {
        // Load the three input images
        BufferedImage image1 = loadImage(path1);
        BufferedImage image2 = loadImage(path2);
        BufferedImage image3 = loadImage(path3);

        // Create a blank canvas for the combined image
        int canvasWidth = image1.getWidth() + image2.getWidth() + image3.getWidth();
        int canvasHeight = Math.max(image1.getHeight(), Math.max(image2.getHeight(), image3.getHeight()));
        BufferedImage combinedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);

        // Draw the three images onto the canvas
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(image1, 0, 0, null);
        g2d.drawImage(image2, 0, 0, null);
        g2d.drawImage(image3, 0, 0, null);
        g2d.dispose();

        // Save the combined image to a PNG file
        saveImage(combinedImage, "assets/img/combined.png");

        System.out.println("Images combined successfully.");
    }

    private static BufferedImage loadImage(String filePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static void saveImage(BufferedImage image, String filePath) {
        try {
            ImageIO.write(image, "PNG", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






