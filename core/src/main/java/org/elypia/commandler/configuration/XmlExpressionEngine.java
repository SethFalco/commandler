package org.elypia.commandler.configuration;

import org.apache.commons.configuration2.tree.*;
import org.slf4j.*;

public class XmlExpressionEngine extends DefaultExpressionEngine {

    private static final Logger logger = LoggerFactory.getLogger(XmlExpressionEngine.class);

    /** @see DefaultExpressionEngineSymbols#DEFAULT_SYMBOLS */
    private static final DefaultExpressionEngineSymbols DEFAULT_SYMBOLS = DefaultExpressionEngineSymbols.DEFAULT_SYMBOLS;

    private static final DefaultExpressionEngineSymbols ENGINE = new DefaultExpressionEngineSymbols.Builder(DEFAULT_SYMBOLS)
        .setAttributeStart(DEFAULT_SYMBOLS.getPropertyDelimiter())
        .setAttributeEnd(null)
        .create();

    public XmlExpressionEngine() {
        super(ENGINE);
        logger.debug("Constructed instance of {}.", this.getClass());
    }
}
