import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
public class UFO {
	private int xLocation;
	private int yLocation;
	private static BufferedImage image;
	private static String imageLocation;
	
	static  {
		try {
		    image = ImageIO.read(new File(imageLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UFO (int xLocation, int yLocation) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.image = image;
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
