package org.dyno.visual.swing.widgets;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;

import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.IContextMenuCustomizer;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

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
			boolean hasGroup=false;
			for (InvisibleAdapter invisibleAdapter : invisibles) {
				if (invisibleAdapter instanceof ButtonGroupAdapter) {
					subMenu.add(new AddButtonGroupAction(rootAdapter, (ButtonGroupAdapter) invisibleAdapter, selected));
					hasGroup=true;
				}
			}
			if(hasGroup)
				subMenu.add(new Separator());
			subMenu.add(new AddToNewButtonGroup(selected, rootAdapter));
			menuManager.add(subMenu);
		}
	}

	class AddButtonGroupAction extends Action {
		private ButtonGroupAdapter buttonGroup;
		private List<Component> selectedButtons;
		private WidgetAdapter root;
		public AddButtonGroupAction(WidgetAdapter root, ButtonGroupAdapter buttonGroup, List<Component> selected) {
			setText(buttonGroup.getName());
			this.buttonGroup = buttonGroup;
			this.selectedButtons = selected;
			this.root = root;
		}

		public void run() {
			if (selectedButtons.isEmpty())
				return;
			for (Component comp : selectedButtons) {
				buttonGroup.getButtonGroup().add((AbstractButton) comp);
			}
			root.setDirty(true);
			root.changeNotify();
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
	public void fillInvisibleAdapterMenu(MenuManager menuManager, WidgetAdapter rootAdapter, List<InvisibleAdapter> selected) {
		if (isAllButtonGroupAdapter(selected)) {
			if(selected.size()==1){
				menuManager.add(new ButtonGroupRenamingAction(rootAdapter, (ButtonGroupAdapter)selected.get(0)));
			}
			menuManager.add(new DeleteButtonGroup(rootAdapter, selected));
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
			root.setDirty(true);
			root.addNotify();
		}
	}
	class AddToNewButtonGroup extends Action {
		private WidgetAdapter root;
		private List<Component> selectedButtons;

		public AddToNewButtonGroup(List<Component> selected, WidgetAdapter root) {
			super("Add to new button group");
			this.root = root;
			this.selectedButtons = selected;
		}

		@Override
		public void run() {
			ButtonGroupAdapter buttonGroup = new ButtonGroupAdapter();
			root.getInvisibles().add(buttonGroup);
			if (selectedButtons.isEmpty())
				return;
			for (Component comp : selectedButtons) {
				buttonGroup.getButtonGroup().add((AbstractButton) comp);
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
			root.setDirty(true);
			root.changeNotify();
		}
	}
}
