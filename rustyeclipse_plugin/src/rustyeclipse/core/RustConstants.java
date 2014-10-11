package rustyeclipse.core;

import org.eclipse.swt.graphics.RGB;

public class RustConstants {

	public static final String PLUGIN_PACKAGE ="de.peeeq.eclipserustplugin";
	
	//Parenthesis Highlighting
	public final static String EDITOR_MATCHING_BRACKETS       = "matchingBrackets";
	public final static String EDITOR_MATCHING_BRACKETS_COLOR = "matchingBracketsColor";
	public static final String DEFAULT_MATCHING_BRACKETS_COLOR = "128,128,128";
	
	//PreferenceStore Strings
	public static final String SYNTAXCOLOR_COLOR         = "Color";
	public static final String SYNTAXCOLOR_BOLD          = "Bold";
	public static final String SYNTAXCOLOR_ITALIC        = "Italic";
	public static final String SYNTAXCOLOR_UNDERLINE     = "Underline";
	public static final String SYNTAXCOLOR_STRIKETHROUGH = "Strikethrough";

	public static final String SYNTAXCOLOR_TEXT        = "Text";
	public static final String SYNTAXCOLOR_KEYWORD     = "Keyword";
	public static final String SYNTAXCOLOR_JASSTYPE    = "Jasstype";
	public static final String SYNTAXCOLOR_STRING      = "String";
	public static final String SYNTAXCOLOR_ANNOTATION  = "Annotation";
	public static final String SYNTAXCOLOR_COMMENT     = "Comment";
	public static final String SYNTAXCOLOR_DOCCOMMENT  = "Doc comment";
	public static final String SYNTAXCOLOR_FUNCTION    = "Function";
	public static final String SYNTAXCOLOR_DATATYPE    = "Datatype";
	public static final String SYNTAXCOLOR_VAR         = "Var";
	public static final String SYNTAXCOLOR_PARAM       = "Param";
	public static final String SYNTAXCOLOR_FIELD       = "Field";
	public static final String SYNTAXCOLOR_INTERFACE   = "Interface";
	public static final String SYNTAXCOLOR_CONSTRUCTOR = "Constructor";

	//Colors
	private static final RGB COLOR_BLACK = new RGB(0,0,0);
	
	
	public static final RGB SYNTAXCOLOR_RGB_TEXT         = new RGB(0, 0, 0);
	public static final RGB SYNTAXCOLOR_RGB_JASSTYPE     = new RGB(34, 136, 143);
	public static final RGB SYNTAXCOLOR_RGB_KEYWORD      = new RGB(127, 0, 85);
	public static final RGB SYNTAXCOLOR_RGB_STRING       = new RGB(42, 0, 255);
	public static final RGB SYNTAXCOLOR_RGB_ANNOTATION   = new RGB(100, 100, 100);
	public static final RGB SYNTAXCOLOR_RGB_COMMENT      = new RGB(63, 127, 95);
	public static final RGB SYNTAXCOLOR_RGB_HOTDOC       = new RGB(115, 146, 225);
	public static final RGB SYNTAXCOLOR_RGB_FUNCTION     = COLOR_BLACK;
	public static final RGB SYNTAXCOLOR_RGB_DATATYPE     = new RGB(64, 64, 64);
	public static final RGB SYNTAXCOLOR_RGB_VAR          = COLOR_BLACK;
	public static final RGB SYNTAXCOLOR_RGB_PARAM        = COLOR_BLACK;
	public static final RGB SYNTAXCOLOR_RGB_FIELD        = new RGB(0, 0, 128); 
	public static final RGB SYNTAXCOLOR_RGB_INTERFACE    = SYNTAXCOLOR_RGB_FIELD;          
	public static final RGB SYNTAXCOLOR_RGB_CONSTRUCTOR  = COLOR_BLACK;
	// config
	public static final String RUST_AUTOCOMPLETION_DELAY = "RUST_AUTOCOMPLETION_DELAY";
	public static final String RUST_ENABLE_AUTOCOMPLETE = "RUST_ENABLE_AUTOCOMPLETE";
	public static final String RUST_ENABLE_RECONCILING = "RUST_ENABLE_RECONCILING";
	public static final String RUST_RECONCILATION_DELAY = "RUST_RECONCILATION_DELAY";
	public static final String RUST_IGNORE_ERRORS = "RUST_IGNORE_ERRORS";
	public static final String RUST_COMMAND_RUSTC = "RUST_COMMAND_RUSTC";
	public static final String RUST_COMMAND_CARGO = "RUST_COMMAND_CARGO";
	public static final String RUST_COMMAND_RACER = "RUST_COMMAND_RACER";
	public static final String RUST_LIB_PATH = "RUST_LIB_PATH";
	
	//Partition Scanner
	public static final String PARTITION_SINLGE_LINE_COMMENT = "singleComment";
	public static final String PARTITION_MULTI_LINE_COMMENT  = "multiComment";
	public static final String PARTITION_STRING              = "string";
	public static final String PARTITION_CHARACTER           = "char";
	public static final String[] PARTITION_TYPES = {PARTITION_SINLGE_LINE_COMMENT, PARTITION_MULTI_LINE_COMMENT, PARTITION_STRING, PARTITION_CHARACTER};
	
	// marker
	public static final String POS_START_LINE = PLUGIN_PACKAGE + ".startPosLine";
	public static final String POS_START_COLUMN = PLUGIN_PACKAGE + ".startPosColumn";
	public static final String POS_END_LINE = PLUGIN_PACKAGE + ".endPosLine";
	public static final String POS_END_COLUMN = PLUGIN_PACKAGE + ".endPosColumn";

	public static final String RUST_PARTITIONING = "__rust_partitioning";


	
}
