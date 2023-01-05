/*
 * 
 */
package enstabretagne.base.logger.loggerimpl;

import enstabretagne.base.logger.ILogger;
import enstabretagne.base.logger.LoggerConf;

// TODO: Auto-generated Javadoc
/**
 * The Class AbstractLogger.
 */
public abstract class AbstractLogger implements ILogger {

	/* (non-Javadoc)
	 * @see enstabretagne.base.logger.ILogger#checkLoggerConf(enstabretagne.base.logger.loggerimpl.LoggerConf)
	 */
	@Override
	public String checkLoggerConf(LoggerConf conf) {
		
		return conf.checkLoggerConf();
	}


}
