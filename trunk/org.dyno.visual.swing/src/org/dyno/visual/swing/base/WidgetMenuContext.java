package org.dyno.visual.swing.base;

import java.awt.Component;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.dyno.visual.swing.editors.actions.AddEventAction;
import org.dyno.visual.swing.editors.actions.DelEventAction;
import org.dyno.visual.swing.editors.actions.VarChangeAction;
import org.dyno.visual.swing.plugin.spi.BorderAdapter;
import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdaptableContext;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.IMenuContext;
import org.dyno.visual.swing.plugin.spi.Messages;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;

public class WidgetMenuContext implements IMenuContext, IAdaptableContext {

	protected void fillAddEventAction(MenuManager eventMenu) {
		EventSetDescriptor[] esds = adaptable.getBeanInfo()
				.getEventSetDescriptors();
		for (EventSetDescriptor esd : esds) {
			MenuManager subEventMenu = new MenuManager(esd.getName(),
					"#ADD_EVENT_" + esd.getName()); //$NON-NLS-1$
			MethodDescriptor[] eds = esd.getListenerMethodDescriptors();
			for (MethodDescriptor md : eds) {
				subEventMenu.add(new AddEventAction(adaptable, esd, md));
			}
			eventMenu.add(subEventMenu);
		}
	}

	public void fillContextAction(MenuManager menu) {
		if (!adaptable.isRoot())
			menu.add(new VarChangeAction(adaptable));
		MenuManager eventMenu = new MenuManager(
				Messages.WidgetAdapter_Add_Edit_Events, "#EVENT"); //$NON-NLS-2$
		fillAddEventAction(eventMenu);
		menu.add(eventMenu);
		MenuManager delEventMenu = new MenuManager(
				Messages.WidgetAdapter_Delete_Events, "#DELETE_EVENT"); //$NON-NLS-1$
		fillDelEventAction(delEventMenu);
		menu.add(delEventMenu);
		MenuManager borderMenu = new MenuManager(Messages.WidgetAdapter_Border,
				"#BORDER"); //$NON-NLS-2$
		fillBorderAction(borderMenu);
		menu.add(borderMenu);
		if (!adaptable.isRoot()) {
			CompositeAdapter parentAdapter = adaptable.getParentAdapter();
			if (parentAdapter != null) {
				WidgetMenuContext context = (WidgetMenuContext) parentAdapter
						.getAdapter(IMenuContext.class);
				if (context != null)
					context.fillConstraintsAction(menu, adaptable.getWidget());
			}
		}
	}
	public void fillConstraintsAction(MenuManager menu, Component widget) {
	}
	private void fillBorderAction(MenuManager borderMenu) {
		List<BorderAdapter> list = BorderAdapter.getBorderList();

		for (BorderAdapter adapter : list) {
			if (adaptable.getWidget() instanceof JComponent) {
				IAction action = adapter
						.getContextAction((JComponent) adaptable.getWidget());
				borderMenu.add(action);
			}
		}
	}

	protected void fillDelEventAction(MenuManager eventMenu) {
		Set<EventSetDescriptor> keys = adaptable.getEventDescriptor().keySet();
		for (EventSetDescriptor key : keys) {
			MenuManager subEventMenu = new MenuManager(key.getName(),
					"#DELETE_EVENT_" + key); //$NON-NLS-1$
			IEventListenerModel model = adaptable.getEventDescriptor().get(key);
			Iterable<MethodDescriptor> mSet = model.methods();
			for (MethodDescriptor method : mSet) {
				subEventMenu.add(new DelEventAction(adaptable, key, method));
			}
			eventMenu.add(subEventMenu);
		}
	}

	@Override
	public void setAdaptable(IAdaptable adaptable) {
		this.adaptable = (WidgetAdapter) adaptable;
	}

	protected WidgetAdapter adaptable;
}
