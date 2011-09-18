package org.noname.designer.core.internal.contenttypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;

public class NonameContentType implements ITextContentDescriber,
		IExecutableExtension {
	private static final String VISUAL_SWING_MAGIC = "VS4E";
	private static String VISUAL_SWING_CLASS_TAILER = ".*//\\s*"
			+ VISUAL_SWING_MAGIC + ".*";
	public static final QualifiedName COMPONENT_TYPE = new QualifiedName(
			"org.noname.designer.core", "Component");
	private static QualifiedName[] QUALIFIERS = new QualifiedName[] { COMPONENT_TYPE };

	private static class PatternItem {
		public String type;
		public String value;

		public PatternItem(String type, String value) {
			this.type = type;
			this.value = value;
		}
	}
	
	private List<PatternItem> patterns;

	@Override
	public int describe(InputStream contents, IContentDescription description)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(contents);
		return describe(reader, description);
	}

	@Override
	public QualifiedName[] getSupportedOptions() {
		return QUALIFIERS;
	}

	@Override
	public int describe(Reader contents, IContentDescription description)
			throws IOException {
		BufferedReader buffer = new BufferedReader(contents);
		StringBuilder cat = new StringBuilder();
		String line;
		while ((line = buffer.readLine()) != null) {
			cat.append(line);
		}
		String result = cat.toString();
		if (result.matches(VISUAL_SWING_CLASS_TAILER)) {
			for (PatternItem pattern : patterns) {
				if (result.matches(pattern.value)) {
					if (description != null)
						description.setProperty(COMPONENT_TYPE, pattern.type);
					return VALID;
				}
			}
			if (description != null)
				description.setProperty(COMPONENT_TYPE, "Unknown");
			return VALID;
		}
		return INDETERMINATE;
	}

	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		patterns = new ArrayList<PatternItem>();
		IConfigurationElement[] children = config.getChildren("pattern");
		if(children!=null&&children.length>0){
			for(IConfigurationElement child:children){
				String type = child.getAttribute("type");
				String value = child.getAttribute("value");
				patterns.add(new PatternItem(type, value));
			}
		}
	}
}
