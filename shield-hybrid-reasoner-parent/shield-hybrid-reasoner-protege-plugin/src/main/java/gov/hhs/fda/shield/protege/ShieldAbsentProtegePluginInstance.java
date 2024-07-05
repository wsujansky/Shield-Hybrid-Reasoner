package gov.hhs.fda.shield.protege;

import org.protege.editor.core.editorkit.plugin.EditorKitHook;
import org.semanticweb.elk.protege.ProtegeMessageAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;

public class ShieldAbsentProtegePluginInstance extends EditorKitHook {

	public ShieldAbsentProtegePluginInstance() {
	}

	public void dispose() throws Exception {
	}

	public void initialise() throws Exception {
		Logger logger = LoggerFactory.getLogger("org.semanticweb.elk");
		if (logger instanceof ch.qos.logback.classic.Logger) {
			ch.qos.logback.classic.Logger logbackLogger = (ch.qos.logback.classic.Logger) logger;
			ProtegeMessageAppender appender = ProtegeMessageAppender.getInstance();
			LoggerContext context = logbackLogger.getLoggerContext();
			appender.setContext(context);
			logbackLogger.addAppender(appender);
			ThresholdFilter filter = new ThresholdFilter();
			filter.setLevel(Level.WARN.levelStr);
			filter.start();
			appender.addFilter(filter);
			appender.start();
		}
	}
}
