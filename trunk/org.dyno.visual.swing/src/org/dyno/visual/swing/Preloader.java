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

package org.dyno.visual.swing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class Preloader extends Job {
	public Preloader(){
		super("Preloader");
	}
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		List<String> classes = getPreloadedClasses();
		monitor.setTaskName("Preloading Swing Classes");
		int count = classes.size();
		monitor.beginTask("Loading ...", count);
		for(int i=0;i<count;i++){
			try {
				Class.forName(classes.get(i));
			} catch (ClassNotFoundException e) {
				VisualSwingPlugin.getLogger().error(e);
			}
			monitor.worked(1);
		}
		monitor.done();
		return Status.OK_STATUS;
	}
	private List<String> getPreloadedClasses() {
		BufferedReader br = null;
		List<String> classes = new ArrayList<String>();
		try {			
			InputStream inputStream = getClass().getResourceAsStream("preloaded.txt");
			br = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0) {
					classes.add(line);
				}
			}
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					VisualSwingPlugin.getLogger().error(e);
				}
			}
		}
		return classes;
	}
}

