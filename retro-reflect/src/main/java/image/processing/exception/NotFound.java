package image.processing.exception;

/**
 * Useful to definitively exit a deep search without having to propagate results
 */
public class NotFound extends Exception {

	/**
	 * NOTE: using an instance is faster, but can complicate debugging
	 */
	public static NotFound INSTANCE = new NotFound();

	private static final long serialVersionUID = 1L;

}
