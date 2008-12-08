package org.dyno.visual.swing.base;

import org.dyno.visual.swing.VisualSwingPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

public class PluginLogger {
	public static PluginLogger instance;
	static{
		instance = new PluginLogger();
	}
	private Plugin plugin;
	private String pluginID;
	private PluginLogger(){
		this(VisualSwingPlugin.getDefault());
	}
	public PluginLogger(Plugin plugin){
		this.plugin=plugin;
		pluginID=plugin.getBundle().getSymbolicName();
	}
	public ILog getLog(){
		return plugin.getLog();
	}
	public void info(String message){
		log(IStatus.INFO, message);
	}
	public void warning(String message){
		log(IStatus.WARNING, message);
	}
	public void error(String message){
		log(IStatus.ERROR, message);
	}
	public void log(int severity, String message){
		getLog().log(new Status(severity, pluginID, message));
	}
	public void info(Throwable e){
		log(IStatus.INFO, e);
	}
	public void error(Throwable e){
		log(IStatus.ERROR, e);
	}
	public void warning(Throwable e){
		log(IStatus.WARNING, e);
	}
	public void log(int severity, Throwable e){
		getLog().log(new Status(severity, pluginID, e.getMessage(), e));
	}
}
