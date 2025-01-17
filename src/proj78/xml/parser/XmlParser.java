/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj78.xml.XmlException;

/**
 * An <code>XmlParser</code> object takes a string or some other source of XML
 * as its input and processes it, generating <em>significant parse events</em> 
 * along the way. These events can be captured (for further processing) by 
 * classes implementing the <code>XmlParseEventListener</code> interface.
 * 
 * @author K.A., copyright (c) 2008
 */
public abstract class XmlParser {
	/**
	 * A list containing the registered parse event listeners.
	 */
	private List<XmlParseEventListener> listeners;
	
	/**
	 * Parses a string of XML.
	 * 
	 * @param xml a string containing the XML to parse; either an entire XML 
	 * 	document or a well-formed fragment.
	 * @throws XmlException (or the more specific {@link XmlParseException}) if
	 * 	a parse error occurs.
	 */
	public final void parse(String xml) {
		parse(Scanner.over(xml));
	}
	
	/**
	 * Parses XML from a resource that is made accessible through the specified
	 * <code>Reader</code> object.
	 * 
	 * @param in a <code>Reader</code> object containing the XML to parse.
	 * @throws XmlException (or the more specific {@link XmlParseException}) if
	 * 	a parse error occurs.
	 */
	public final void parse(Reader in) throws IOException {
		StringBuffer xml = new StringBuffer();
		char[] buffer = new char[1024];
		int len = -1;
		while ((len = in.read(buffer, 0, buffer.length)) != -1) {
			xml.append(buffer, 0, len);
		}
		parse(xml.toString());
	}
	
	/**
	 * Parses XML from a resource that is made accessible through the specified 
	 * <code>InputStream</code> object.
	 * 
	 * @param in represents the stream of XML to parse.
	 * @throws XmlException (or the more specific {@link XmlParseException}) if 
	 * 	a parse error occurs.
	 */
	public final void parse(InputStream in) throws IOException {
		parse(new InputStreamReader(in));
	}
	
	/**
	 * Main parsing routine that is implemented in the concrete subclass; 
	 * processes the entire XML document.
	 * 
	 * @param in the <code>Scanner</code> object used for processing the XML 
	 * 	document, with the cursor initially positioned before the very first 
	 * 	character.
	 */
	protected abstract void parse(Scanner in);
	
