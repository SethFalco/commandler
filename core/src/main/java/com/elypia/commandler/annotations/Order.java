package com.elypia.commandler.annotations;

import java.lang.annotation.*;

/**
 * <p>
 *     Used for ensuring the order of parameters when using
 *     parameter objects rather than inline method parameters to
 *     describe command parameters, for example when making:
 * </p>
 * <pre><code>
 * public String sayCommand(@Param int first, @Param int second, @Param int third) {
 *
 * }</code></pre>
 *
 * <p>You may do:</p>
 * <pre><code>
 * public class SayParams {
 *
 *     &#64;Param
 *     &#64;Order(0)
 *     private int first;
 *
 *     &#64;Param
 *     &#64;Order(1)
 *     private int second;
 *
 *     &#64;Param
 *     &#64;Order(2)
 *     private int third;
 *
 *     // Getters/Setters
 * }
 *
 * public String sayCommand(@Params SayParams params) {
 *
 * }
 * </code></pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

    /**
     * The presidence this parameter has in the order.
     * orders do not have to be incremental from the previous
     * but there must be a clear way to determine which has more
     * presidence.
     *
     * @return The presidence this has when parsing parameters.
     */
    int order();
}
