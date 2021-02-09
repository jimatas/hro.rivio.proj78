/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import proj78.xml.XmlException;

/**
 * Parses XML from some source, generating parse events during the process. 
 * 
 * <p>Implements the {@link proj78.xml.parser2.XmlParseEventSource XmlParseEventSource} interface 
 * so that interested classes implementing the {@link proj78.xml.parser2.XmlParseEventListener 
 * XmlParseEventListener} interface can register themselves with this class. 
 * 
 * @author K.A., copyright (c) 2008
 */
public abstract class XmlParser implements XmlParseEventSource {
	/**
	 *  Characters that are considered whitespace in XML.
	 */
	protected static final char[] WHITESPACE = new char[] { ' ', '\r', '\n', '\t' };
	
	/**
	 * The current line number.
	 */
	private int line;
	
	/**
	 * A {@link java.io.Reader} containing the XML being parsed.
	 */
	private Reader reader;
	
	/**
	 * Indicates whether a next call to <code>readIntoBuffer()</code> will return more data.
	 */
	private boolean eof;
	
	/**
	 * Internal character buffer.
	 */
	private StringBuffer buffer;
	
	/**
	 * The current character index into the buffer.
	 */
	private int index;
	
	/**
	 * Indicates whether lines are terminated by either <code>\r\n</code> or <code>\r</code>. 
	 */
	private boolean usesCarriageReturns;
	
	/**
	 * All the registered parse event listeners.
	 */
	private List<XmlParseEventListener> listeners;
	
	/**
	 * Initializes a new instance of the <code>XmlParser</code> class.
	 */
	protected XmlParser() {
	}
	
	/**
	 * Factory method that allows for the application to obtain an XML parser.
	 * 
	 * @return a new instance of the concrete <code>XmlParser</code> subclass.
	 */
	public static XmlParser newInstance() {
		return new XmlParserImpl();
	}
	
	/**
	 * Parses XML from a resource that is made accessible through the specified {@link java.io.InputStream InputStream} object.
	 * 
	 * @param in represents the stream of XML characters to parse.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the specified byte stream.
	 */
	public final void parse(InputStream in) {
		parse(new InputStreamReader(in));
	}
	
	/**
	 * Parses XML from a resource that is made accessible through the specified {@link java.io.Reader Reader} object.
	 * 
	 * @param in a <code>Reader</code> containing the XML to parse; either a complete document or a well-formed fragment.
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 *	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the specified character stream.
	 */
	public final void parse(Reader in) {
		reader = in;
		reset();
		parse();
	}
	
	/**
	 * Resets the parser; re-intializing its private fields to their default values.
	 */
	private void reset() {
		line = 1;
		eof = false;
		buffer = new StringBuffer();
		index = 0;
		usesCarriageReturns = false;
	}
	
	/**
	 * Main parsing routine that is implemented in the subclass.
	 * 
	 * @throws XmlException <em>MALFORMED_XML_ERR</em>: if a parse error occurs.<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected abstract void parse();
	
	/**
	 * Returns the current line number.
	 * 
	 * @return the line that is currently being parsed.
	 */
	protected final int getLine() {
		return line;
	}
	
	public final void addParseEventListener(XmlParseEventListener listener) {
		getListenerList().add(listener);
	}
	
	public final void removeParseEventListener(XmlParseEventListener listener) {
		getListenerList().remove(listener);
	}
	
