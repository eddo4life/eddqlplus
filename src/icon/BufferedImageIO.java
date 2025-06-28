package icon;

import view.iconmaker.IconGenerator;
import view.pupupsmessage.PupupMessages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class BufferedImageIO {

    public BufferedImageIO() {
    }

    public ImageIcon getIcon(int x, int y, String name) {

        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(BufferedImageIO.class.getResource(name)));
        } catch (Exception e) {
            new PupupMessages().message(
                    "Error system, some resources couldn't be loaded, restart the app or contact assistance.",
                    new IconGenerator().exceptionIcon());
            return new ImageIcon(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));

        }
        ImageIcon icon = new ImageIcon();
        icon.setImage(bufferedImage);
        Image scaleImage = icon.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH);
        return new ImageIcon(scaleImage);
    }
}