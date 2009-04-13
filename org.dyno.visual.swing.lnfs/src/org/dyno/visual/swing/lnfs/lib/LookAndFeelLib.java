package org.dyno.visual.swing.lnfs.lib;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.LookAndFeel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dyno.visual.swing.base.ExtensionRegistry;
import org.dyno.visual.swing.base.JavaUtil;
import org.dyno.visual.swing.base.TypeAdapter;
import org.dyno.visual.swing.lnfs.LnfPlugin;
import org.dyno.visual.swing.plugin.spi.ILookAndFeelAdapter;
import org.dyno.visual.swing.plugin.spi.ISystemValue;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@SuppressWarnings("unchecked")
public class LookAndFeelLib implements IClasspathContainer, ILookAndFeelAdapter {
	private static final String LAF_FILE = "laf.xml"; //$NON-NLS-1$
	private static Map<String, LookAndFeelLib> lnfLibs = new HashMap<String, LookAndFeelLib>();
	private static class SystemValue implements ISystemValue{
		private static final long serialVersionUID = 1L;
	}
	private static SystemValue SYSTEM_VALUE=new SystemValue();

	public static LookAndFeelLib getLnfLib(String dir) {
		LookAndFeelLib icc = lnfLibs.get(dir);
		if (icc == null) {
			icc = new LookAndFeelLib(dir);
			lnfLibs.put(dir, icc);
		}
		return icc;
	}

	private String libName;
	private IPath libPath;
	private IPath[] libJars;
	private IPath[] libSrcs;
	private String lnfClassname;
	private Map<String, Map<String, Object>> values;
	private LookAndFeel lnfInstance;
 
	private LookAndFeelLib(String dir) {
		this.libPath = JavaUtil.VS_LIBRARY.append(
				LookAndFeelLibrary.LOOK_AND_FEEL_EXT).append(dir);
		IPath path = getLafLibDir();
		path = path.append(dir);
		initLaf(path);
	}
	public static IPath getLafLibDir(){
		IPath path = Platform.getLocation();
		path = path.append(".metadata"); //$NON-NLS-1$
		path = path.append(".plugins"); //$NON-NLS-1$
		path = path.append(LnfPlugin.getPluginID());
		path = path.append(LookAndFeelLibrary.LOOK_AND_FEEL_LIB_DIR);
		return path;
	}
	private void initLaf(IPath path) {
		IPath laf_path = path.append(LAF_FILE);
		File laf_file = laf_path.toFile();
		if (laf_file.exists()) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder newDocumentBuilder = factory
						.newDocumentBuilder();
				Document document = newDocumentBuilder.parse(laf_file);
				Element root = document.getDocumentElement();
				this.libName = root.getAttribute("name"); //$NON-NLS-1$
				this.lnfClassname = root.getAttribute("class"); //$NON-NLS-1$
				NodeList nodes = root.getElementsByTagName("classpath"); //$NON-NLS-1$
				if(nodes!=null&&nodes.getLength()>0){
					List<IPath>jars=new ArrayList<IPath>();
					List<IPath>srcs=new ArrayList<IPath>();
					for(int i=0;i<nodes.getLength();i++){
						Element classpathNode=(Element)nodes.item(i);
						String strJar = classpathNode.getAttribute("jar"); //$NON-NLS-1$
						String strSrc = classpathNode.getAttribute("src"); //$NON-NLS-1$
						jars.add(path.append(strJar));
						if(strSrc!=null&&strSrc.trim().length()>0){
							srcs.add(path.append(strSrc));
						}else{
							srcs.add(null);
						}
					}
					this.libJars = new IPath[jars.size()];
					this.libSrcs = new IPath[srcs.size()];
					jars.toArray(libJars);
					srcs.toArray(libSrcs);
				}else{
					this.libJars = new IPath[0];
					this.libSrcs = new IPath[0];
				}
				values = new HashMap<String, Map<String, Object>>();
				nodes = root.getElementsByTagName("component"); //$NON-NLS-1$
				if (nodes != null && nodes.getLength() > 0) {
					for (int i = 0; i < nodes.getLength(); i++) {
						Element node = (Element) nodes.item(i);
						extractComponentValue(node);
					}
				}
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
		} else {
			throw new IllegalArgumentException(Messages.LookAndFeelLib_Laf_Xml_Not_Found
					+ path.toString());
		}
	}

	private void extractComponentValue(Element component)
			throws ClassNotFoundException, IntrospectionException {
		String strClass = component.getAttribute("class"); //$NON-NLS-1$
		Class clazz = Class.forName(strClass);
		Map<String, Object> compValues = values.get(strClass);
		if (compValues == null) {
			compValues = new HashMap<String, Object>();
			values.put(strClass, compValues);
		}
		NodeList nodes = component.getElementsByTagName("property"); //$NON-NLS-1$
		if (nodes != null && nodes.getLength() > 0) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Element node = (Element) nodes.item(i);
				extractPropertyValue(compValues, node, clazz);
			}
		}
	}

	private void extractPropertyValue(Map<String, Object> values, Element node,
			Class clazz) throws IntrospectionException {
		String name = node.getAttribute("name"); //$NON-NLS-1$
		String value = node.getAttribute("default"); //$NON-NLS-1$
		if (value != null && value.trim().length() > 0&&!value.equals("null")) { //$NON-NLS-1$
			if(value.equals("SYSTEM_VALUE")){ //$NON-NLS-1$
				values.put(name, SYSTEM_VALUE);
			} else {
				PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
				Class type = pd.getPropertyType();
				TypeAdapter ta = ExtensionRegistry.getTypeAdapter(type);
				if (ta != null && ta.getEndec() != null) {
					Object v = ta.getEndec().decode(value);
					values.put(name, v);
				}
			}
		}
	}
	
	public IClasspathEntry[] getClasspathEntries() {
		IClasspathEntry[] libs=new IClasspathEntry[libJars.length];
		for(int i=0;i<libs.length;i++)
			libs[i]=JavaCore.newLibraryEntry(libJars[i], libSrcs[i], null, true);
		return libs;
	}

	public String getDescription() {
		return libName;
	}

	
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	
	public IPath getPath() {
		return libPath;
	}

	
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (o instanceof LookAndFeelLib) {
			LookAndFeelLib lnfLib = (LookAndFeelLib) o;
			return lnfLib.libPath.equals(libPath);
		} else
			return false;
	}

	
	public Object getDefaultValue(Class widgetClass, String propertyName) {
		Map<String, Object> map = values.get(widgetClass.getName());
		if (map != null) {
			return map.get(propertyName);
		}
		return null;
	}

	
	public LookAndFeel getLookAndFeelInstance() {
		if (lnfInstance == null) {
			try {
				URL[]urls=new URL[libJars.length];
				for(int i=0;i<libJars.length;i++){
					urls[i]=libJars[i].toFile().toURI().toURL();
				}
				URLClassLoader urlLoader = new URLClassLoader(urls,	getClass().getClassLoader());
				Class lnfClass = urlLoader.loadClass(lnfClassname);
				lnfInstance = (LookAndFeel) lnfClass.newInstance();
				lnfInstance = new DelegateLookAndFeel(lnfInstance);
			} catch (Exception e) {
				LnfPlugin.getLogger().error(e);
			}
		}
		return lnfInstance;
	}
	
	public String getClassname() {
		return lnfClassname;
	}

	
	public String getName() {
		return libName;
	}
}
