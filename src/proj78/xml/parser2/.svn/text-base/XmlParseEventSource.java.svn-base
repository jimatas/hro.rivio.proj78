/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser2;

import java.util.Map;

/**
 * A class implementing this interface (typically a parser) acts as a source of parse events.
 * 
 * <p>The parse events that are generated are:
 * <ul>
 * 	<li>XML declaration event</li>
 * 	<li>DOCTYPE declaration event</li>
 * 	<li>Tag start and end events</li>
 * 	<li>Processing instruction event</li>
 * 	<li>Characters event</li>
 * 	<li>CDATA-section event and </li>
 * 	<li>Comment event.</li>
 * </ul>
 * 
 * @author K.A., copyright (c) 2008
 */
public interface XmlParseEventSource {
	/**
	 * Registers a parse event listener with this source.
	 * 
	 * @param listener an object implementing the {@link proj78.xml.parser2.XmlParseEventListener XmlParseEventListener} interface.
	 */
	void addParseEventListener(XmlParseEventListener listener);
	
	/**
	 * Removes a previously registered parse event listener.
	 * 
	 * @param listener the parse event listener object to remove.
	 */
	void removeParseEventListener(XmlParseEventListener listener);
	
	/**
	 * Fires an event to indicate that the XML declaration was just parsed.
	 * 
	 * <p>Note: implementors should invoke the <code>onXmlDeclaration</code> method on all the registered listeners.
	 * 
	 * @param version the XML version in use.
	 * @param encoding the character encoding in use; an optional attribute that may be <code>null</code>.
	 * @param standalone the standalone designation; either "yes", "no" or <code>null</code> (not specified).
	 */
	void fireXmlDeclarationEvent(String version, String encoding, String standalone);
	
	/**
	 * Fires an event to indicate that the DOCTYPE was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onDoctypeDeclaration</code> method on all the registered listeners.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @param publicId the external subset's public identifier which is optional and may be <code>null</code>.
	 * @param systemId the external subset's system identifier which is optional and may be <code>null</code>.
	 * @param internalSubset a string containing the internal subset; <code>null</code> if not present.
	 */
	void fireDoctypeDeclarationEvent(String dtdName, String publicId, String systemId, String internalSubset);
	
	/**
	 * Fires an event to indicate that the starting tag of an element was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onTagStart</code> method on all the registered listeners.
	 * 
	 * @param tagName the name of the tag.
	 * @param attrs a possibly empty map containing the attributes that were present.
	 */
	void fireTagStartEvent(String tagName, Map<String, String> attrs);
	
	/**
	 * Fires an event to indicate that the ending tag of an element was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onTagEnd</code> method on all the registered listeners.
	 * 
	 * @param tagName the name of the tag.
	 */
	void fireTagEndEvent(String tagName);
	
	/**
	 * Fires an event to indicate that a processing instruction was just parsed. 
	 * 
	 * <p>Note: Implementors should invoke the <code>onProcessingInstruction</code> method on all the registered listeners.
	 * 
	 * @param target the instruction target.
	 * @param data the instruction data; <code>null</code> if not present.
	 */
	void fireProcessingInstructionEvent(String target, String data);
	
	/**
	 * Fires an event to indicate that text (characters other than CDATA) was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onCharacters</code> method on all the registered listeners.
	 * 
	 * @param chars the characters that were parsed.
	 */
	void fireCharactersEvent(String chars);
	
	/**
	 * Fires an event to indicate that a CDATA-section was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onCDataSection</code> method on all the registered listeners.
	 * 
	 * @param content the content of the CDATA-section.
	 */
	void fireCDataSectionEvent(String content);
	
	/**
	 * Fires an event to indicate that a comment was just parsed.
	 * 
	 * <p>Note: Implementors should invoke the <code>onComment</code> method on all the registered listeners.
	 * 
	 * @param content the content of the comment (i.e., everything between <code>&lt;--</code> and <code>--&gt;</code>).
	 */
	void fireCommentEvent(String content);
}