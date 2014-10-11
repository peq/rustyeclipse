package rustyeclipse.editors.highlighting;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import rustyeclipse.core.RustConstants;
import rustyeclipse.core.RustCorePlugin;
import rustyeclipse.util.UtilityFunctions;

public class SimpleCodeScanner extends RuleBasedScanner implements RustScanner {

	
	private Token keywordToken;
	private Token commentToken;
	private Token hotdocToken;
	private Token stringToken;
	private Token annotationToken;
	private Token jasstypeToken;
	private Token identifierToken;

	public SimpleCodeScanner() {
		
		UtilityFunctions.getDefaultPreferenceStore()
		  .addPropertyChangeListener(new IPropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent event) {
		    	updateColors();
		    	setRules();
		    	RustCorePlugin.refreshEditors();
		    }
		  }); 
		
		updateColors();
		setRules();
	}

	private void setRules() {
		WordRule keywordRule = new WordRule(new IWordDetector() {
			@Override
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			@Override
			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		}, identifierToken, false);
		// add tokens for each reserved word
		for (String keyword : RustKeywords.KEYWORDS) {
			keywordRule.addWord(keyword, keywordToken);
		}
		
		for (String jasstype : RustKeywords.JASSTYPES) {
			keywordRule.addWord(jasstype, jasstypeToken);
		}
		
		WhitespaceRule whitespaceRule = new WhitespaceRule(new IWhitespaceDetector() {
			@Override
			public boolean isWhitespace(char c) {
				return Character.isWhitespace(c);
			}
		});
		setRules(new IRule[] { 
				new EndOfLineRule("//", commentToken), 
				new SingleLineRule("\"", "\"", stringToken, '\\'),
				new SingleLineRule("@", " ", annotationToken),
//				new SingleLineRule("'", "'", stringToken, '\\'), 
//				new MultiLineRule("/*", "*/", commentToken),
				whitespaceRule,
				keywordRule,

			});
	}
	
	private void updateColors() {
		IPreferenceStore preferencestore = UtilityFunctions.getDefaultPreferenceStore();
		jasstypeToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_JASSTYPE);
		keywordToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_KEYWORD);
		commentToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_COMMENT);
		hotdocToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_DOCCOMMENT);
		stringToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_STRING);
		annotationToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_ANNOTATION);
		identifierToken = makeToken(preferencestore, RustConstants.SYNTAXCOLOR_TEXT);
	}

	private Token makeToken(IPreferenceStore preferencestore, String key) {
		return new Token(new TextAttribute(new Color(Display.getCurrent(), PreferenceConverter.getColor(preferencestore, RustConstants.SYNTAXCOLOR_COLOR
				+ key)), null, UtilityFunctions.computeAttributes(preferencestore, key)));
	}

	@Override
	public String getPartitionType() {
		return IDocument.DEFAULT_CONTENT_TYPE;
	}
	
	@Override
	public IToken nextToken() {
		return super.nextToken();
	}

}
