package rustyeclipse.launch;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.RuntimeProcess;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import rustyeclipse.preferences.RustPrefs;

import com.google.common.collect.Maps;

public class LaunchDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
//		IOConsole console = findConsole("rust");
//		console.clearConsole();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(config.getAttribute(LaunchConstants.PROJECT_NAME, ""));
		monitor.beginTask("start preparing " + project, 1);
		File projectDir = project.getLocation().toFile();
		List<String> commands = new ArrayList<>();
		commands.add(RustPrefs.get().getCargoCommand());
		commands.add("run");
		ProcessBuilder pb = new ProcessBuilder(commands);
		pb.directory(projectDir);
		try {
			Process proc = pb.start();
			
			Map<String, String> attr = Maps.newHashMap();
			attr.put(DebugPlugin.ATTR_CAPTURE_OUTPUT, "true");
			RuntimeProcess rproc = new RuntimeProcess(launch, proc, "Rust launch", attr);
			
			
//			while (proc.isAlive()) {
//				System.out.println("waiting");
//				if (monitor.isCanceled()) {
//					throw new InterruptedException("isCanceled");
//				}
//				Thread.sleep(1000);
//			}
//			System.out.println("terminated");
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}

}
