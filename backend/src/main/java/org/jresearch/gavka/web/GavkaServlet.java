package org.jresearch.gavka.web;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jresearch.gavka.domain.LogUiLogger;
import org.jresearch.gavka.tool.Loggers;
import org.jresearch.gavka.tool.Logs;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;

public class GavkaServlet extends HttpServlet {

	private static final Splitter REST_COMMAND_SPLITTER = Splitter.on('/').omitEmptyStrings();

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GavkaServlet.class);

	private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$

	private static final long serialVersionUID = 1202824349935915983L;

	public static final String CMD_GET_LOGGERS = "loggers"; //$NON-NLS-1$
	public static final String CMD_UPDATE_LOGGER = "/logger"; //$NON-NLS-1$
	public static final String REST = "/rest"; //$NON-NLS-1$
	private static final String GWT_HTML = "gwt.html"; //$NON-NLS-1$

	private ObjectMapper mapper;

	@Override
	public void init() throws ServletException {
		// Warm the logger
		ForkJoinPool.commonPool().execute(Logs::getLoggers);
		// Create Object JSON mapper
		mapper = new ObjectMapper();
		LOGGER.trace("Init of GavkaServlet complete."); //$NON-NLS-1$
		super.init();
	}

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String servletPath = req.getServletPath();
		if (servletPath.endsWith(REST)) {
			doRestGet(resp, req.getPathInfo());
		} else {
			doStartupGet(req, resp);
		}
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		final String servletPath = req.getServletPath();
		if (servletPath.endsWith(REST)) {
			doRestPost(req, resp, req.getPathInfo());
		}
	}

	protected void doRestPost(final HttpServletRequest req, final HttpServletResponse resp, final String restCommand) throws IOException {
		if (restCommand.startsWith(CMD_UPDATE_LOGGER) && (restCommand.length() == CMD_UPDATE_LOGGER.length() || restCommand.charAt(CMD_UPDATE_LOGGER.length()) == '/')) {
			final LogUiLogger logger = mapper.readValue(req.getReader(), LogUiLogger.class);
			if (logger != null) {
				final boolean result = Loggers.updateLogger(logger);
				mapper.writeValue(resp.getOutputStream(), Boolean.valueOf(result));
				resp.setContentType(APPLICATION_JSON);
			}
			return;
		}
		resp.setStatus(404);
	}

	protected void doRestGet(final HttpServletResponse resp, final String restCommand) throws IOException {
		if (restCommand != null) {
			final List<String> list = REST_COMMAND_SPLITTER.splitToList(restCommand);
			final String cmd = getRestCommand(list);
			switch (cmd) {
			case CMD_GET_LOGGERS:
				final boolean inherited = getInherited(list);
				final String filter = getFilter(list);
				LOGGER.trace("Filter logger list with {}", filter); //$NON-NLS-1$
				mapper.writeValue(resp.getOutputStream(), Loggers.getLoggers(filter, inherited));
				resp.setContentType(APPLICATION_JSON);
				break;
			default:
				break;
			}
		}
		resp.setStatus(404);
	}

	private static String getFilter(@Nonnull final List<String> list) {
		return list.size() < 3 ? "" : list.get(2);
	}

	private static boolean getInherited(@Nonnull final List<String> list) {
		return list.size() < 2 ? true : Boolean.parseBoolean(list.get(1));
	}

	private static String getRestCommand(@Nonnull final List<String> list) {
		return list.get(0);
	}

	protected static void doStartupGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher(GWT_HTML).include(req, resp);
	}

}
