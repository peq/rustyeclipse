package rustyeclipse.builder;

import org.eclipse.jdt.annotation.NonNull;


public class CompileError {
	private final SourcePos position;
	private final String message;
	private final ErrorType errorType;
	
	public CompileError(SourcePos position, String message, ErrorType errorType) {
		this.position = position;
		this.message = message;
		this.errorType = errorType;
	}

	public SourcePos getPosition() {
		return position;
	}

	public String getMessage() {
		return message;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
	
	

}
