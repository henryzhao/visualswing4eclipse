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

package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IContextCustomizer;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

@SuppressWarnings("serial")
public class ButtonGroupCustomizer implements IContextCustomizer {
	private static final String BUTTON_GROUP_ICON = "/icons/button_group_16.png"; //$NON-NLS-1$
	static java.awt.Image BUTTON_GROUP_AWT_ICON_IMAGE;
	static JComponent DUMMY = new JComponent() {
	};
	static {
		URL url = ButtonGroupAdapter.class.getResource(BUTTON_GROUP_ICON);
		BUTTON_GROUP_AWT_ICON_IMAGE = Toolkit.getDefaultToolkit().getImage(url);
		MediaTracker mt = new MediaTracker(DUMMY);
		mt.addImage(BUTTON_GROUP_AWT_ICON_IMAGE, 0);
		while (true) {
			try {
				mt.waitForAll();
			} catch (InterruptedException e) {
			}
			if (mt.checkID(0))
				break;
		}
	}

	private boolean isAllButton(List<Component> selected) {
		if(selected.isEmpty())
			return false;
		for (Component comp : selected) {
			if (!(comp instanceof AbstractButton))
				return false;
		}
		return true;
	}

	
	public void fillContextMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<Component> selected) {
		if (isAllButton(selected)) {
			MenuManager subMenu = new MenuManager(Messages.ButtonGroupCustomizer_Add2_ButtonGroup,
					"#ADD_TO_BUTTON_GROUP"); //$NON-NLS-1$
			List<InvisibleAdapter> invisibles = rootAdapter.getInvisibles();
			boolean hasGroup = false;
			for (InvisibleAdapter invisibleAdapter : invisibles) {
				if (invisibleAdapter instanceof ButtonGroupAdapter) {
					subMenu.add(new AddButtonGroupAction(rootAdapter,
							(ButtonGroupAdapter) invisibleAdapter, selected));
					hasGroup = true;
				}
			}
			if (hasGroup)
				subMenu.add(new Separator());
			subMenu.add(new AddToNewButtonGroup(selected, rootAdapter));
			menuManager.add(subMenu);
		}
	}

	class AddButtonGroupAction extends Action {
		private ButtonGroupAdapter buttonGroup;
		private List<Component> selectedButtons;
		private WidgetAdapter root;

		public AddButtonGroupAction(WidgetAdapter root,
				ButtonGroupAdapter buttonGroup, List<Component> selected) {
			setText(buttonGroup.getName());
			this.buttonGroup = buttonGroup;
			this.selectedButtons = selected;
			this.root = root;
		}

		public void run() {
			if (selectedButtons.isEmpty())
				return;
			ButtonGroup group = buttonGroup.getButtonGroup();
			for (Component comp : selectedButtons) {
				if (!isInGroup(group, comp)){
					AbstractButton aBtn=(AbstractButton)comp;
					removeButtonFromGroup(aBtn);
					buttonGroup.getButtonGroup().add(aBtn);
				}
			}
			root.setDirty(true);
			root.addNotify();
		}
	}

	private boolean isInGroup(ButtonGroup group, Component comp) {
		Enumeration<AbstractButton> elements = group.getElements();
		while (elements.hasMoreElements()) {
			AbstractButton ab = elements.nextElement();
			if (ab == comp)
				return true;
		}
		return false;
	}

	private boolean isAllButtonGroupAdapter(List<InvisibleAdapter> selected) {
		for (InvisibleAdapter adapter : selected) {
			if (!(adapter instanceof ButtonGroupAdapter)) {
				return false;
			}
		}
		return true;
	}

	private void findButtons(WidgetAdapter adapter, List<AbstractButton> buttons) {
		if (adapter.getWidget() instanceof AbstractButton) {
			buttons.add((AbstractButton) adapter.getWidget());
		}
		if (adapter instanceof CompositeAdapter) {
			CompositeAdapter container = (CompositeAdapter) adapter;
			int count = container.getChildCount();
			for (int i = 0; i < count; i++) {
				Component child = container.getChild(i);
				WidgetAdapter childAdapter = WidgetAdapter
						.getWidgetAdapter(child);
				findButtons(childAdapter, buttons);
			}
		}
	}

	
	public void fillInvisibleAdapterMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<InvisibleAdapter> selected) {
		if (isAllButtonGroupAdapter(selected)) {
			menuManager.add(new DeleteButtonGroup(rootAdapter, selected));
			if (selected.size() == 1) {
				ButtonGroupAdapter bga = (ButtonGroupAdapter) selected.get(0);
				menuManager
						.add(new ButtonGroupRenamingAction(rootAdapter, bga));
				ButtonGroup bg = bga.getButtonGroup();
				List<AbstractButton> allButtons = new ArrayList<AbstractButton>();
				findButtons(rootAdapter, allButtons);
				if (bg.getButtonCount() > 0) {
					Enumeration<AbstractButton> elements = bg.getElements();
					MenuManager delMenu = new MenuManager(
							Messages.ButtonGroupCustomizer_Remove_Button_From_This_Group);
					while (elements.hasMoreElements()) {
						AbstractButton aButton = elements.nextElement();
						delMenu.add(new DeleteButtonFromThisGroupAction(
								rootAdapter, aButton, bg));
						allButtons.remove(aButton);
					}
					menuManager.add(delMenu);
				}
				if (!allButtons.isEmpty()) {
					MenuManager addMenu = new MenuManager(
							Messages.ButtonGroupCustomizer_Add_Button_2_This_Group);
					for (AbstractButton aButton : allButtons) {
						addMenu.add(new AddButtonToThisGroupAction(rootAdapter,
								aButton, bg));
					}
					menuManager.add(addMenu);
				}
			}
		}
	}

	class AddButtonToThisGroupAction extends Action {
		private AbstractButton button;
		private ButtonGroup buttonGroup;
		private WidgetAdapter rootAdapter;

		public AddButtonToThisGroupAction(WidgetAdapter rootAdapter,
				AbstractButton button, ButtonGroup bg) {
			this.button = button;
			setText(WidgetAdapter.getWidgetAdapter(button).getName());
			this.buttonGroup = bg;
			this.rootAdapter = rootAdapter;
		}

		
		public void run() {
			if (!isInGroup(buttonGroup, button)) {
				removeButtonFromGroup(button);
				this.buttonGroup.add(button);
				this.rootAdapter.setDirty(true);
				this.rootAdapter.addNotify();
			}
		}
	}
	private void removeButtonFromGroup(AbstractButton button) {
		WidgetAdapter btnAdapter=WidgetAdapter.getWidgetAdapter(button);
		IAdapter iadapter=btnAdapter.getParent();
		if(iadapter!=null&&iadapter instanceof ButtonGroupAdapter){
			((ButtonGroupAdapter)iadapter).getButtonGroup().remove(button);
		}
	}
	class DeleteButtonFromThisGroupAction extends Action {
		private AbstractButton button;
		private ButtonGroup buttonGroup;
		private WidgetAdapter rootAdapter;

		public DeleteButtonFromThisGroupAction(WidgetAdapter rootAdapter,
				AbstractButton button, ButtonGroup bg) {
			this.button = button;
			setText(WidgetAdapter.getWidgetAdapter(button).getName());
			this.buttonGroup = bg;
			this.rootAdapter = rootAdapter;
		}

		
		public void run() {
			if (isInGroup(buttonGroup, button))
				this.buttonGroup.remove(button);
			this.rootAdapter.setDirty(true);
			this.rootAdapter.addNotify();
		}
	}

	
	public void fillInvisibleRootMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter) {
		menuManager.add(new AddButtonGroup(rootAdapter));
	}

	class AddButtonGroup extends Action {
		private WidgetAdapter root;

		public AddButtonGroup(WidgetAdapter root) {
			super(Messages.ButtonGroupCustomizer_Add_New_Button_Group);
			this.root = root;
		}

		
		public void run() {
			ButtonGroupAdapter btnAdapter = new ButtonGroupAdapter(root);
			root.getInvisibles().add(btnAdapter);
			root.setDirty(true);
			root.addNotify();
		}
	}

	class AddToNewButtonGroup extends Action {
		private WidgetAdapter root;
		private List<Component> selectedButtons;

		public AddToNewButtonGroup(List<Component> selected, WidgetAdapter root) {
			super(Messages.ButtonGroupCustomizer_Add_2_New_Button_Group);
			this.root = root;
			this.selectedButtons = selected;
		}

		
		public void run() {
			ButtonGroupAdapter btnAdapter = new ButtonGroupAdapter(root);
			root.getInvisibles().add(btnAdapter);
			btnAdapter.setName(root.getNamespace().nextName(btnAdapter.getBasename()));
			if (selectedButtons.isEmpty())
				return;
			for (Component comp : selectedButtons) {
				AbstractButton aBtn=(AbstractButton) comp;
				removeButtonFromGroup(aBtn);
				btnAdapter.getButtonGroup().add(aBtn);
			}
			root.setDirty(true);
			root.addNotify();
		}
	}

	class DeleteButtonGroup extends Action {
		private WidgetAdapter root;
		private List<InvisibleAdapter> adapters;

		public DeleteButtonGroup(WidgetAdapter root,
				List<InvisibleAdapter> adapters) {
			super(Messages.ButtonGroupCustomizer_Delete_Button_Group);
			this.root = root;
			this.adapters = new ArrayList<InvisibleAdapter>();
			for (InvisibleAdapter adapter : adapters) {
				this.adapters.add(adapter);
			}
		}

		
		public void run() {
			for (InvisibleAdapter adapter : adapters) {
				root.getInvisibles().remove(adapter);
				ButtonGroup bg = ((ButtonGroupAdapter)adapter).getButtonGroup();
				Enumeration<AbstractButton> elements = bg.getElements();
				while(elements.hasMoreElements()){
					AbstractButton ab=elements.nextElement();
					bg.remove(ab);
				}
			}
			root.setDirty(true);
			root.addNotify();
		}
	}

	
	public void fillIAdapterMenu(MenuManager manager,
			WidgetAdapter rootAdapter, List<IAdapter> iadapters) {
		if (isAllAbstractButtonAdapter(iadapters)) {
			manager
					.add(new DeleteButtonFromParentGroup(rootAdapter, iadapters));
		}
	}

	class DeleteButtonFromParentGroup extends Action {
		private WidgetAdapter rootAdapter;
		private List<IAdapter> iadapters;

		public DeleteButtonFromParentGroup(WidgetAdapter rootAdapter,
				List<IAdapter> iadapters) {
			super(Messages.ButtonGroupCustomizer_Remove_From_This_Group);
			this.rootAdapter = rootAdapter;
			this.iadapters = iadapters;
		}

		
		public void run() {
			for (IAdapter iadapter : iadapters) {
				WidgetAdapter wa = (WidgetAdapter) iadapter;
				IAdapter parentAdapter = wa.getParent();
				if (parentAdapter != null
						&& parentAdapter instanceof ButtonGroupAdapter) {
					ButtonGroup bg = ((ButtonGroupAdapter) parentAdapter)
							.getButtonGroup();
					AbstractButton aBtn = (AbstractButton) wa.getWidget();
					if (isInGroup(bg, aBtn))
						bg.remove(aBtn);
				}
			}
			rootAdapter.setDirty(true);
			rootAdapter.addNotify();
		}
	}

	private boolean isAllAbstractButtonAdapter(List<IAdapter> iadapters) {
		for (IAdapter iadapter : iadapters) {
			if (iadapter instanceof WidgetAdapter) {
				Component comp = ((WidgetAdapter) iadapter).getWidget();
				if (!(comp instanceof AbstractButton))
					return false;
				IAdapter parentAdapter=((WidgetAdapter) iadapter).getParent();
				if(parentAdapter==null)
					return false;
				if(!(parentAdapter instanceof ButtonGroupAdapter))
					return false;
			}else{
				return false;
			}
		}
		return true;
	}

	
	public void paintContext(Graphics g, WidgetAdapter rootAdapter) {
		List<Component> selected = rootAdapter.getSelection();
		if (selected != null && !selected.isEmpty()) {
			List<AbstractButton> buttons = new ArrayList<AbstractButton>();
			for (Component comp : selected) {
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(comp);
				if (comp instanceof AbstractButton) {
					IAdapter parent = adapter.getParent();
					if (parent instanceof ButtonGroupAdapter) {
						ButtonGroupAdapter parentAdapter = (ButtonGroupAdapter) parent;
						ButtonGroup bg = parentAdapter.getButtonGroup();
						Enumeration<AbstractButton> elements = bg.getElements();
						while (elements.hasMoreElements()) {
							AbstractButton ab = elements.nextElement();
							if (!buttons.contains(ab)) {
								buttons.add(ab);
							}
						}
					}
				}
			}
			if (!buttons.isEmpty()) {
				for (AbstractButton aButton : buttons) {
					WidgetAdapter bAdapter = WidgetAdapter
							.getWidgetAdapter(aButton);
					Point p = bAdapter.convertToGlobal(new Point(0, 0));
					Rectangle rect = aButton.getBounds();
					rect.x = p.x;
					rect.y = p.y;
					int ih = BUTTON_GROUP_AWT_ICON_IMAGE.getHeight(DUMMY);
					g.drawImage(BUTTON_GROUP_AWT_ICON_IMAGE, rect.x
							+ rect.width + 6,
							rect.y + rect.height / 2 - ih / 2, DUMMY);
				}
			}
		}
	}
}

