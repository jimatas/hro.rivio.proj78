/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser2;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import proj78.xml.XmlException;

import static proj78.xml.XmlUtility.*;

/**
 * Concrete {@link proj78.xml.parser2.XmlParser XmlParser} subclass.
 * 
 * @author K.A., copyright (c) 2008
 */
/* internal */ class XmlParserImpl extends XmlParser {
	/**
	 * The main parsing routine.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 * @see proj78.xml.parser2.XmlParser#parse()
	 */
	protected void parse() {
		parseXmlProlog();
		Stack<String> tags = new Stack<String>();
		while (true) {
			char ch = scan();
			if (ch == '<') {
				ch = scan();
				if (ch == '!') {
					ch = peek();
					if (ch == '-') {
						parseComment();
					} else if (ch == '[') {
						parseCDATASection();
					} else {
						throw new XmlException(String.format("Invalid tag name; XML comment or CDATA expected on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
				} else if (ch == '?') {
					parseProcessingInstruction();
				} else if (ch == '/') {
					parseTagEnd(tags);
				} else {
					unscan();
					parseTagStart(tags);
				}
			} else if (ch == 0) {
				if (!tags.empty()) {
					throw new XmlException(String.format("Unexpected end of document on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
				}
				break;
			} else {
				String chars = scanUntil('<');
				if (chars == null) {
					while (isWhitespaceChar(ch = scan()));
					if (ch == 0 && tags.empty()) {
						break;
					}
					if (!tags.empty()) {
						throw new XmlException(String.format("Unexpected end of document on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
					throw new XmlException("Junk after end of document.", XmlException.MALFORMED_XML_ERR);
				}
				fireCharactersEvent(String.valueOf(ch) + chars);
			}
		} 
	}
	
	/**
	 * Processes the document prolog (e.g., XML declaration, DOCTYPE), if it is present.
	 * 
	 * <p>Note: The cursor will initially be positioned before the very first character in the document.
	 * When this method completes successfully, the cursor will be positioned immediately before the
	 * opening left angle bracket of the root element.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseXmlProlog() {
		skip(WHITESPACE);
		while (true) {
			char ch = scan();
			if (ch == 0) {
				throw new XmlException("A well-formed document requires a root element.", XmlException.MALFORMED_XML_ERR);
			}
			if (ch == '<') {
				ch = scan();
				if (ch == '!') {
					ch = peek();
					if (ch == '-') {
						parseComment();
						skip(WHITESPACE);
					} else if (ch == 'D') {
						parseDoctypeDeclaration();
						skip(WHITESPACE);
					} else {
						throw new XmlException(String.format("Invalid tag name; XML comment or DOCTYPE expected on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
				} else if (ch == '?') {
					parseProcessingInstruction();
					skip(WHITESPACE);
				} else {
					unscan();
					unscan(); // '<'
					break;
				}
			} else {
				throw new XmlException("No content allowed before the XML prolog.", XmlException.MALFORMED_XML_ERR);
			}
		}
	}
	
	/**
	 * Called to process the pseudo-attributes in the XML declaration.
	 * 
	 * <p>Note: The cursor will initially be positioned before the first pseudo-attribute in the declaration (i.e., 
	 * <code>version="..."</code>). When this method completes successfully, the cursor will be positioned immediately 
	 * after the string <code>"?>"</code>.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseXmlDeclaration() {
		Map<String, String> attrs = parseAttributes();
		if (skip("?>") == 0) {
			throw new XmlException("XML declaration not properly terminated.", XmlException.MALFORMED_XML_ERR);
		}
		String version = null;
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
				throw new XmlException("Unknown pseudo-attribute in XML declaration.", XmlException.MALFORMED_XML_ERR);
			}
		}
		if (version == null) {
			throw new XmlException("The required XML version-attribute is missing.", XmlException.MALFORMED_XML_ERR);
		}
		fireXmlDeclarationEvent(version, encoding, standalone);
	}
	
	/**
	 * Processes the DOCTYPE declaration.
	 * 
	 * <p>Note: The cursor will initially be positioned before the string <code>"DOCTYPE"</code>, 
	 * just after the left angle bracket. When this method completes successfully, the cursor will
	 * be positioned immediately after the closing right angle bracket of the DOCTYPE declaration.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseDoctypeDeclaration() {
		if (skip("DOCTYPE") == 0) {
			throw new XmlException(String.format("Invalid tag name; DOCTYPE expected on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		if (skip(WHITESPACE) == 0) {
			throw new XmlException("Whitespace required after DOCTYPE keyword.", XmlException.MALFORMED_XML_ERR);
		}
		boolean done = false;
		boolean hasInternalSubset = false;
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("Unexpected end of DOCTYPE declaration on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (ch == '[') {
				hasInternalSubset = true;
				break;
			}
			if (ch == '>') {
				done = true;
				break;
			}
			if (isWhitespaceChar(ch)) {
				break;
			}
			if (!isNameChar(ch, buffer.length() == 0)) {
				throw new XmlException("Illegal XML character in root name of DOCTYPE declaration.", XmlException.MALFORMED_XML_ERR);
			}
			buffer.append(ch);
		}
		String dtdName = buffer.toString();
		if (dtdName.length() == 0) {
			throw new XmlException("Missing root name in DOCTYPE declaration.", XmlException.MALFORMED_XML_ERR);
		}
		if (done) {
			fireDoctypeDeclarationEvent(dtdName, null, null, null);
		} else {
			String publicId = null;
			String systemId = null;
			if (!hasInternalSubset) {
				skip(WHITESPACE);
				if (skip("PUBLIC") != 0) {
					if (skip(WHITESPACE) == 0) {
						throw new XmlException(String.format("Whitespace required after PUBLIC keyword on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
					publicId = parseIdLiteral();
					skip(WHITESPACE);
					systemId = parseIdLiteral();
				} else if (skip("SYSTEM") != 0) {
					if (skip(WHITESPACE) == 0) {
						throw new XmlException(String.format("Whitespace required after SYSTEM keyword on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
					systemId = parseIdLiteral();
				}
				skip(WHITESPACE);
				if (peek() == '[') {
					hasInternalSubset = true;
				}
			}
			String internalSubset = null;
			if (hasInternalSubset) {
				scan();
				internalSubset = scanUntil(']');
				if (internalSubset == null) {
					throw new XmlException(String.format("Internal subset not properly terminated on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
				}
				scan();
			}
			skip(WHITESPACE);
			if (scan() != '>') {
				throw new XmlException(String.format("DOCTYPE declaration not properly terminated on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			fireDoctypeDeclarationEvent(dtdName, publicId, systemId, internalSubset);
		}
	}
	
	/**
	 * Parses an external (e.g., public or system) identifier literal.
	 * 
	 * <p>Note: The cursor will initially be positioned before the quote character enclosing the literal.
	 * When this method completes successfully, the cursor will be positioned immediately after the 
	 * terminating quote character.
	 * 
	 * @return the ID literal that was parsed.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private String parseIdLiteral() {
		char quoteChar = scan();
		if (!isQuoteChar(quoteChar)) {
			throw new XmlException(String.format("ID literal must be enclosed in quotes on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("ID literal not properly terminated on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (ch == quoteChar) {
				break;
			}
			buffer.append(ch);
		}
		return buffer.toString();
	}
	
	/**
	 * Processes the starting tag of an element.
	 * 
	 * <p>Note: The cursor will initially be positioned before the first character in the tag name. 
	 * When this method completes successfully, the cursor will be positioned immediately after the 
	 * closing right angle bracket of the tag.
	 * 
	 * @param tags a stack of open tags onto which the tag name is to be pushed.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseTagStart(Stack<String> tags) {
		if (skip(WHITESPACE) != 0) {
			throw new XmlException(String.format("No whitespace allowed before tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		boolean done = false;
		boolean selfClosing = false;
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("Unexpected end of element begin tag on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (ch == '/' && peek() == '>') {
				scan();
				done = selfClosing = true;
				break;
			}
			if (ch == '>') {
				done = true;
				break;
			}
			if (isWhitespaceChar(ch)) {
				break;
			}
			if (!isNameChar(ch, buffer.length() == 0)) {
				throw new XmlException(String.format("Illegal XML character in tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			buffer.append(ch);
		}
		String tagName = buffer.toString();
		if (tagName.length() == 0) {
			throw new XmlException(String.format("Missing tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		Map<String, String> attrs = new HashMap<String, String>();
		if (!done) {
			attrs = parseAttributes();
			if (skip("/>") != 0) {
				selfClosing = true;
			} else if (scan() != '>') {
				throw new XmlException(String.format("Unexpected end of element begin tag on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
		}
		fireTagStartEvent(tagName, attrs);
		if (selfClosing) {
			fireTagEndEvent(tagName);
		} else {
			tags.push(tagName);
		}
	}
	
	/**
	 * Parses the attributes found in the starting tag of an element or (the pseudo-attributes) in the XML declaration.
	 * 
	 * @return a possibly empty map of name/value pairs representing the attributes that were parsed.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private Map<String, String> parseAttributes() {
		Map<String, String> attrs = new HashMap<String, String>();
		while (true) {
			skip(WHITESPACE);
			if (peek() == '>') {
				break;
			}
			if (peek() == '/' || peek() == '?') {
				scan();
				if (peek() == '>') {
					unscan();
					break;
				}
			}
			parseAttributeInto(attrs);
		}
		return attrs;
	}
	
	/**
	 * Parses a single attribute into the specified map of name/value pairs representing the attributes that have been parsed so far.
	 *  
	 * @param attrs a map containing the attributes that have been parsed so far.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseAttributeInto(Map<String, String> attrs) {
		StringBuffer buffer = new StringBuffer();
		String name = null;
		String value = null;
		char quoteChar = 0;
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("Unexpected end of attribute definition on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (isQuoteChar(ch)) {
				if (quoteChar == 0) {
					quoteChar = ch;
					buffer = new StringBuffer();
					continue;
				} 
				if (ch == quoteChar) {
					value = buffer.toString();
					break;
				}
			}
			if (quoteChar == 0) {
				if (ch == '=') {
					name = buffer.toString().trim();
					skip(WHITESPACE);
					if (!isQuoteChar(peek())) {
						throw new XmlException(String.format("Attribute value must be enclosed in quotes on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
					continue;
				}
				if (!isNameChar(ch, buffer.length() == 0)) {
					if (isWhitespaceChar(ch)) {
						skip(WHITESPACE);
						if (peek() == '=') {
							continue;
						}
					}
					throw new XmlException(String.format("Illegal XML character in attribute name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
				}
			}
			buffer.append(ch);
		}
		attrs.put(name, value);
	}
	
	/**
	 * Processes the ending tag of an element.
	 * 
	 * <p>Note: The cursor will initially be positioned before the first character in the tag name.
	 * When this method completes successfully, the cursor will be positioned immediately after the 
	 * closing right angle bracket of the tag.
	 * 
	 * @param tags a stack of opened tags from which the tag name is to be popped.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseTagEnd(Stack<String> tags) {
		if (skip(WHITESPACE) != 0) {
			throw new XmlException(String.format("No whitespace allowed before tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("Unexpected end of element end tag on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (isWhitespaceChar(ch)) {
				break;
			}
			if (ch == '>') {
				unscan();
				break;
			}
			if (!isNameChar(ch, buffer.length() == 0)) {
				throw new XmlException(String.format("Illegal XML character in tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			buffer.append(ch);
		}
		String tagName = buffer.toString();
		if (tagName.length() == 0) {
			throw new XmlException(String.format("Missing tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		skip(WHITESPACE);
		if (scan() != '>') {
			throw new XmlException(String.format("Junk after tag name on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		String lastTagName = null;
		if (tags.empty() || !(lastTagName = tags.pop()).equals(tagName)) {
			throw new XmlException(String.format("Mismatched tag name '%s'; '%s' expected on line %d.", tagName, lastTagName, getLine()), XmlException.MALFORMED_XML_ERR);
		}
		fireTagEndEvent(tagName);
	}
	
	/**
	 * Processes a PI (processing instruction) that was encountered in the document.
	 * 
	 * Note: The cursor will initially be positioned before the first character in the instruction target.
	 * When this method completes successfully, the cursor will be positioned immediately after the string 
	 * <code>"?>"</code>.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseProcessingInstruction() {
		if (skip(WHITESPACE) != 0) {
			throw new XmlException(String.format("No whitespace allowed before instruction target on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		boolean done = false;
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0 || ch == '<') {
				throw new XmlException(String.format("Unexpected end of processing instruction on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (isWhitespaceChar(ch)) {
				break;
			}
			if (ch == '?' && peek() == '>') {
				scan();
				done = true;
				break;
			}
			if (!isNameChar(ch, buffer.length() == 0)) {
				throw new XmlException(String.format("Illegal XML character in instruction target on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			buffer.append(ch);
		}
		String target = buffer.toString();
		if (target.length() == 0) {
			throw new XmlException(String.format("Missing instruction target on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		if (target.equalsIgnoreCase("xml")) {
			if (!target.equals("xml")) {
				throw new XmlException(String.format("The target '%s' is not allowed in the processing instruction one line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			skip(WHITESPACE);
			parseXmlDeclaration();
		} else {
			String data = null;
			if (!done) {
				skip(WHITESPACE);		
				buffer = new StringBuffer();
				while (true) {
					char ch = scan();
					if (ch == 0 || ch == '<') {
						throw new XmlException(String.format("Unexpected end of processing instruction on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
					}
					if (ch == '?' && peek() == '>') {
						scan();
						break;
					}
					buffer.append(ch);
				}
				data = buffer.toString();
			}
			fireProcessingInstructionEvent(target, data);
		}
	}
	
	/**
	 * Processes a CDATA-section.
	 * 
	 * <p>Note: The cursor will initially be positioned before the string <code>"[CDATA["</code>, just
	 * after the left angle bracket and exclamation mark. When this method completes successfully, the 
	 * cursor will be positioned immediately after the string <code>"]]>"</code>.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseCDATASection() {
		if (skip("[CDATA[") == 0) {
			throw new XmlException(String.format("Invalid tag name; CDATA expected on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		String content = scanUntil("]]>");
		if (content == null) {
			throw new XmlException(String.format("CDATA-section not properly terminated on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		skip("]]>");
		fireCDataSectionEvent(content);
	}
	
	/**
	 * Processes a comment in the document.
	 * 
	 * <p>Note: The cursor will initially be positioned before the string <code>"--"</code>, just after the left angle bracket.
	 * When this method completes successfully, the cursor will be positioned immediately after the string <code>"-->"</code>.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void parseComment() {
		if (skip("--") == 0) {
			throw new XmlException(String.format("Invalid tag name; XML comment expected on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
		}
		StringBuffer buffer = new StringBuffer();
		while (true) {
			char ch = scan();
			if (ch == 0) {
				throw new XmlException(String.format("Unexpected end of XML comment on line %d.", getLine()), XmlException.MALFORMED_XML_ERR);
			}
			if (ch == '-') {
				if (scan() == '-' && peek() == '>') {
					scan();
					break;
				}
				unscan();
			}
			buffer.append(ch);
		}
		fireCommentEvent(buffer.toString());
	}
}