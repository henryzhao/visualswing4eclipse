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

package org.dyno.visual.swing.editors;

import java.awt.Component;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dyno.visual.swing.plugin.spi.CompositeAdapter;
import org.dyno.visual.swing.plugin.spi.IAdapter;
import org.dyno.visual.swing.plugin.spi.IEventListenerModel;
import org.dyno.visual.swing.plugin.spi.InvisibleAdapter;
import org.dyno.visual.swing.plugin.spi.WidgetAdapter;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * ComponentTreeContentProvider
 * 
 * @version 1.0.0, 2008-7-3
 * @author William Chen
 */
@SuppressWarnings("unchecked")
public class ComponentTreeContentProvider implements ITreeContentProvider {
	
	@Override
	public Object[] getChildren(Object parentElement) {
		if (root != null) {
			if (parentElement == root) {
				return new Object[] { root.getDesigner() };
			} else if (parentElement == root.getDesigner()) {
				Component rootComp = root.getDesigner().getRoot();
				if (rootComp != null) {
					WidgetAdapter adapter = WidgetAdapter
							.getWidgetAdapter(rootComp);
					if (adapter != null)
						return new Object[] { otherComponents,
								adapter.getWidget() };
					else
						return new Object[] { otherComponents };
				} else {
					return new Object[] { otherComponents };
				}
			} else if (parentElement == otherComponents) {
				List<InvisibleAdapter> children = root.getInvisibles();
				Object[] values = new Object[children == null ? 0 : children
						.size()];
				if (children != null)
					children.toArray(values);
				return values;
			} else if (parentElement instanceof Component) {
				Component component = (Component) parentElement;
				WidgetAdapter adapter = WidgetAdapter
						.getWidgetAdapter(component);
				Map<EventSetDescriptor, IEventListenerModel> events = adapter
						.getEventDescriptor();
				Set<EventSetDescriptor> keys = events.keySet();
				List<Object> children = new ArrayList<Object>();
				if (!keys.isEmpty()) {
					EventDesc ed = new EventDesc(component);
					List<EventSet> eventSets = new ArrayList<EventSet>();
					for (EventSetDescriptor key : keys) {
						EventSet set = new EventSet(key, key.getDisplayName(),
								ed);
						eventSets.add(set);
						IEventListenerModel model = events.get(key);
						List<EventMethod> mlist = new ArrayList<EventMethod>();
						for (MethodDescriptor mthd : model.methods()) {
							EventMethod method = new EventMethod(mthd, model
									.getDisplayName(mthd), set);
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
				Object[] children = new Object[list.size()];
				return list.toArray(children);
			} else if (parentElement instanceof InvisibleAdapter) {
				List children = ((InvisibleAdapter) parentElement)
						.getElements();
				if (children != null)
					return children.toArray();
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
		} else if (element instanceof Component) {
			Component child = (Component) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			if (adapter.isRoot())
				return root.getDesigner();
			else {
				WidgetAdapter parentAdapter = adapter.getParentAdapter();
				if (parentAdapter == null)
					return root.getDesigner();
				return parentAdapter.getWidget();
			}
		} else if (element instanceof EventDesc) {
			EventDesc desc = (EventDesc) element;
			return desc.getWidget();
		} else if (element instanceof EventSet) {
			EventSet set = (EventSet) element;
			return set.getParent();
		} else if (element instanceof EventMethod) {
			EventMethod m = (EventMethod) element;
			return m.getParent();
		} else if (element instanceof InvisibleAdapter) {
			return otherComponents;
		} else if (element instanceof IAdapter) {
			return ((IAdapter) element).getParent();
		}
		return null;
	}

	
	@Override
	public boolean hasChildren(Object element) {
		if (element == root) {
			return true;
		} else if (element == root.getDesigner()) {
			return true;
		} else if (element instanceof Component) {
			Component child = (Component) element;
			WidgetAdapter adapter = WidgetAdapter.getWidgetAdapter(child);
			Map<EventSetDescriptor, IEventListenerModel> events = adapter
					.getEventDescriptor();
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
			List<InvisibleAdapter> invisibles = root.getInvisibles();
			return invisibles == null ? false : invisibles.size() > 0;
		} else if (element instanceof EventDesc) {
			EventDesc eDesc = (EventDesc) element;
			return eDesc.getEventSets().size() > 0;
		} else if (element instanceof EventSet) {
			EventSet eSet = (EventSet) element;
			return eSet.getMethods().size() > 0;
		} else if (element instanceof InvisibleAdapter) {
			List list = ((InvisibleAdapter) element).getElements();
			return list != null && !list.isEmpty();
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
	private String otherComponents = Messages.ComponentTreeContentProvider_Other_Components;
}
