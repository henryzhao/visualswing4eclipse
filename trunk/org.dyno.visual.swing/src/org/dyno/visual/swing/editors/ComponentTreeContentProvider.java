package org.dyno.visual.swing.editors;

import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ComponentTreeContentProvider implements ITreeContentProvider {
	@Override
	public Object[] getChildren(Object parentElement) {
		if (root != null) {
			if (parentElement == root) {
				return new Object[] { root.getDesigner() };
			} else if (parentElement == root.getDesigner()) {
				return new Object[] { otherComponents, root.getDesigner().getRoot() };
			} else if (parentElement == otherComponents) {
				List<?> children = root.getInvisibles();
				Object[] values = new Object[children == null ? 0 : children.size()];
				if (children != null)
					children.toArray(values);
				return values;
			} else if (parentElement instanceof JComponent) {
				JComponent component = (JComponent) parentElement;
				WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(component);
				Map<EventSetDescriptor, Map<MethodDescriptor, String>> events = adapter.getEventDescriptor();
				Set<EventSetDescriptor> keys = events.keySet();
				List<Object> children = new ArrayList<Object>();
				if (!keys.isEmpty()) {
					EventDesc ed = new EventDesc(component);
					List<EventSet> eventSets = new ArrayList<EventSet>();
					for (EventSetDescriptor key : keys) {
						EventSet set = new EventSet(key.getDisplayName(), ed);
						eventSets.add(set);
						Map<MethodDescriptor, String> map = events.get(key);
						Set<MethodDescriptor> mKey = map.keySet();
						List<EventMethod> mlist = new ArrayList<EventMethod>();
						for (MethodDescriptor m : mKey) {
							String displayName=map.get(m);
							EventMethod method = new EventMethod(m.getDisplayName()+"["+displayName+"]", set);
							mlist.add(method);
						}
						set.setMethods(mlist);
					}
					ed.setEventSets(eventSets);
					children.add(ed);
				}
				if (adapter instanceof CompositeAdapter) {
					CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
					int count = compositeAdapter.getChildCount();
					if (count > 0) {
						for (int i = 0; i < count; i++) {
							children.add(compositeAdapter.getChild(i));
						}
					}
				}
				Object[] subs = new Object[children.size()];
				return children.toArray(subs);
			} else if (parentElement instanceof EventDesc) {
				EventDesc desc = (EventDesc) parentElement;
				List<EventSet> list = desc.getEventSets();
				Object[] children = new Object[list.size()];
				return list.toArray(children);
			} else if (parentElement instanceof EventSet) {
				EventSet set = (EventSet) parentElement;
				List<EventMethod> list = set.getMethods();
				Object[]children = new Object[list.size()];
				return list.toArray(children);
			}
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element == root) {
			return null;
		} else if (element == root.getDesigner()) {
			return root;
		} else if (element == otherComponents) {
			return root.getDesigner();
		} else if (element instanceof JComponent) {
			JComponent child = (JComponent) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			if (adapter.isRoot())
				return root.getDesigner();
			else {
				WidgetAdapter parentAdapter = adapter.getParentAdapter();
				if (parentAdapter == null)
					return root.getDesigner();
				return parentAdapter.getWidget();
			}
		} else if (element instanceof EventDesc){
			EventDesc desc = (EventDesc)element;
			return desc.getWidget();
		} else if(element instanceof EventSet){
			EventSet set = (EventSet)element;
			return set.getParent();
		} else if(element instanceof EventMethod){
			EventMethod m = (EventMethod)element;
			return m.getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element == root) {
			return true;
		} else if (element == root.getDesigner()) {
			return true;
		} else if (element instanceof JComponent) {
			JComponent child = (JComponent) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			Map<EventSetDescriptor, Map<MethodDescriptor, String>> events = adapter.getEventDescriptor();
			Set<EventSetDescriptor> keys = events.keySet();
			if (!keys.isEmpty()) {
				return true;
			}
			if (adapter instanceof CompositeAdapter) {
				CompositeAdapter compositeAdapter = (CompositeAdapter) adapter;
				int count = compositeAdapter.getChildCount();
				return count > 0;
			}
		} else if (element == otherComponents) {
			List<?> invisibles = root.getInvisibles();
			return invisibles == null ? false : invisibles.size() > 0;
		} else if(element instanceof EventDesc) {
			EventDesc eDesc=(EventDesc)element;
			return eDesc.getEventSets().size()>0;
		} else if(element instanceof EventSet){
			EventSet eSet = (EventSet)element;
			return eSet.getMethods().size()>0;
		} 
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof ComponentTreeInput) {
			this.root = (ComponentTreeInput) newInput;
		}
	}

	private ComponentTreeInput root;
	private String otherComponents = "Other Components";
}
