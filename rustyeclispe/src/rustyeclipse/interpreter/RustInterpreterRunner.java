package rustyeclipse.interpreter;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IExecutionLogger;
import org.eclipse.dltk.internal.launching.DLTKLaunchingPlugin;
import org.eclipse.dltk.launching.AbstractInterpreterRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.LaunchingMessages;

import rustyeclipse.interpreter.launch.LaunchConstants;

public class RustInterpreterRunner extends AbstractInterpreterRunner implements IInterpreterRunner {

	protected RustInterpreterRunner(IInterpreterInstall install) {
		super(install);
	}

	@Override
	protected String getProcessType() {
		return LaunchConstants.ID_RUST_PROCESS_TYPE;
	}
	
	@Override
	protected IProcess rawRun(ILaunch launch, InterpreterConfig config) throws CoreException {
		IProcess proc = super.rawRun(launch, config);
		while (!proc.isTerminated()) {
			System.out.println("waiting to terminate ... ");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException("cancelled.");
			}
		}
		System.out.println("proc.exitValue = " + proc.getExitValue());
		if (proc.getExitValue() == 0) {
			// compile successful, run it
			IPath exePath = config.getScriptFilePath().removeFileExtension();
			String[] cmdLine = new String[] {
					EnvironmentPathUtils.getLocalPath(exePath).toOSString()
			};
			IPath workingDirectory = config.getWorkingDirectoryPath();
			String[] environment = getEnvironmentVariablesAsStrings(config);

			IExecutionEnvironment exeEnv = getInstall().getExecEnvironment();
			IExecutionLogger logger = null;
			Process p = exeEnv.exec(cmdLine, workingDirectory, environment, logger);
			if (p == null) {
				abort(
						LaunchingMessages.AbstractInterpreterRunner_executionWasCancelled,
						null);
			}
			String cmdLineLabel = Arrays.toString(cmdLine);
			final IProcess process[] = new IProcess[] { null };
			String processLabel = "adasdasads";
			DebugPlugin.getDefault().addDebugEventListener(
					new IDebugEventSetListener() {
						@Override
						public void handleDebugEvents(DebugEvent[] events) {
							for (int i = 0; i < events.length; i++) {
								DebugEvent event = events[i];
								if (event.getSource().equals(process[0])) {
									if (event.getKind() == DebugEvent.CHANGE
											|| event.getKind() == DebugEvent.TERMINATE) {
//										updateProcessLabel(launch, cmdLineLabel, process[0]);
										if (event.getKind() == DebugEvent.TERMINATE) {
											DebugPlugin.getDefault()
													.removeDebugEventListener(this);
										}
									}
								}
							}
						}
					});
			process[0] = newProcess(launch, p, processLabel, getDefaultProcessMap());
			process[0].setAttribute(IProcess.ATTR_CMDLINE, cmdLineLabel);
//			updateProcessLabel(launch, cmdLineLabel, process[0]);
			
		}
		return proc;
	}

}
