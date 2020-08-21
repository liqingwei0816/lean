/**
 * 
 */
package com.github.compiler.exceptions;



/**
 * @author deniz.toktay
 *
 */
public class DynamicCompilerException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DynamicCompilerException() {

	}

	/**
	 * @param message
	 */
	public DynamicCompilerException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DynamicCompilerException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DynamicCompilerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DynamicCompilerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
