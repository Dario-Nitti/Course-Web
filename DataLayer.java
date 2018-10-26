package model.Dal;

/**
 * We will always use our DataLayer implementation for MySQL databases within
 * a try-with-resources block, thus letting us not worry about resource exhaustion !
 * AutoCloseable -> An object that may hold resources until it is closed.
 * The close() method of an AutoCloseable object is called automatically when
 * exiting a try-with-resources block for which the object has been declared
 * in the resource specification header. This construction ensures prompt release,
 * avoiding resource exhaustion, exceptions and errors that may otherwise occur.
 *
 * @author Dario Nitti
 */
public interface DataLayer {}