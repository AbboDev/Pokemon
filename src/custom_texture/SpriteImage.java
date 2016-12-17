package custom_texture;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Thomas
 */
public class SpriteImage extends ImageIcon {
    private static final long serialVersionUID = 761260847832282907L;
    
    /**
     * Return a Image from a File
     * @param path the path where the image is
     * @return the ImageIcon get from path
     */
    public static ImageIcon getImage(String path) {
        BufferedImage imageBuff = loadImage(path);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageBuff).getImage());
        return imageIcon;
    }
    
    /**
     * 
     * @param image
     * @return
     */
    public static ImageIcon getMirrorImage(ImageIcon image) {
        ImageIcon imageIcon = new MirrorImageIcon(image.getImage());
        return imageIcon;
    }
    
    /**
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon getScaledImage(ImageIcon image, int width, int height) {
        ImageIcon imageIcon =
                new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param dim
     * @return
     */
    public static ImageIcon getScaledImage(ImageIcon image, int dim) {
        ImageIcon imageIcon =
                new ImageIcon(image.getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
        return imageIcon;
    }
    
    /**
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon getScaledMirrorImage(ImageIcon image, int width, int height) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getScaledImage(image, width, height).getImage());
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param dim
     * @return
     */
    public static ImageIcon getScaledMirrorImage(ImageIcon image, int dim) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getScaledImage(image, dim, dim).getImage());
        return imageIcon;
    }
    
    /**
     *
     * @param image
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ImageIcon getColorizedImage(ImageIcon image, int r, int g, int b) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, false, 0);
        ImageIcon imageIcon =
                new ImageIcon(new ImageIcon(imageColor).getImage());
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static ImageIcon getAlphaColorizedImage(ImageIcon image, int r, int g, int b, int a) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, true, a);
        ImageIcon imageIcon =
                new ImageIcon(new ImageIcon(imageColor).getImage());
        return imageIcon;
    }
    
    /**
     *
     * @param image
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ImageIcon getColorizedMirrorImage(ImageIcon image, int r, int g, int b) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getColorizedImage(image, r, g, b).getImage());
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static ImageIcon getAlphaColorizedMirrorImage(ImageIcon image, int r, int g, int b, int a) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getAlphaColorizedImage(image, r, g, b, a).getImage());
        return imageIcon;
    }
   
    /**
     *
     * @param image
     * @param width
     * @param height
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ImageIcon getColorizedScaledImage(ImageIcon image, int width, int height, int r, int g, int b) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, false, 0);
        ImageIcon imageIcon =
                new ImageIcon(imageColor.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param dim
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ImageIcon getColorizedScaledImage(ImageIcon image, int dim, int r, int g, int b) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, false, 0);
        ImageIcon imageIcon =
                new ImageIcon(imageColor.getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param width
     * @param height
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static ImageIcon getAlphaColorizedScaledImage(ImageIcon image, int width, int height, int r, int g, int b, int a) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, true, a);
        ImageIcon imageIcon =
                new ImageIcon(imageColor.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param dim
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static ImageIcon getAlphaColorizedScaledImage(ImageIcon image, int dim, int r, int g, int b, int a) {
        BufferedImage imageColor =
                colorImage(imageToBufferedImage(image.getImage()), r, g, b, true, a);
        ImageIcon imageIcon =
                new ImageIcon(imageColor.getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
        return imageIcon;
    }
    
    /**
     *
     * @param image
     * @param width
     * @param height
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static ImageIcon getColorizedScaledMirrorImage(ImageIcon image, int width, int height, int r, int g, int b) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getColorizedScaledImage(image, width, height, r, g, b).getImage());
        return imageIcon;
    }

    /**
     *
     * @param image
     * @param width
     * @param height
     * @param r
     * @param g
     * @param b
     * @param a
     * @return
     */
    public static ImageIcon getAlphaColorizedScaledMirrorImage(ImageIcon image, int width, int height, int r, int g, int b, int a) {
        ImageIcon imageIcon =
                new MirrorImageIcon(getAlphaColorizedScaledImage(image, width, height, r, g, b, a).getImage());
        return imageIcon;
    }
    
    /**
     * Converts a given Icon into an Image
     * http://stackoverflow.com/questions/5830533/how-can-i-convert-an-icon-to-an-image
     * @param icon The Icon to be converted
     * @return The converted Image
     */
    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
           return ((ImageIcon)icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }
    /**
     * Converts a given Icon into an ImageIcon
     * @param icon The Icon to be converted
     * @return The converted ImageIcon
     */
    public static ImageIcon iconToImageIcon(Icon icon) {
        if (icon instanceof ImageIcon) {
           return (ImageIcon)icon;
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return new ImageIcon(image);
        }
    }
    /**
     * Converts a given Image into a BufferedImage
     * http://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    public static BufferedImage imageToBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        } else {
            // Create a buffered image with transparency
            BufferedImage bimage = new BufferedImage(img.getWidth(null),
                    img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            // Draw the image on to the buffered image
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();
            // Return the buffered image
            return bimage;
        }
    }
    
    /**
     * Recolorize an Image
     * Add the RGB values to each pixels of the image
     * http://www.java2s.com/Tutorials/Java/Graphics_How_to/Image/Change_color_of_png_format_image.htm
     * @param image
     * @param r the red value of RGB
     * @param g the green value of RGB
     * @param b the blue value of RGB
     * @param use
     * @param a the alpha value of RGB
     * @return 
     */
    public static BufferedImage colorImage(BufferedImage image, int r, int g, int b, boolean use, int a) {
        int width = image.getWidth();
        int height = image.getHeight();
        WritableRaster raster = image.getRaster();

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                int[] pixels = raster.getPixel(xx, yy, (int[]) null);
                pixels[0] += r/2;
                pixels[1] += g/2;
                pixels[2] += b/2;
                if (use) {
                    pixels[3] = a;
                }
                for (int i = 0; i < pixels.length; ++i) {
                    if (pixels[i] < 0) pixels[i] = 0;
                    else if (pixels[i] > 255) pixels[i] = 255;
                }
                raster.setPixel(xx, yy, pixels);
            }
        }
        return image;
    }
    
    public static BufferedImage colorImage(Image image, int r, int g, int b) {
        BufferedImage buff = colorImage(imageToBufferedImage(image), r, g, b, false, 0);
        return buff;
    }
    public static BufferedImage colorImage(Image image, int r, int g, int b, int a) {
        BufferedImage buff = colorImage(imageToBufferedImage(image), r, g, b, true, a);
        return buff;
    }
    
    private static BufferedImage loadImage(String path) {
        BufferedImage buff;
        try {
            buff = ImageIO.read(new File(path));
        } catch (IOException e) {
            return null;
        }
        return buff;
    }
}

/**
 * This class return an instance of image
 * which is horizontaly flip
 * http://stackoverflow.com/questions/1708011/create-a-imageicon-that-is-the-mirror-of-another-one
 */
@SuppressWarnings("serial")
class MirrorImageIcon extends ImageIcon {

    public MirrorImageIcon(Image filename) {
    	super(filename);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
    	Graphics2D g2 = (Graphics2D)g.create();
    	g2.translate(getIconWidth(), 0);
    	g2.scale(-1, 1);
    	super.paintIcon(c, g2, -x, y);
    }
}
