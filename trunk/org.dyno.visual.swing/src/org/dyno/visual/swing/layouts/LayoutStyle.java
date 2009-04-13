package org.dyno.visual.swing.layouts;

import java.awt.Container;
import java.lang.reflect.Method;

import javax.swing.JComponent;

public abstract class LayoutStyle {
	private static class DefaultLayoutStyle extends LayoutStyle {
		@Override
		public int getContainerGap(JComponent component, int position, Container parent) {
			return 12;
		}

		@Override
		public int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent) {
			switch (type) {
			case RELATED:
				return 6;
			case UNRELATED:
				return 12;
			case INDENT:
				return 12;
			}
			return 12;
		}
	}

	@SuppressWarnings("unchecked")
	private static class Java6LayoutStyle extends LayoutStyle {
		private static Class java6LayoutStyleClass;
		private static Class java6ComponentPlacementClass;

		private Object style;
		private static Method getContainerGap;
		private static Method getPreferredGap;
		private static Method valueOf;

		public Java6LayoutStyle() {
			if (java6LayoutStyleClass == null) {
				try {
					java6LayoutStyleClass = Class.forName("javax.swing.LayoutStyle");
					java6ComponentPlacementClass = Class.forName("javax.swing.LayoutStyle$ComponentPlacement");
					getContainerGap = java6LayoutStyleClass.getMethod("getContainerGap", JComponent.class, int.class, Container.class);
					getPreferredGap = java6LayoutStyleClass.getMethod("getPreferredGap", JComponent.class, JComponent.class, java6ComponentPlacementClass, int.class, Container.class);
					valueOf = java6ComponentPlacementClass.getMethod("valueOf", String.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Method method = java6LayoutStyleClass.getMethod("getInstance");
				style = method.invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getContainerGap(JComponent component, int position, Container parent) {
			try {
				return ((Integer) getContainerGap.invoke(style, component, position, parent));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 12;
		}

		@Override
		public int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent) {
			try {
				Object enumType = valueOf.invoke(null, type.name());
				return ((Integer) getPreferredGap.invoke(style, component1, component2, enumType, position, parent));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 12;
		}
	}

	private static LayoutStyle singleton5 = new DefaultLayoutStyle();
	private static LayoutStyle singleton6;

	public static LayoutStyle getInstance() {
		String version = System.getProperty("java.version");
		if (version.startsWith("1.6")) {
			if (singleton6 == null)
				singleton6 = new Java6LayoutStyle();
			return singleton6;
		} else
			return singleton5;
	}

	public enum ComponentPlacement {
		RELATED, UNRELATED, INDENT;
	}

	public abstract int getContainerGap(JComponent component, int position, Container parent);

	public abstract int getPreferredGap(JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent);
}
