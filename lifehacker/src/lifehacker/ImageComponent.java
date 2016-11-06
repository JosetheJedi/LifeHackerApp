package lifehacker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageComponent extends JComponent
{
	private static int IMAGE_WIDTH = 320;	// the final width for the image
	private static int IMAGE_HEIGHT = 180;	// the final height for the image
	
	// these variables are used to make sure that each side of the image are
	// within the JPanel bounds
	private int xlocationLeft = 100;						// xlocationLeft is the images location from the left side
	private int xlocationRight = xlocationLeft + IMAGE_WIDTH; // this will keep track of the image from it's right side
	private int ylocationTop = 100;							// the images location from the top side
	private int ylocationDown = ylocationTop + IMAGE_HEIGHT;	// the images location from it's bottom side
	
	private BufferedImage image;		// image object 
	String fileLocation = "tmpResources/image0.jpg";	// default file to use when the app is started
	
	// instantiating the component through the constructor
	public ImageComponent()
	{
		// will attempt to load the image file.
		// and if it does not exist, then the program will 
		// notify the user that the file is not found.
		try
		{
			image = ImageIO.read(new File(fileLocation));
		}
		catch(IOException e)
		{
			System.out.println("File not found");
		}
	}
	
	// This method will paint the image component 
	// onto the screen.
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// the image will be drawn at the certain location given, with the certain width and height given.
        g.drawImage(image, xlocationLeft, ylocationTop, IMAGE_WIDTH, IMAGE_HEIGHT, null);
	}


	/**
	 * This method will change the fileLocation to a different location for
	 * another image. then it will call the repaint() method to redraw the image.
	 * @param fileLocation: a string of the file location for the image
	 * selected by the user.
	 */
	public void changeImage(String file)
	{
		this.fileLocation = "tmpResources/" + file;
		
		// will attempt to load the image file.
		// and if it does not exist, then the program will 
		// notify the user that the file is not found.
		try
		{
			image = ImageIO.read(new File(fileLocation));
		}
		catch(IOException e)
		{
			System.out.println("File not found");
		}
		repaint();
	}

	public static int getIMAGE_WIDTH() {
		return IMAGE_WIDTH;
	}

	public static void setIMAGE_WIDTH(int iMAGE_WIDTH) {
		IMAGE_WIDTH = iMAGE_WIDTH;
	}

	public static int getIMAGE_HEIGHT() {
		return IMAGE_HEIGHT;
	}

	public static void setIMAGE_HEIGHT(int iMAGE_HEIGHT) {
		IMAGE_HEIGHT = iMAGE_HEIGHT;
	}	
	
	
}