	/**
	 * Registers a new parse event listener with this parser.
	 * 
	 * @param listener an object implementing the <code>XmlParseEventListener</code>
	 *	interface, that will be notified of all the parse events generated by this
	 *	parser.
	 */
	public final void addParseEventListener(XmlParseEventListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<XmlParseEventListener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * Removes a previously registered listener object.
	 * 
	 * @param listener the listener object to unregister.
	 */
	public final void removeParseEventListener(XmlParseEventListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed the XML 
	 * declaration. Dispatches the <code>onXmlDeclaration()</code> event to all 
	 * the registered listeners.
	 * 
	 * @param version the XML version in use.
	 * @param encoding the character encoding used, if available.
	 * @param standalone the standalone designation of the document, if available.
	 */
	protected final void fireXmlDeclarationEvent(String version, String encoding, String standalone) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onXmlDeclaration(version, encoding, standalone);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed the 
	 * DOCTYPE declaration. Dispatches the <code>onDoctype()</code> event to 
	 * all the registered listeners.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @param publicId the public identifier of the external subset, if available.
	 * @param systemId the system identifier of the external subset, if available.
	 * @param internalSubset a string containing the internal subset, if available.
	 */
	protected final void fireDoctypeEvent(String dtdName, String publicId, String systemId, String internalSubset) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onDoctype(dtdName, publicId, systemId, internalSubset);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed the 
	 * starting tag of an element. Dispatches the <code>onTagStart()</code> 
	 * event to all the registered listeners.
	 * 
	 * @param tagName the tag name parsed.
	 * @param attrs a map containing the the attributes that were parsed, if any;
	 *	otherwise <code>null</code> or an empty map.
	 */
	protected final void fireTagStartEvent(String tagName, Map<String, String> attrs) {
		if (listeners != null) {
			if (attrs == null) {
				attrs = new HashMap<String, String>();
			} else {
				for (String key : attrs.keySet()) {
					attrs.put(key, expandPredefinedEntities(attrs.get(key)));
				}
			}
			attrs = Collections.unmodifiableMap(attrs);
			for (XmlParseEventListener listener : listeners) {
				listener.onTagStart(tagName, attrs);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed the 
	 * ending tag of an element. Dispatches the <code>onTagEnd()</code> event
	 * to all the registered listeners.
	 * 
	 * @param tagName the tag name parsed.
	 */
	protected final void fireTagEndEvent(String tagName) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onTagEnd(tagName);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed a processing instruction 
	 * (the XML declaration is handled by the <code>fireXmlDeclaration()</code> method). 
	 * Dispatches the <code>onProcessingInstruction()</code> call to all the registered 
	 * listeners.
	 * 
	 * @param target the instruction target parsed.
	 * @param data the instruction data parsed.
	 */
	protected final void fireProcessingInstructionEvent(String target, String data) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onProcessingInstruction(target, data);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed a 
	 * CDATA-section. Dispatches the <code>onCDataSection()</code> event to 
	 * all the registered listeners.
	 * 
	 * @param content the CDATA-section's content.
	 */
	protected final void fireCDataSectionEvent(String content) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onCDataSection(content);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed an XML 
	 * comment. Dispatches the <code>onComment()</code> event to all the 
	 * registered listeners.
	 * 
	 * @param content the commented text (i.e., everything between 
	 * 	<code>&lt;!--</code> and <code>--&gt;</code>) that was parsed.
	 */
	protected final void fireCommentEvent(String content) {
		if (listeners != null) {
			for (XmlParseEventListener listener : listeners) {
				listener.onComment(content);
			}
		}
	}
	
	/**
	 * Called by the concrete parser subclass as soon as it has parsed (some 
	 * more) text (i.e., character data other than CDATA). Dispatches the 
	 * <code>onCharacters()</code> event to all the registered listeners.
	 * 
	 * @param chars the characters parsed.
	 */
	protected final void fireCharactersEvent(String chars) {
		if (listeners != null) {
			chars = expandPredefinedEntities(chars);
			for (XmlParseEventListener listener : listeners) {
				listener.onCharacters(chars);
			}
		}
	}
	
	/**
	 * Expands all occurrences of the predefined XML entity references (i.e., 
	 * "&amp;amp;", "&amp;lt;", "&amp;gt;", "&amp;apos;" and "&amp;quot;") to 
	 * their character values inside the specified string.
	 */
	private static String expandPredefinedEntities(String str) {
		return str.replace("&amp;", "&")
				.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&apos;", "'")
				.replace("&quot;", "\"");
	}
	
	/**
	 * Scans over a source of textual input one character at a time.
	 * 
	 * <p>Some basic string matching operations are also provided, such as those 
	 * for scanning text until some pattern is found or skipping over the input 
	 * while the scanned sequence of characters matches some pattern.
	 * 
	 * @author K.A., copyright (c) 2008
	 */
	protected static abstract class Scanner {
		/**
		 * The end-of-input marker returned by <code>scan()</code>, <code>unscan()</code> 
		 * and <code>peek()</code>.
		 */
		public static final char EOF = '\0';
		
		/**
		 * Alias of <code>EOF</code>.
		 */
		public static final char BOF = EOF;
		
		/**
		 * Creates a new character array-backed <code>Scanner</code> object.
		 * 
		 * @param chars the characters to scan over.
		 * @return a character array-backed scanner.
		 */
		public static final Scanner over(final char[] chars) {
			return new Scanner() {
				private int i = -1;
				
				/*
				 * (non-Javadoc)
				 * @see proj78.xml.parser.XmlParser.Scanner#scan()
				 */
				public char scan() {
					return i < chars.length - 1 ? chars[++i] : EOF;
				}
				
				/*
				 * (non-Javadoc)
				 * @see proj78.xml.parser.XmlParser.Scanner#unscan()
				 */
				public char unscan() {
					return i >= 0 ? chars[i--] : BOF;
				}
				
				/*
				 * (non-Javadoc)
				 * @see proj78.xml.parser.XmlParser.Scanner#peek()
				 */
				public char peek() {
					return i < chars.length - 1 ? chars[i + 1] : EOF;
				}
			};
		}
		
		/**
		 * Creates a new string-backed <code>Scanner</code> object.
		 * 
		 * @param str the string to scan over.
		 * @return a string-backed scanner.
		 */
		public static final Scanner over(String str) {
			return over(str.toCharArray());
		}
		
		/**
		 * Moves the internal cursor forward one position and returns the character
		 * at that position.
		 * 
		 * @return the character at the next position or <code>'\0'</code>, if scanned 
		 * 	past the very last character.
		 */
		public abstract char scan();
		
		/**
		 * Moves the internal cursor back one position and returns the character at
		 * that position.
		 * 
		 * @return the character at the previous position or <code>'\0'</code>, if 
		 * 	scanned past the very first character. 
		 */
		public abstract char unscan();
		
		/**
		 * Returns the character at the next position without moving the internal 
		 * cursor forward.
		 * 
		 * @return the character at the next position or <code>'\0'</code>, if peeked 
		 * 	past the very last character.
		 */
		public abstract char peek();
		
		/**
		 * Returns a string containing everything from the current position to the 
		 * first occurrence of the specified string.
		 * 
		 * @param pattern the string to search until.
		 * @return everything from the current position to the first occurrence of 
		 * 	the specified string or <code>null</code>, if that string was not found.
		 */
		public final String scanUntil(String pattern) {
			return scanUntil(pattern, false);
		}
		
		/**
		 * Returns a string containing everything from the current position to the 
		 * first occurrence of the specified string.
		 * 
		 * @param pattern the string to search until.
		 * @param ignoreCase specifies whether the comparison is case-insensitive.
		 * @return everything from the current position to the first occurrence of 
		 * 	the specified string or <code>null</code>, if that string was not found.
		 */
		public final String scanUntil(String pattern, boolean ignoreCase) {
			if (pattern != null && pattern.length() != 0) {
				StringBuffer str = new StringBuffer();
				int scanned = 0;
				char ch;
				while ((ch = scan()) != EOF) {
					scanned++;
					if (!charsEqual(ch, pattern.charAt(0), ignoreCase)) {
						str.append(ch);
					} else {
						int mark = scanned;
						boolean found = true;
						for (int i = 1, len = pattern.length(); i < len; i++) {
							if ((ch = scan()) != EOF) {
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
							return str.toString();
						}
						while (scanned != mark) {
							unscan();
							scanned--;
						}
						str.append(pattern.charAt(0));
					}
				}
				while (scanned != 0) {
					unscan();
					scanned--;
				}
			}
			return null;
		}
		
		/**
		 * Returns a string containing everything from the current position to the 
		 * first occurrence of <b>any</b> one of the specified characters.
		 * 
		 * @param any the character(s) to search until.
		 * @return everything from the current position to the first occurrence of 
		 * 	any of the specified characters or <code>null</code>, if none of those 
		 * 	characters could be found.
		 */
		public final String scanUntil(char... any) {
			if (any != null && any.length != 0) {
				StringBuffer str = new StringBuffer();
				int scanned = 0;
				char ch;
				while ((ch = scan()) != EOF) {
					scanned++;
					for (int i = any.length; --i >= 0; ) {
						if (charsEqual(ch, any[i], false)) {
							unscan();
							return str.toString();
						}
					}
					str.append(ch);
				}
				while (scanned != 0) {
					unscan();
					scanned--;
				}
			}
			return null;
		}
		
		/**
		 * Moves the internal cursor forward without returning a value (skipping 
		 * characters) while the next character read matches the subsequent 
		 * character in the specified string, in order, from first to last.
		 * 
		 * @param pattern the string to skip.
		 * @return the number of characters skipped; either zero or the length of 
		 * 	the specified string
		 */
		public final int skip(String pattern) {
			return skip(pattern, false);
		}
		
		/**
		 * Moves the internal cursor forward without returning a value (skipping 
		 * characters) while the next character read matches the subsequent 
		 * character in the specified string, in order, from first to last.
		 * 
		 * @param pattern the string to skip.
		 * @param ignoreCase specifies whether the comparison is case-insensitive.
		 * @return the number of characters skipped; either zero or the length of 
		 * 	the specified string.
		 */
		public final int skip(String pattern, boolean ignoreCase) {
			if (pattern != null && pattern.length() != 0) {
				char ch;
				for (int i = 0, len = pattern.length(); i < len; i++) {
					if ((ch = scan()) != EOF) {
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
			return 0;
		}
		
		/**
		 * Moves the internal cursor forward without returning a value (skipping 
		 * characters) while the next character read is equal to <b>any</b> one
		 * of the characters specified.
		 * 
		 * @param any the character(s), any of which, to skip on.
		 * @return the number of characters skipped.
		 */
		public final int skip(char... any) {
			int skipped = 0;
			if (any != null && any.length != 0) {
				char ch;
				while ((ch = scan()) != EOF) {
					boolean skip = false;
					for (int i = any.length; --i >= 0; ) {
						if (charsEqual(ch, any[i], false)) {
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
			}
			return skipped;
		}
		
		/**
		 * Compares two characters for equality either with or without regard for 
		 * case (as specified by the third argument).
		 * 
		 * @param ch1 the first character to be compared.
		 * @param ch2 the second character to be compared.
		 * @param ignoreCase specifies whether the comparison is case-insensitive.
		 * @return <code>true</code> if the characters are considered equal,
		 * 	<code>false</code> otherwise.
		 */
		private static boolean charsEqual(char ch1, char ch2, boolean ignoreCase) {
			if (ch1 != ch2) {
				if (!ignoreCase) {
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
}