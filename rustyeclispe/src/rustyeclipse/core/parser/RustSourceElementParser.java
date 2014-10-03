package rustyeclipse.core.parser;

import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.eclipse.dltk.core.ISourceElementParser;

import rustyeclipse.core.RustNature;

public class RustSourceElementParser extends AbstractSourceElementParser implements ISourceElementParser {

	public RustSourceElementParser() {
		System.out.println("Created");
	}

	@Override
	protected String getNatureId() {
		return RustNature.RUST_NATURE;
	}

	@Override
	public void parseSourceModule(IModuleSource module) {
		ISourceElementRequestor requestor = getRequestor();

		requestor.enterModule();
		TypeInfo info = new TypeInfo();
		info.name = "Example type";
		requestor.enterType(info);
		requestor.exitType(0);
		requestor.exitModule(0);
	}

}
