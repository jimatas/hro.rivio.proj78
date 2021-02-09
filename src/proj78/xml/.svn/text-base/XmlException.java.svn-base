/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * The exception that is thrown when an error occurs while working with the DOM. 
 * 
 * @author K.A., copyright (c) 2008
 */
@SuppressWarnings("serial")
public class XmlException extends RuntimeException {
	/**
	 * An index or size was negative or greater than the allowed value.
	 */
	public static final int INDEX_SIZE_ERR = 1;
	
	/**
	 * (Not used) A range of text did not fit into a <code>DOMString</code>.
	 */
	public static final int DOMSTRING_SIZE_ERR = 2;
	
	/**
	 * An attempt was made to insert a node somewhere it does not belong, or 
	 * where it is not permitted.
	 */
	public static final int HIERARCHY_REQUEST_ERR = 3;
	
	/**
	 * A node was used in a document other than the one that created it.
	 */
	public static final int WRONG_DOCUMENT_ERR = 4;
	
	/**
	 * An invalid or illegal XML character was specified.
	 */
	public static final int INVALID_CHARACTER_ERR = 5;
	
	/**
	 * Data was specified for a node that does not support data.
	 */
	public static final int NO_DATA_ALLOWED_ERR = 6;
	
	/**
	 * An attempt was made to modify a node that is read-only.
	 */
	public static final int NO_MODIFICATION_ALLOWED_ERR = 7;
	
	/**
	 * A non-existent node was referenced.
	 */
	public static final int NOT_FOUND_ERR = 8;
	
	/**
	 * A requested object or operation is not supported by the implementation.
	 */
	public static final int NOT_SUPPORTED_ERR = 9;
	
	/**
	 * An attempt was made to add an attribute that is already in use elsewhere.
	 */
	public static final int INUSE_ATTRIBUTE_ERR = 10;
	
	/**
	 * (non-DOM)
	 * Badly formed XML was supplied to the parser.
	 */
	public static final int MALFORMED_XML_ERR = 101;
	
	/**
	 * (non-DOM)
	 * An error occurred during an I/O operation by a parser or serializer.
	 * Allows for application code to use unchecked exception handling for I/O errors.
	 */
	public static final int FILE_IO_ERR = 102;
	
	/**
	 * The error code; an integer value corresponding to one of the named 
	 * error constants.
	 */
	private final int errorCode;
	
	/**
	 * Constructs a new <code>XmlException</code> given the detail message and 
	 * error code.
	 * 
	 * @param errorMessage a message detailing the error that occured.
	 * @param errorCode an integer error code; one of the named error constants.
	 */
	public XmlException(String errorMessage, int errorCode) {
		super(prefixedErrorMessage(errorMessage, errorCode));
		this.errorCode = errorCode;
	}
	
	/**
	 * Constructs a new <code>XmlException</code> given the detail message.
	 * 
	 * @param errorMessage a message detailing the error that occured.
	 */
	public XmlException(String errorMessage) {
		this(errorMessage, 0);
	}
	
	/**
	 * Returns the error code.
	 * 
	 * @return an integer value corresponding to one of the named error constants.
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * Prefixes the error message with the category name corresponding to the specified 
	 * error code. (e.g., a message with error code <code>XmlException.INDEX_SIZE_ERR</code> 
	 * will be prefixed <code>"INDEX_SIZE_ERR: ..."</code>).
	 */
	private static String prefixedErrorMessage(String errorMessage, int errorCode) {
		switch (errorCode) {
			case INDEX_SIZE_ERR: 
				return String.format("INDEX_SIZE_ERR: %s", errorMessage);
			case DOMSTRING_SIZE_ERR: 
				return String.format("DOMSTRING_SIZE_ERR: %s", errorMessage);
			case HIERARCHY_REQUEST_ERR: 
				return String.format("HIERARCHY_REQUEST_ERR: %s", errorMessage);
			case WRONG_DOCUMENT_ERR: 
				return String.format("WRONG_DOCUMENT_ERR: %s", errorMessage);
			case INVALID_CHARACTER_ERR: 
				return String.format("INVALID_CHARACTER_ERR: %s", errorMessage);
			case NO_DATA_ALLOWED_ERR: 
				return String.format("NO_DATA_ALLOWED_ERR: %s", errorMessage);
			case NO_MODIFICATION_ALLOWED_ERR: 
				return String.format("NO_MODIFICATION_ALLOWED_ERR: %s", errorMessage);
			case NOT_FOUND_ERR: 
				return String.format("NOT_FOUND_ERR: %s", errorMessage);
			case NOT_SUPPORTED_ERR: 
				return String.format("NOT_SUPPORTED_ERR: %s", errorMessage);
			case INUSE_ATTRIBUTE_ERR: 
				return String.format("INUSE_ATTRIBUTE_ERR: %s", errorMessage);
			default: 
				return errorMessage;
		}
	}
}