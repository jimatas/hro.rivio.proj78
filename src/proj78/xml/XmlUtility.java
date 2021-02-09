/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Contains various static utility methods that are used throughout the DOM.
 * 
 * <p>Note that although this class is publicly accessible, it is meant primarily for internal use.
 * 
 * @author K.A., copyright (c) 2008
 */
public final class XmlUtility {
	
	/* non-instantiable */ private XmlUtility() {}
	
	/**
	 * Determines whether the specified character can be used in an XML name.
	 * 
	 * @param ch the character to evaluate.
	 * @param initialChar indicates whether it is the first character in the name.
	 * @return <code>true</code> if the specified character is an XML name character, 
	 * 	<code>false</code> otherwise.
	 * @see proj78.xml.XmlUtility#isValidName(java.lang.String)
	 */
	public static boolean isNameChar(char ch, boolean initialChar) {
		return (Character.isLetter(ch) || ch == '_')
				|| (!initialChar && (Character.isDigit(ch)
						|| ch == '-'
						|| ch == '.'
						|| ch == ':'));
	}
	
	/**
	 * Determines whether the specified string contains a valid XML name.
	 * 
	 * <p>An XML name must start with a letter or an underscore and may contain only letters, 
	 * digits, underscores, hyphens, periods and (at most a single) colon.
	 * 
	 * @param str the string to evaluate.
	 * @return <code>true</code> if the specified string contains a valid XML name, 
	 * 	<code>false</code> otherwise.
	 * @see proj78.xml.XmlUtility#isNameChar(char, boolean)
	 */
	public static boolean isValidName(String str) {
		if (str.equals("#cdata-section")
				|| str.equals("#comment")
				|| str.equals("#document")
				|| str.equals("#document-fragment")
				|| str.equals("#text")) {
			return true;
		}
		int len = str.length();
		int colon = -1;
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			if (!isNameChar(ch, i == 0 || i - 1 == colon)) {
				return false;
			}
			if (ch == ':') {
				if (colon >= 0 || i == len - 1) {
					return false;
				}
				colon = i;
			}
		}
		return len != 0;
	}
	
	/**
	 * Determines whether the specified character is a quote character.
	 * 
	 * @param ch the character to evaluate.
	 * @return <code>true</code> if the specified character is either a single or a double quote,
	 * 	<code>false</code> otherwise.
	 */
	public static boolean isQuoteChar(char ch) {
		return ch == '"' || ch == '\'';
	}
	
	/**
	 * Determines whether the specified character is considered whitespace in XML.
	 * 
	 * <p>XML whitespace characters are:
	 * <ul>
	 * 	<li>spaces (<code>' '</code>)</li>
	 * 	<li>carrage returns (<code>'\r'</code>)</li>
	 * 	<li>line feeds (<code>'\n'</code>) and</li>
	 * 	<li>horizontal tabs (<code>'\t'</code>)</li>
	 * </ul>
	 * 
	 * @param ch the character to evaluate.
	 * @return <code>true</code> if the specified character is an XML whitespace character,
	 * 	<code>false</code> otherwise.
	 * @see proj78.xml.XmlUtility#isWhitespace(java.lang.String)
	 */
	public static boolean isWhitespaceChar(char ch) {
		return ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t';
	}
	
	/**
	 * Determines whether the specified string consists entirely of XML whitespace.
	 * 
	 * @param str the string to evaluate.
	 * @return <code>true</code> if the specified string is made up of XML whitespace characters, 
	 * 	<code>false</code> otherwise.
	 * @see proj78.xml.XmlUtility#isWhitespaceChar(char)
	 */
	public static boolean isWhitespace(String str) {
		int len = str.length();
		for (int i = 0; i < len; i++) {
			if (!isWhitespaceChar(str.charAt(i))) {
				return false;
			}
		}
		return len != 0;
	}
	
	/**
	 * Determines whether the specified string contains an entity reference.
	 * 
	 * @param str the string to evaluate.
	 * @return <code>true</code> if the specified string contains either a predefined XML entity reference
	 * 	or a (possibly numeric) character reference, <code>false</code> otherwise.
	 */
	public static boolean isEntityReference(String str) {
		if (str.equals("&amp;") 
				|| str.equals("&lt;") 
				|| str.equals("&gt;") 
				|| str.equals("&apos;")
				|| str.equals("&quot;")) {
			return true;
		}
		if (!(str.startsWith("&") && str.endsWith(";"))) {
			return false;
		}
		str = str.substring(1, str.length() - 1); // everything between '&' and ';'
		int len = str.length();
		if (str.startsWith("#")) {
			str = str.substring(1); // everything after '#'
			len--;
			if (str.startsWith("x") || str.startsWith("X")) {
				str = str.substring(1); // everything after 'x' (or 'X')
				len--;
				for (int i = 0; i < len; i++) {
					char ch = str.charAt(i);
					if ((ch < '0' || ch > '9')
							&& ch != 'a' && ch != 'A'
							&& ch != 'b' && ch != 'B'
							&& ch != 'c' && ch != 'C'
							&& ch != 'd' && ch != 'D'
							&& ch != 'e' && ch != 'E'
							&& ch != 'f' && ch != 'F') {
						return false;
					}
				}
				return len >= 1;
			}
			for (int i = 0; i < len; i++) {
				char ch = str.charAt(i);
				if (ch < '0' || ch > '9') {
					return false;
				}
			}
			return len >= 2;
		}
		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);
			if (((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z'))
					&& (i == 0 || ((ch < '0' || ch > '9') 
									&& ch != '_' 
									&& ch != '-'))) {
				return false;
			}
		}
		return len >= 2;
	}
}