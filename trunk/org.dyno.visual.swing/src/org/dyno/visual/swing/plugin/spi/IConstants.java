/******************************************************************************
 * Copyright (c) 2008 William Chen.                                           *
 *                                                                            *
 * All rights reserved. This program and the accompanying materials are made  *
 * available under the terms of GNU Lesser General Public License.            *
 *                                                                            * 
 * Use is subject to the terms of GNU Lesser General Public License.          * 
 ******************************************************************************/
package org.dyno.visual.swing.plugin.spi;

import java.awt.Color;

/**
 * 
 * IConstants
 *
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
public interface IConstants {
	int STATE_MOUSE_HOVER = 0;
	int STATE_MOUSE_DRAGGING = 1;
	int STATE_BEAN_TOBE_HOVER = 2;
	int STATE_BEAN_HOVER = 3;
	int STATE_SELECTION = 4;
	int STATE_BEAN_RESIZE_LEFT = 5;
	int STATE_BEAN_RESIZE_LEFT_TOP = 6;
	int STATE_BEAN_RESIZE_TOP = 7;
	int STATE_BEAN_RESIZE_RIGHT_TOP = 8;
	int STATE_BEAN_RESIZE_RIGHT = 9;
	int STATE_BEAN_RESIZE_RIGHT_BOTTOM = 10;
	int STATE_BEAN_RESIZE_BOTTOM = 11;
	int STATE_BEAN_RESIZE_LEFT_BOTTOM = 12;
	int STATE_BEAN_TOBE_RESIZED_RIGHT = 13;
	int STATE_BEAN_TOBE_RESIZED_RIGHT_BOTTOM = 14;
	int STATE_BEAN_TOBE_RESIZED_BOTTOM = 15;
	int STATE_BEAN_TOBE_RESIZED_LEFT_BOTTOM = 16;
	int STATE_BEAN_TOBE_RESIZED_LEFT = 17;
	int STATE_BEAN_TOBE_RESIZED_LEFT_TOP = 18;
	int STATE_BEAN_TOBE_RESIZED_TOP = 19;
	int STATE_BEAN_TOBE_RESIZED_RIGHT_TOP = 20;
	int STATE_ROOT_RESIZE_RIGHT = 21;
	int STATE_ROOT_RESIZE_RIGHT_BOTTOM = 22;
	int STATE_ROOT_RESIZE_BOTTOM = 23;
	int DND_THRESHOLD = 5;
	
	String ADAPTER_PROPERTY = "widget.adapter";
	int OUTER = 0;
	int INNER = 1;
	int LEFT_TOP = 2;
	int TOP = 3;
	int RIGHT_TOP = 4;
	int RIGHT = 5;
	int RIGHT_BOTTOM = 6;
	int BOTTOM = 7;
	int LEFT_BOTTOM = 8;
	int LEFT = 9;
	int ADHERE_PAD = 6;

	int ACCESS_PRIVATE = 0;
	int ACCESS_DEFAULT = 1;
	int ACCESS_PROTECTED = 2;
	int ACCESS_PUBLIC = 3;
	
	Color SELECTION_COLOR = new Color(255, 164, 0);	
}
