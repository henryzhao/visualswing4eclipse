
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

package org.dyno.visual.swing.widgets.grouplayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.dyno.visual.swing.layouts.Alignment;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Spring;
import org.dyno.visual.swing.layouts.Trailing;

public abstract class Anchor {
	protected JComponent target;

	protected Anchor(JComponent target) {
		this.target = target;
	}

	public abstract Alignment createHoveredAxis(Component me, Rectangle bounds);

	public abstract Alignment createRightAxis(Component me, Rectangle bounds, Alignment lastAxis);

	public abstract Alignment createBottomAxis(Component me, Rectangle bounds, Alignment lastAxis);

	public abstract Alignment createLeftAxis(Component me, Rectangle bounds, Alignment lastAxis);

	public abstract Alignment createTopAxis(Component me, Rectangle bounds, Alignment lastAxis);

	private boolean isOverlap(int start0, int length0, int start1, int length1) {
		return start0 + length0 > start1 && start1 + length1 > start0;
	}

	protected Alignment createVerticalLeading(Component me, Rectangle bounds, Container parent) {
		int min = Integer.MIN_VALUE;
		int pref = Integer.MIN_VALUE;
		int count = parent.getComponentCount();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		LayoutStyle layoutStyle = LayoutStyle.getInstance();
		for (int i = 0; i < count; i++) {
			Component target = parent.getComponent(i);
			Constraints constraints = layout.getConstraints(target);
			Rectangle targetBounds = target.getBounds();
			if (constraints != null && isOverlap(bounds.x, bounds.width, targetBounds.x, targetBounds.width)) {
				Alignment vertical = constraints.getVertical();
				Dimension prefs = target.getPreferredSize();
				Dimension mins = target.getMinimumSize();
				if (vertical instanceof Trailing) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.UNRELATED, SwingConstants.NORTH, parent);
					Trailing trailing = (Trailing) vertical;
					int pref_size = trailing.getSize() == Alignment.PREFERRED ? prefs.height : trailing.getSize();
					int min_size = pref_size > mins.height ? mins.height : pref_size;
					int actual = gap + min_size + trailing.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + pref_size + trailing.getTrailing();
					if (actual > pref)
						pref = actual;
				} else if (vertical instanceof Bilateral) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.RELATED, SwingConstants.NORTH, parent);
					Bilateral bilateral = (Bilateral) vertical;
					int actual = gap + mins.height + bilateral.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + prefs.height + bilateral.getTrailing();
					if (actual > pref)
						pref = actual;
				}
			}
		}
		int gap = layoutStyle.getContainerGap((JComponent) me, SwingConstants.SOUTH, parent);
		if (gap > min)
			min = gap;
		if (gap > pref)
			pref = gap;
		Spring spring = new Spring(min, pref);
		Insets insets = parent.getInsets();
		return new Leading(bounds.y - insets.top, bounds.height, spring);
	}

	protected Alignment createVerticalTrailing(Component me, Rectangle bounds, Container parent) {
		int min = Integer.MIN_VALUE;
		int pref = Integer.MIN_VALUE;
		int count = parent.getComponentCount();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		LayoutStyle layoutStyle = LayoutStyle.getInstance();
		for (int i = 0; i < count; i++) {
			Component target = parent.getComponent(i);
			Constraints constraints = layout.getConstraints(target);
			Rectangle targetBounds = target.getBounds();
			if (constraints != null && isOverlap(bounds.x, bounds.width, targetBounds.x, targetBounds.width)) {
				Alignment vertical = constraints.getVertical();
				Dimension prefs = target.getPreferredSize();
				Dimension mins = target.getMinimumSize();
				if (vertical instanceof Leading) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.UNRELATED, SwingConstants.SOUTH, parent);
					Leading leading = (Leading) vertical;
					int pref_size = leading.getSize() == Alignment.PREFERRED ? prefs.height : leading.getSize();
					int min_size = pref_size > mins.height ? mins.height : pref_size;
					int actual = gap + min_size + leading.getLeading();
					if (actual > min)
						min = actual;
					actual = gap + pref_size + leading.getLeading();
					if (actual > pref)
						pref = actual;
				} else if (vertical instanceof Bilateral) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.RELATED, SwingConstants.SOUTH, parent);
					Bilateral bilateral = (Bilateral) vertical;
					int actual = gap + mins.height + bilateral.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + prefs.height + bilateral.getTrailing();
					if (actual > pref)
						pref = actual;
				}
			}
		}
		int gap = layoutStyle.getContainerGap((JComponent) me, SwingConstants.NORTH, parent);
		if (gap > min)
			min = gap;
		if (gap > pref)
			pref = gap;
		Spring spring = new Spring(min, pref);
		Insets insets = parent.getInsets();
		int h = parent.getHeight();
		return new Trailing(h - bounds.y - bounds.height - insets.bottom, bounds.height, spring);
	}

	protected Alignment createVerticalSpring(Component me, Rectangle bounds, Container parent) {
		int h = parent.getHeight();
		Insets insets = parent.getInsets();
		int min = me.getMinimumSize().height;
		int pref = me.getPreferredSize().height;
		Spring spring = new Spring(min, pref);
		return new Bilateral(bounds.y - insets.top, h - bounds.y - bounds.height - insets.bottom, spring);
	}

	protected Alignment createHorizontalLeading(Component me, Rectangle bounds, Container parent) {
		int min = Integer.MIN_VALUE;
		int pref = Integer.MIN_VALUE;
		int count = parent.getComponentCount();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		LayoutStyle layoutStyle = LayoutStyle.getInstance();
		for (int i = 0; i < count; i++) {
			Component target = parent.getComponent(i);
			Constraints constraints = layout.getConstraints(target);
			Rectangle targetBounds = target.getBounds();
			if (constraints != null && isOverlap(bounds.y, bounds.height, targetBounds.y, targetBounds.height)) {
				Alignment horizontal = constraints.getHorizontal();
				Dimension prefs = target.getPreferredSize();
				Dimension mins = target.getMinimumSize();
				if (horizontal instanceof Trailing) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.UNRELATED, SwingConstants.WEST, parent);
					Trailing trailing = (Trailing) horizontal;
					int pref_size = trailing.getSize() == Alignment.PREFERRED ? prefs.width : trailing.getSize();
					int min_size = pref_size > mins.width ? mins.width : pref_size;
					int actual = gap + min_size + trailing.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + pref_size + trailing.getTrailing();
					if (actual > pref)
						pref = actual;
				} else if (horizontal instanceof Bilateral) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.RELATED, SwingConstants.WEST, parent);
					Bilateral bilateral = (Bilateral) horizontal;
					int actual = gap + mins.width + bilateral.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + prefs.width + bilateral.getTrailing();
					if (actual > pref)
						pref = actual;
				}
			}
		}
		int gap = layoutStyle.getContainerGap((JComponent) me, SwingConstants.EAST, parent);
		if (gap > min)
			min = gap;
		if (gap > pref)
			pref = gap;
		Spring spring = new Spring(min, pref);
		Insets insets = parent.getInsets();
		return new Leading(bounds.x - insets.left, bounds.width, spring);
	}

	protected Alignment createHorizontalTrailing(Component me, Rectangle bounds, Container parent) {
		int min = Integer.MIN_VALUE;
		int pref = Integer.MIN_VALUE;
		int count = parent.getComponentCount();
		GroupLayout layout = (GroupLayout) parent.getLayout();
		LayoutStyle layoutStyle = LayoutStyle.getInstance();
		for (int i = 0; i < count; i++) {
			Component target = parent.getComponent(i);
			Constraints constraints = layout.getConstraints(target);
			Rectangle targetBounds = target.getBounds();
			if (constraints != null && isOverlap(bounds.y, bounds.height, targetBounds.y, targetBounds.height)) {
				Alignment horizontal = constraints.getHorizontal();
				Dimension prefs = target.getPreferredSize();
				Dimension mins = target.getMinimumSize();
				if (horizontal instanceof Leading) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.UNRELATED, SwingConstants.EAST, parent);
					Leading leading = (Leading) horizontal;
					int pref_size = leading.getSize() == Alignment.PREFERRED ? prefs.width : leading.getSize();
					int min_size = pref_size > mins.width ? mins.width : pref_size;
					int actual = gap + min_size + leading.getLeading();
					if (actual > min)
						min = actual;
					actual = gap + pref_size + leading.getLeading();
					if (actual > pref)
						pref = actual;
				} else if (horizontal instanceof Bilateral) {
					int gap = layoutStyle.getPreferredGap((JComponent) target, (JComponent) me, ComponentPlacement.RELATED, SwingConstants.EAST, parent);
					Bilateral bilateral = (Bilateral) horizontal;
					int actual = gap + mins.width + bilateral.getTrailing();
					if (actual > min)
						min = actual;
					actual = gap + prefs.width + bilateral.getTrailing();
					if (actual > pref)
						pref = actual;
				}
			}
		}
		int gap = layoutStyle.getContainerGap((JComponent) me, SwingConstants.WEST, parent);
		if (gap > min)
			min = gap;
		if (gap > pref)
			pref = gap;
		Spring spring = new Spring(min, pref);
		Insets insets = parent.getInsets();
		int w = parent.getWidth();
		return new Trailing(w - bounds.x - bounds.width - insets.right, bounds.width, spring);
	}

	protected Alignment createHorizontalSpring(Component me, Rectangle bounds, Container parent) {
		int w = parent.getWidth();
		Insets insets = parent.getInsets();
		int min = me.getMinimumSize().width;
		int pref = me.getPreferredSize().width;
		Spring spring = new Spring(min, pref);
		return new Bilateral(bounds.x - insets.left, w - bounds.x - bounds.width - insets.right, spring);
	}
}

