
/************************************************************************************
 * Copyright (c) 2008 William Chen.                                                 *
 *                                                                                  *
 * All rights reserved. This program and the accompanying materials are made        *
 * available under the terms of the Eclipse Public License v1.0 which accompanies   *
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html *
 *                                                                                  *
 * Use is subject to the terms of Eclipse Public License v1.0.                      *
 *                                                                                  *
 * Contributors:                                                                    * 
 *     William Chen - initial API and implementation.                               *
 ************************************************************************************/

package org.dyno.visual.swing.types.renderer;

import java.awt.Color;

import org.dyno.visual.swing.types.TypePlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;

public class ColorLabelProvider extends LabelProvider {
	private static int BOX_SIZE = 13;
	private static final int DEFAULT_EXTENT = 18;
	private static RGB BORDER_RGB = new RGB(28, 81, 128);

	public ColorLabelProvider() {
	}

	@Override
	public Image getImage(Object element) {
		if (element == null)
			return null;
		RGB rgb = new RGB(0, 0, 0);
		if (element != null && element instanceof RGB) {
			rgb = (RGB) element;
		}
		String rgb_id = "rgb" + rgb.hashCode();
		Image image = TypePlugin.getDefault().getImageRegistry().get(rgb_id);
		if (image == null) {
			ImageData id = createColorImage(rgb);
			image = ImageDescriptor.createFromImageData(id).createImage();
			TypePlugin.getDefault().getImageRegistry().put(rgb_id, image);
		}
		return image;
	}

	private ImageData createColorImage(RGB color) {

		int size = BOX_SIZE;

		int indent = 1;
		int extent = DEFAULT_EXTENT;

		int width = indent + size + 2;
		int height = extent;

		int xoffset = indent;
		int yoffset = (height - size) / 2;

		RGB black = new RGB(0, 0, 0);
		PaletteData dataPalette = new PaletteData(new RGB[] { black, BORDER_RGB, color });
		ImageData data = new ImageData(width, height, 4, dataPalette);
		data.transparentPixel = 0;

		int end = size - 1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (x == 0 || y == 0 || x == end || y == end) {
					data.setPixel(x + xoffset, y + yoffset, 1);
				} else {
					data.setPixel(x + xoffset, y + yoffset, 2);
				}
			}
		}

		return data;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "null";
		if (element instanceof Color) {
			Color color = (Color) element;
			return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
		} else if (element instanceof RGB) {
			RGB rgb = (RGB) element;
			return "(" + rgb.red + ", " + rgb.green + ", " + rgb.blue + ")";
		} else {
			return element.toString();
		}
	}
}