	public final void fireXmlDeclarationEvent(String version, String encoding, String standalone) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onXmlDeclaration(version, encoding, standalone);
		}
	}
	
	public final void fireDoctypeDeclarationEvent(String dtdName, String publicId, String systemId, String internalSubset) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onDoctypeDeclaration(dtdName, publicId, systemId, internalSubset);
		}
	}
	
	public final void fireTagStartEvent(String tagName, Map<String, String> attrs) {
		for (String key : attrs.keySet()) {
			attrs.put(key, expandPredefinedEntities(attrs.get(key)));
		}
		attrs = Collections.unmodifiableMap(attrs);
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onTagStart(tagName, attrs);
		}
	}
	
	public final void fireTagEndEvent(String tagName) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onTagEnd(tagName);
		}
	}
	
	public final void fireProcessingInstructionEvent(String target, String data) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onProcessingInstruction(target, data);
		}
	}
	
	public final void fireCharactersEvent(String chars) {
		chars = expandPredefinedEntities(chars);
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onCharacters(chars);
		}
	}
	
	public final void fireCDataSectionEvent(String content) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onCDataSection(content);
		}
	}
	
	public final void fireCommentEvent(String content) {
		for (XmlParseEventListener listener : getListenerList()) {
			listener.onComment(content);
		}
	}
	
	/**
	 * Expands all predefined entities to their characters values inside the specified string.
	 */
	private String expandPredefinedEntities(String xml) {
		return xml.replace("&amp;", "&")
			.replace("&lt;", "<")
			.replace("&gt;", ">")
			.replace("&apos;", "'")
			.replace("&quot;", "\"");
	}
	
	/**
	 * Returns a list view of all the parse event listeners that have been registered with this parser.
	 * 
	 * @return a list view of the listeners registered with this parser.
	 */
	private List<XmlParseEventListener> getListenerList() {
		if (listeners == null) {
			synchronized (this) {
				if (listeners == null) {
					listeners = new ArrayList<XmlParseEventListener>();
				}
			}
		}
		return listeners;
	}
	
	/**
	 * Reads the next chunk of XML data into the internal buffer. 
	 * 
	 * <p>This method may set the <code>eof</code> field to <code>true</code> if the end of stream 
	 * has been reached (i.e., a subsequent read operation would not return more data). 
	 * 
	 * @throws XmlException <em>XML_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	private void readIntoBuffer() {
		char[] chars = new char[256];
		int len = 0;
		try {
			len = reader.read(chars);
		} catch (IOException ex) {
			throw new XmlException(ex.getMessage(), XmlException.FILE_IO_ERR);
		}
		if (len >= 1) {
			buffer.append(chars, 0, len);
			eof = len < chars.length;
		} else {
			eof = true;
		}
	}
	
	/**
	 * Moves the cursor forward one position and returns the character at that position.
	 * 
	 * @return the character at the next position or the null-char (<code>'\0'</code>), 
	 * 	if there were no more characters.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final char scan() {
		char ch = 0;
		if (index >= buffer.length()) {
			if (eof) {
				return ch;
			}
			readIntoBuffer();
		}
		ch = buffer.charAt(index++); 
		if (ch == '\r' && line == 1) {
			usesCarriageReturns = true;
		}
		if (ch == '\r' || (ch == '\n' && !usesCarriageReturns)) {
			line++;
		}
		return ch;
	}
	
	/**
	 * Moves the cursor back one position and returns the character at that position.
	 * 
	 * @return the character at the previous position or the null-char (<code>'\0'</code>), 
	 * 	if there were no more characters.
	 */
	protected final char unscan() {
		char ch = 0;
		if (index == 0) {
			return ch;
		}
		ch = buffer.charAt(--index);
		if (ch == '\r' || (ch == '\n' && !usesCarriageReturns)) {
			line--;
		}
		return ch;
	}
	
	/**
	 * Returns the character at the next position without moving the cursor forward.
	 * 
	 * @return the character at the next position or the null-char (<code>'\0'</code>), 
	 * 	if there were no more characters.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final char peek() {
		char ch = scan();
		if (ch != 0 || index == buffer.length()) {
			unscan();
		}
		return ch;
	}
	
	/**
	 * Scans from the current position to the first occurrence of any of the specified characters.
	 * 
	 * @param pattern the characters to scan until.
	 * @return a string cotaining everything from the current position to the first occurrence of any of the specified characters. 
	 * 	Returns <code>null</code> if none of those characters were encountered.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final String scanUntil(char... pattern) {
		StringBuffer buffer = new StringBuffer();
		int scanned = 0;
		char ch;
		while ((ch = scan()) != 0) {
			scanned++;
			for (int i = pattern.length - 1; i >= 0; i--) {
				if (charsEqual(ch, pattern[i], false)) {
					unscan();
					return buffer.toString();
				}
			}
			buffer.append(ch);
		}
		while (scanned != 0) {
			unscan();
			scanned--;
		}
		return null;
	}
	
	/**
	 * Scans from the current position to the first occurrence of the specified string.
	 * 
	 * @param pattern the string to scan until.
	 * @return a string containing everything from the current position to the first occurrence of the specified string.
	 * 	Returns <code>null</code> if the specified string was not encountered.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final String scanUntil(String pattern) {
		return scanUntil(pattern, false);
	}
	
	/**
	 * Scans from the current position to the first occurrence of the specified string.
	 * 
	 * @param pattern the string to scan until.
	 * @param ignoreCase indicates whether the comparison is case-insensitive.
	 * @return a string containing everything from the current position to the first occurrence of the specified string.
	 * 	Returns <code>null</code> if the specified string was not encountered.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final String scanUntil(String pattern, boolean ignoreCase) {
		StringBuffer buffer = new StringBuffer();
		int scanned = 0;
		char ch;
		while ((ch = scan()) != 0) {
			scanned++;
			if (!charsEqual(ch, pattern.charAt(0), ignoreCase)) {
				buffer.append(ch);
			} else {
				int mark = scanned;
				boolean found = true;
				for (int i = 1; i < pattern.length(); i++) {
					if ((ch = scan()) != 0) {
						scanned++;
						if (!charsEqual(ch, pattern.charAt(i), ignoreCase)) {
							found = false;
							break;
						}
					} else {
						found = false;
						break;
					}
				}
				if (found) {
					while (scanned >= mark) {
						unscan();
						scanned--;
					}
					return buffer.toString();
				}
				while (scanned != mark) {
					unscan();
					scanned--;
				}
				buffer.append(pattern.charAt(0));
			}
		}
		while (scanned != 0) {
			unscan();
			scanned--;
		}
		return null;
	}
	
	/**
	 * Moves the cursor forward without returning a value (i.e., skipping characters) while the next character 
	 * scanned matches any of the specified characters - in any order.
	 * 
	 * @param pattern the string to skip.
	 * @return the number of characters that were skipped; either zero or the length of the specified string.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final int skip(char... pattern) {
		int skipped = 0;
		char ch;
		while ((ch = scan()) != 0) {
			boolean skip = false;
			for (int i = 0; i < pattern.length; i++) {
				if (charsEqual(ch, pattern[i], false)) {
					skip = true;
					break;
				}
			}
			if (!skip) {
				unscan();
				break;
			}
			skipped++;
		}
		return skipped;
	}
	
	/**
	 * Moves the cursor forward without returning a value (i.e., skipping characters) while the next character 
	 * scanned matches the subsequent character in the specified string, in order, from first to last.
	 * 
	 * @param pattern - the string to skip.
	 * @return the number of characters that were skipped; either zero or the length of the specified string.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final int skip(String pattern) {
		return skip(pattern, false);
	}
	
	/**
	 * Moves the cursor forward without returning a value (i.e., skipping characters) while the next character 
	 * scanned matches the subsequent character in the specified string, in order, from first to last.
	 * 
	 * @param pattern the string to skip.
	 * @param ignoreCase indicates whether the comparison is case-insensitive.
	 * @return the number of characters that were skipped; either zero or the length of the specified string.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs reading from the underlying character stream.
	 */
	protected final int skip(String pattern, boolean ignoreCase) {
		char ch;
		for (int i = 0; i < pattern.length(); i++) {
			if ((ch = scan()) != 0) {
				if (!charsEqual(ch, pattern.charAt(i), ignoreCase)) {
					for ( ; i >= 0; i--) {
						unscan();
					}
					return 0;
				}
			}
		}
		return pattern.length();
	}
	
	/**
	 * Determines whether two characters are equal, either with or without regard for case (depending on the boolean argument).
	 */
	private static boolean charsEqual(char ch1, char ch2, boolean ignoreCase) {
		if (ch1 != ch2) {
			if (ignoreCase) {
				return false;
			}
			ch1 = Character.toUpperCase(ch1);
			ch2 = Character.toUpperCase(ch2);
			if (ch1 != ch2) {
				ch1 = Character.toLowerCase(ch1);
				ch2 = Character.toLowerCase(ch2);
				if (ch1 != ch2) {
					return false;
				}
			}
		}
		return true;
	}
}