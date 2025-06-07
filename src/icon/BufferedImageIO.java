package icon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import view.iconmaker._Icon;
import view.pupupsmessage.PupupMessages;

public class BufferedImageIO {

	public BufferedImageIO(){}
	public ImageIcon getIcon(int x, int y, String name) {

		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(BufferedImageIO.class.getResource(name));
		} catch (IOException e) {
			new PupupMessages().message(
					"Error system, some resources couldn't be loaded, restart the app or contact assistance.",
					new _Icon().exceptionIcon());
			return null;
		}
		ImageIcon icon = new ImageIcon();
		icon.setImage(bufferedImage);
		Image scaleImage = icon.getImage().getScaledInstance(x, y, Image.SCALE_SMOOTH);
		return new ImageIcon(scaleImage);
	}
}