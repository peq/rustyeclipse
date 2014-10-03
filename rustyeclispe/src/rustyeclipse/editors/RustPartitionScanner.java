package rustyeclipse.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;


public class RustPartitionScanner extends RuleBasedPartitionScanner {
	public final static String RUST_MULTILINE_COMMENT= "__rust_multiline_comment";
	public final static String RUST_OTHER = "__rust_other";
	public final static String HOT_DOC= "__rust_hotdoc"; 
	public final static String[] PARTITION_TYPES= new String[] { RUST_MULTILINE_COMMENT, HOT_DOC, RUST_OTHER };

	/**
	 * Detector for empty comments.
	 */
	static class EmptyCommentDetector implements IWordDetector {

		/* (non-Javadoc)
		* Method declared on IWordDetector
	 	*/
		@Override
		public boolean isWordStart(char c) {
			return (c == '/');
		}

		/* (non-Javadoc)
		* Method declared on IWordDetector
	 	*/
		@Override
		public boolean isWordPart(char c) {
			return (c == '*' || c == '/');
		}
	}

	/**
	 *
	 */
	static class WordPredicateRule extends WordRule implements IPredicateRule {

		private IToken fSuccessToken;

		public WordPredicateRule(IToken successToken) {
			super(new EmptyCommentDetector());
			fSuccessToken= successToken;
			addWord("/**/", fSuccessToken); //$NON-NLS-1$
		}

		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#evaluate(ICharacterScanner, boolean)
		 */
		@Override
		public @Nullable IToken evaluate(@Nullable ICharacterScanner scanner, boolean resume) {
			return super.evaluate(scanner);
		}

		/*
		 * @see org.eclipse.jface.text.rules.IPredicateRule#getSuccessToken()
		 */
		@Override
		public IToken getSuccessToken() {
			return fSuccessToken;
		}
	}

	/**
	 * Creates the partitioner and sets up the appropriate rules.
	 */
	public RustPartitionScanner() {
		super();

		IToken javaDoc= new Token(HOT_DOC);
		IToken comment= new Token(RUST_MULTILINE_COMMENT);
		IToken other= new Token(RUST_OTHER);

		List<IPredicateRule> rules= new ArrayList<>();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("//", other)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", other, '\\')); //$NON-NLS-2$ //$NON-NLS-1$
		rules.add(new SingleLineRule("'", "'", other, '\\')); //$NON-NLS-2$ //$NON-NLS-1$

		// Add special case word rule.
		rules.add(new WordPredicateRule(comment));

		// Add rules for multi-line comments and javadoc.
		rules.add(new MultiLineRule("/**", "*/", javaDoc, (char) 0, true)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new MultiLineRule("/*", "*/", comment, (char) 0, true)); //$NON-NLS-1$ //$NON-NLS-2$

		setPredicateRules(rules.toArray(new IPredicateRule[rules.size()]));
	}
	
	
	@Override
	public @Nullable IToken nextToken() {
		return super.nextToken();
	}
}
