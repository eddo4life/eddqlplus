package icon.tools;

import view.iconmaker.IconGenerator;
import view.pupupsmessage.PopupMessages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageIO {

    public BufferedImageIO() {
    }

    public ImageIcon getIcon(int x, int y, String name) {

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(BufferedImageIO.class.getResource(name));
        } catch (IOException e) {
            new PopupMessages().message(
                    "Error system, some resources couldn't be loaded, restart the app or contact assistance.",
                    new IconGenerator().exceptionIcon());
            return null;
        }
        ImageIcon icon = new ImageIcon();
        icon.setImage(bufferedImage);
        Image scaleImage = icon.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH);
        return new ImageIcon(scaleImage);
    }
}