package main;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ResourceHelper{
	
	/**
	 * Located the image of the specified name.
	 * @param path The name of the image
	 * @return An image located at the area, or null if not found
	 */
	public static Image getImage(String path){
		return new ImageIcon(ResourceHelper.class.getResource("../assets/"+path)).getImage();
	}
}
