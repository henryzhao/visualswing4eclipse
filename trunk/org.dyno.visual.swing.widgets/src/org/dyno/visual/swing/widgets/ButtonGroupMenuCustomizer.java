package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;

import org.dyno.visual.swing.plugin.spi.IContextMenuCustomizer;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;

public class ButtonGroupMenuCustomizer implements IContextMenuCustomizer {
	private boolean isAllButton(List<Component> selected) {
		for (Component comp : selected) {
			if (!(comp instanceof AbstractButton))
				return false;
		}
		return true;
	}

	@Override
	public void fillContextMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<Component> selected) {
		if (isAllButton(selected)) {
			MenuManager subMenu = new MenuManager("Add to button group",
					"#ADD_TO_BUTTON_GROUP");
			List<InvisibleAdapter> invisibles = rootAdapter.getInvisibles();
			for (InvisibleAdapter invisibleAdapter : invisibles) {
				if (invisibleAdapter instanceof ButtonGroupAdapter) {
					subMenu.add(new AddButtonGroupAction(
							(ButtonGroupAdapter) invisibleAdapter, selected));
				}
			}
			menuManager.add(subMenu);
		}
	}

	class AddButtonGroupAction extends Action {
		private ButtonGroupAdapter buttonGroup;
		private List<Component> selectedButtons;

		public AddButtonGroupAction(ButtonGroupAdapter buttonGroup,
				List<Component> selected) {
			setText(buttonGroup.getName());
			this.buttonGroup = buttonGroup;
			this.selectedButtons = selected;
		}

		public void run() {
			if (selectedButtons.isEmpty())
				return;
			for (Component comp : selectedButtons) {
				buttonGroup.getButtonGroup().add((AbstractButton) comp);
			}
		}
	}

	private boolean isAllButtonGroupAdapter(List<InvisibleAdapter> selected) {
		for (InvisibleAdapter adapter : selected) {
			if (!(adapter instanceof ButtonGroupAdapter)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void fillInvisibleAdapterMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter, List<InvisibleAdapter> selected) {
		if (isAllButtonGroupAdapter(selected)) {
			menuManager.add(new DeleteButtonGroup(rootAdapter, selected));
			if(selected.size()==1){
				
			}
		}
	}

	@Override
	public void fillInvisibleRootMenu(MenuManager menuManager,
			WidgetAdapter rootAdapter) {
		menuManager.add(new AddButtonGroup(rootAdapter));
	}

	class AddButtonGroup extends Action {
		private WidgetAdapter root;

		public AddButtonGroup(WidgetAdapter root) {
			super("Add new button group");
			this.root = root;
		}

		@Override
		public void run() {
			root.getInvisibles().add(new ButtonGroupAdapter());
			root.addNotify();
		}
	}

	class DeleteButtonGroup extends Action {
		private WidgetAdapter root;
		private List<InvisibleAdapter> adapters;

		public DeleteButtonGroup(WidgetAdapter root,
				List<InvisibleAdapter> adapters) {
			super("Delete button group");
			this.root = root;
			this.adapters = new ArrayList<InvisibleAdapter>();
			for(InvisibleAdapter adapter:adapters){
				this.adapters.add(adapter);
			}
		}

		@Override
		public void run() {
			for (InvisibleAdapter adapter : adapters) {
				root.getInvisibles().remove(adapter);
			}
			root.changeNotify();
		}
	}
}
