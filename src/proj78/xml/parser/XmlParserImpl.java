/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import proj78.xml.XmlUtility;

/**
 * Concrete <code>XmlParser</code> subclass.
 *
 * @author K.A., copyright (c) 2008
 */
/* internal */ class XmlParserImpl extends XmlParser {
	/**
	 * An array of all the characters that are considered whitespace in XML.
	 */
	private static final char[] WHITESPACE = { ' ', '\r', '\n', '\t' };
	
	/**
	 * The current line number (used for error reporting).
	 */
	private int line;
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.parser.XmlParser#parse(proj78.xml.parser.XmlParser.Scanner)
	 */
	protected void parse(Scanner in) {
		in = makeLineTracking(in);
		parseXmlProlog(in);
		Stack<String> tags = new Stack<String>();
		while (true) {
			if (in.peek() == '<') {
				in.scan(); // '<'
				if (in.peek() == '!') {
					in.scan(); // '!'
					if (in.peek() == '-') {
						parseComment(in);
					} else if (in.peek() == '[') {
						parseCDataSection(in);
					} else {
						throw new XmlParseException(String.format("Invalid tag name; XML comment or CDATA expected on line %d.", line));
					}
				} else if (in.peek() == '?') {
					parseProcessingInstruction(in);
				} else if (in.peek() == '/') {
					parseEndingTag(in, tags);
				} else {
					parseStartingTag(in, tags);
				}
			} else if (in.peek() == Scanner.EOF) {
				if (!tags.empty()) {
					throw new XmlParseException(String.format("Unexpected end of document on line %d.", line));
				}
				break;
			} else {
				String chars = in.scanUntil('<');
				if (chars == null) {
					char ch;
					while (XmlUtility.isWhitespaceChar(ch = in.scan()));
					if (ch == Scanner.EOF && tags.empty()) {
						break;
					}
					if (!tags.empty()) {
						throw new XmlParseException(String.format("Unexpected end of document on line %d.", line));
					}
					throw new XmlParseException(String.format("Junk after end of document on line %d.", line));
				}
				fireCharactersEvent(chars);
			}
		}
	}
	
	/**
	 * Processes the XML prolog (e.g., XML declaration, DOCTYPE), if present.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the very first character in the XML document. When 
	 * 	this method completes successfully, the cursor will be positioned 
	 * 	immediately before the opening left angle bracket of the root element.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseXmlProlog(Scanner in) {
		in.skip(WHITESPACE);
		while (true) {
			if (in.peek() == Scanner.EOF) {
				throw new XmlParseException("A well-formed document requires a root element.");
			}
			if (in.peek() == '<') {
				in.scan(); // '<'
				if (in.peek() == '!') {
					in.scan(); // '!'
					if (in.peek() == '-') {
						parseComment(in);
						in.skip(WHITESPACE);
					} else if (in.peek() == 'D') {
						parseDoctype(in);
						in.skip(WHITESPACE);
					} else {
						throw new XmlParseException(String.format("Invalid tag name; XML comment or DOCTYPE expected on line %d.", line));
					}
				} else if (in.peek() == '?') {
					parseProcessingInstruction(in);
					in.skip(WHITESPACE);
				} else {
					in.unscan(); // '<' (root element tag)
					break;
				}
			} else {
				throw new XmlParseException("No content allowed before start of document.");
			}
		}
	}
	
	/**
	 * Processes the starting tag of an element.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the first character in the tag name, just after the 
	 * 	opening left angle bracket. When this method completes successfully, 
	 * 	the cursor will be positioned immediately after the closing right angle 
	 * 	bracket.
	 * @param tags a stack of opened tags onto which the tag name is to be pushed.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseStartingTag(Scanner in, Stack<String> tags) {
		if (in.skip(WHITESPACE) != 0) {
			throw new XmlParseException(String.format("No whitespace allowed before tag name on line %d.", line));
		}
		boolean done = false;
		boolean selfClosing = false;
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF || ch == '<') {
				throw new XmlParseException(String.format("Unexpected end of element begin tag on line %d.", line));
			}
			if (ch == '/' && in.peek() == '>') {
				in.scan(); // '>'
				done = selfClosing = true;
				break;
			}
			if (ch == '>') {
				done = true;
				break;
			}
			if (XmlUtility.isWhitespaceChar(ch)) {
				break;
			}
			if (!XmlUtility.isNameChar(ch, str.length() == 0)) {
				throw new XmlParseException(String.format("Illegal character in tag name on line %d.", line));
			}
			str.append(ch);
		}
		String tagName = str.toString();
		if (tagName.length() == 0) {
			throw new XmlParseException(String.format("Missing tag name on line %d.", line));
		}
		Map<String, String> attrs = null;
		if (!done) {
			in.skip(WHITESPACE); // left over whitespace
			boolean quoted = false;
			char quoteChar = (char)0;
			str = new StringBuffer();
			while (true) {
				char ch = in.scan();
				if (ch == Scanner.EOF || ch == '<') {
					throw new XmlParseException(String.format("Unexpected end of element begin tag on line %d.", line));
				}
				if (XmlUtility.isQuoteChar(ch)) {
					if (ch == quoteChar) {
						quoteChar = (char)0;
						quoted = false;
					} else if (!quoted) {
						quoteChar = ch;
						quoted = true;
					}
				}
				if (!quoted && ch == '/' && in.peek() == '>') {
					in.scan(); // '>'
					selfClosing = true;
					break;
				}
				if (!quoted && ch == '>') {
					break;
				}
				str.append(ch);
			}
			attrs = parseAttributes(str.toString());
		}
		fireTagStartEvent(tagName, attrs);
		if (selfClosing) {
			fireTagEndEvent(tagName);
		} else {
			tags.push(tagName);
		}
	}
	
	/**
	 * Processes the ending tag of an element.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the forward slash character, just after the opening 
	 * 	left angle bracket. When this method completes successfully, the cursor 
	 * 	will be positioned immediately after the closing right angle bracket.
	 * @param tags a stack of opened tags from which the tag name is to be popped.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseEndingTag(Scanner in, Stack<String> tags) {
		in.skip('/');
		if (in.skip(WHITESPACE) != 0) {
			throw new XmlParseException(String.format("No whitespace allowed before tag name on line %d.", line));
		}
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF || ch == '<') {
				throw new XmlParseException(String.format("Unexpected end of element end tag on line %d.", line));
			}
			if (XmlUtility.isWhitespaceChar(ch)) {
				break;
			}
			if (ch == '>') {
				in.unscan(); // '>'
				break;
			}
			if (!XmlUtility.isNameChar(ch, str.length() == 0)) {
				throw new XmlParseException(String.format("Illegal character in tag name on line %d.", line));
			}
			str.append(ch);
		}
		String tagName = str.toString();
		if (tagName.length() == 0) {
			throw new XmlParseException(String.format("Missing tag name on line %d.", line));
		}
		in.skip(WHITESPACE); // left over whitespace
		if (in.scan() != '>') {
			throw new XmlParseException(String.format("Illegal character after tag name on line %d.", line));
		}
		String lastTagName = null;
		if (tags.empty() || !(lastTagName = tags.pop()).equals(tagName)) {
			throw new XmlParseException(String.format("Mismatched tag name '%s'; '%s' expected on line %d.", tagName, lastTagName, line));
		}
		fireTagEndEvent(tagName);
	}
	
	/**
	 * Processes a CDATA-section.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the string <code>"[CDATA["</code>. When this method 
	 * 	completes successfully, the cursor will be positioned immediately after 
	 * 	the closing right angle bracket of the CDATA-section.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseCDataSection(Scanner in) {
		if (in.skip("[CDATA[") == 0) {
			throw new XmlParseException(String.format("Invalid tag name; CDATA expected on line %d.", line));
		}
		String data = in.scanUntil("]]>");
		if (data == null) {
			throw new XmlParseException(String.format("Unexpected end of CDATA-section on line %d.", line));
		}
		fireCDataSectionEvent(data);
		in.skip("]]>");
	}
	
	/**
	 * Processes an XML comment.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the string <code>"--"</code>, just after the opening 
	 * 	left angle bracket and exclamation mark. When this method completes 
	 * 	successfully, the cursor will be positioned immediately after the closing 
	 * 	right angle bracket.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseComment(Scanner in) {
		if (in.skip('-') < 2) {
			throw new XmlParseException(String.format("Invalid tag name; XML comment expected on line %d.", line));
		}
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF) {
				throw new XmlParseException(String.format("Unexpected end of XML comment on line %d.", line));
			}
			if (ch == '-') {
				if (in.scan() == '-' && in.peek() == '>') {
					in.scan(); // '>'
					break;
				}
				in.unscan(); // '-'
			}
			str.append(ch);
		}
		fireCommentEvent(str.toString());
	}
	
	/**
	 * Processes the DOCTYPE declaration.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the string <code>"DOCTYPE"</code>, just after the 
	 * 	opening left angle bracket and exclamation mark. When this method 
	 * 	completes successfully, the cursor will be positioned immediately after 
	 * 	the closing right angle bracket of the DOCTYPE declaration.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseDoctype(Scanner in) {
		if (in.skip("DOCTYPE") == 0) {
			throw new XmlParseException(String.format("Invalid tag name; DOCTYPE expected on line %d.", line));
		}
		if (in.skip(WHITESPACE) == 0) {
			throw new XmlParseException(String.format("Whitespace required after DOCTYPE keyword on line %d.", line));
		}
		boolean done = false;
		boolean hasInternalSubset = false;
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF || ch == '<') {
				throw new XmlParseException(String.format("Unexpected end of DOCTYPE declaration on line %d.", line));
			}
			if (ch == '[') {
				hasInternalSubset = true;
				break;
			}
			if (ch == '>') {
				done = true;
				break;
			} 
			if (XmlUtility.isWhitespaceChar(ch)) {
				break;
			}
			if (!XmlUtility.isNameChar(ch, str.length() == 0)) {
				throw new XmlParseException(String.format("Illegal character in DOCTYPE name on line %d.", line));
			}
			str.append(ch);
		}
		String dtdName = str.toString();
		if (dtdName.length() == 0) {
			throw new XmlParseException(String.format("Missing DOCTYPE name on line %d.", line));
		}
		if (done) {
			fireDoctypeEvent(dtdName, null, null, null);
			return;
		}
		String publicId = null;
		String systemId = null;
		if (!hasInternalSubset) {
			in.skip(WHITESPACE);
			if (in.skip("PUBLIC") != 0) {
				if (in.skip(WHITESPACE) == 0) {
					throw new XmlParseException(String.format("Whitespace required after PUBLIC keyword on line %d.", line));
				}
				publicId = parseExternalId(in);
				in.skip(WHITESPACE); // optional
				systemId = parseExternalId(in);
			} else if (in.skip("SYSTEM") != 0) {
				if (in.skip(WHITESPACE) == 0) {
					throw new XmlParseException(String.format("Whitespace required after SYSTEM keyword on line %d.", line));
				}
				systemId = parseExternalId(in);
			}
			in.skip(WHITESPACE); // optional
			if (in.peek() == '[') {
				hasInternalSubset = true;
			}
		}
		String internalSubset = null;
		if (hasInternalSubset) {
			in.scan(); // '['
			internalSubset = in.scanUntil(']');
			if (internalSubset == null) {
				throw new XmlParseException(String.format("Internal subset not properly terminated on line %d.", line));
			}
			in.scan(); // ']'
		}
		in.skip(WHITESPACE);
		if (in.scan() != '>') {
			throw new XmlParseException(String.format("DOCTYPE declaration not properly terminated on line %d.", line));
		}
		fireDoctypeEvent(dtdName, publicId, systemId, internalSubset);
	}
	
	/**
	 * Parses an external identifier literal.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the opening quote character enclosing the identifier. 
	 * 	When this method completes successfully, the cursor will be positioned 
	 * 	immediately after the terminating quote character.
	 * @return the extracted identifier.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private String parseExternalId(Scanner in) {
		char quoteChar = in.scan();
		if (!XmlUtility.isQuoteChar(quoteChar)) {
			throw new XmlParseException(String.format("External identifier must be enclosed in quotes on line %d.", line));
		}
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF || ch == '<') {
				throw new XmlParseException(String.format("External identifier not properly terminated on line %d.", line));
			} 
			if (ch == quoteChar) {
				break;
			} 
			str.append(ch);
		}
		return str.toString();
	}
	
	/**
	 * Processes a PI in the document.
	 * 
	 * @param in the <code>Scanner</code> object with its cursor initially 
	 * 	positioned before the question mark, just after the opening left angle 
	 * 	bracket. When this method completes successfully, the cursor will be 
	 * 	positioned immediately after the closing right angle bracket of the PI 
	 * 	element.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private void parseProcessingInstruction(Scanner in) {
		in.skip('?');
		if (in.skip(WHITESPACE) != 0) {
			throw new XmlParseException(String.format("No whitespace allowed before instruction target on line %d.", line));
		}
		boolean done = false;
		StringBuffer str = new StringBuffer();
		while (true) {
			char ch = in.scan();
			if (ch == Scanner.EOF || ch == '<') {
				throw new XmlParseException(String.format("Unexpected end of processing instruction on line %d.", line));
			}
			if (XmlUtility.isWhitespaceChar(ch)) {
				break;
			} 
			if (ch == '?' && in.peek() == '>') {
				in.scan(); // '>'
				done = true;
				break;
			}
			if (!XmlUtility.isNameChar(ch, str.length() == 0)) {
				throw new XmlParseException(String.format("Illegal character in instruction target on line %d.", line));
			}
			str.append(ch);
		}
		String target = str.toString();
		if (target.length() == 0) {
			throw new XmlParseException(String.format("Missing instruction target on line %d.", line));
		}
		String data = null;
		if (!done) {
			in.skip(WHITESPACE); // left over whitespace
			str = new StringBuffer();
			while (true) {
				char ch = in.scan();
				if (ch == Scanner.EOF || ch == '<') {
					throw new XmlParseException(String.format("Unexpected end of processing instruction on line %d.", line));
				}
				if (ch == '?' && in.peek() == '>') {
					in.scan(); // '>'
					break;
				}
				str.append(ch);
			}
			data = str.toString();
		}
		if (target.equalsIgnoreCase("xml")) {
			if (!target.equals("xml")) { // "xml" must be lower case; assume an illegal PI target.
				throw new XmlParseException(String.format("The target '%s' is not allowed in the processing instruction on line %d.", target, line));
			}
			parseXmlDeclaration(data);
		} else {
			fireProcessingInstructionEvent(target, data);
		}
	}
	
	/**
	 * Called by <code>parseProcessingInstruction()</code> to further process 
	 * the document's XML declaration.
	 * 
	 * @param xml a string containing the content (i.e., pseudo-attributes) of 
	 * 	the XML declaration.
	 * @throws XmlParseException if a parse error occurs. 
	 */
	private void parseXmlDeclaration(String xml) {
		Map<String, String> attrs = parseAttributes(xml);
		String version = null; // required
		String encoding = null;
		String standalone = null;
		for (Entry<String, String> attr : attrs.entrySet()) {
			if (attr.getKey().equals("version")) {
				version = attr.getValue();
			} else if (attr.getKey().equals("encoding")) {
				encoding = attr.getValue();
			} else if (attr.getKey().equals("standalone")) {
				standalone = attr.getValue();
			} else {
				throw new XmlParseException(String.format("Unknown pseudo-attribute '%s' in XML declaration.", attr.getKey()));
			}
		}
		if (version == null) {
			throw new XmlParseException("The required XML version attribute is missing.");
		}
		fireXmlDeclarationEvent(version, encoding, standalone);
	}
	
	/**
	 * Parses an attribute string into a map of name/value pairs.
	 * 
	 * @param str a string containing the attributes to parse.
	 * @return a map of name/value pairs parsed from the input string.
	 * @throws XmlParseException if a parse error occurs.
	 */
	private Map<String, String> parseAttributes(String str) {
		Map<String, String> attrs = new HashMap<String, String>();
		Scanner in = Scanner.over(str);
		while (true) {
			in.skip(WHITESPACE);
			String name = in.scanUntil('=');
			if (name == null) {
				in.skip(WHITESPACE);
				if (in.scan() != Scanner.EOF) {
					throw new XmlParseException(String.format("Junk after attribute declaration on line %d.", line));
				}
				break;
			}
			name = name.trim();
			if (!XmlUtility.isValidName(name)) {
				throw new XmlParseException(String.format("Illegal character in attribute name on line %d.", line));
			}
			in.scan(); // '='
			in.skip(WHITESPACE);
			char quoteChar = in.scan();
			if (!XmlUtility.isQuoteChar(quoteChar)) {
				throw new XmlParseException(String.format("Attribute value must be enclosed in quotes on line %d.", line));
			}
			String value = in.scanUntil(quoteChar);
			if (value == null) {
				throw new XmlParseException(String.format("Attribute declaration not properly terminated on line %d.", line));
			}
			in.scan();
			attrs.put(name, value);
		}
		return attrs;
	}
	
	/**
	 * Decorates a <code>Scanner</code> object with line tracking functionality.
	 */
	private Scanner makeLineTracking(final Scanner in) {
		line = 1; // reset
		return new Scanner() {
			/**
			 * Indicates whether lines are terminated by either 
			 * <code>"\r\n"</code> or <code>'\r'</code>.
			 */
			private boolean usesCarriageReturns;
			
			/*
			 * (non-Javadoc)
			 * @see proj78.xml.parser.XmlParser.Scanner#scan()
			 */
			public char scan() {
				char ch = in.scan();
				if (ch == '\r' && line == 1) {
					usesCarriageReturns = true;
				}
				if (ch == '\r' || (ch == '\n' && !usesCarriageReturns)) {
					line++;
				}
				return ch;
			}
			
			/*
			 * (non-Javadoc)
			 * @see proj78.xml.parser.XmlParser.Scanner#unscan()
			 */
			public char unscan() {
				char ch = in.unscan();
				if (ch == '\r' || (ch == '\n' && !usesCarriageReturns)) {
					line--;
				}
				return ch;
			}
			
			/*
			 * (non-Javadoc)
			 * @see proj78.xml.parser.XmlParser.Scanner#peek()
			 */
			public char peek() {
				return in.peek();
			}
		};
	}
}