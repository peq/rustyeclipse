package rustyeclipse.interpreter.launch;

import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;


public class LaunchConstants {

	public static final String PROJECT_NAME = ScriptLaunchConfigurationConstants.ATTR_PROJECT_NAME; 
			//c("PROJECT_NAME");
	public static final String LAUNCH_CONFIG_ID = "de.peeeq.eclipsewurstplugin.wustlauchconfig";
	public static final String ID_RUST_SCRIPT = "rustyeclipse.interpreter.RustLaunchConfigurationType";
	public static final String ID_RUST_PROCESS_TYPE = null;

	
	private static String c(String key) {
		return LaunchConstants.class.getCanonicalName() + "_" + key;
	}

}
