package org.jresearch.gavka.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jresearch.gavka.tool.Messages;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;

public class GavkaServlet extends HttpServlet {

	private static final Splitter REST_COMMAND_SPLITTER = Splitter.on('/').omitEmptyStrings();

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GavkaServlet.class);

	private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$

	private static final long serialVersionUID = 1202824349935915983L;

	public static final String CMD_GET_MESSAGES = "messages"; //$NON-NLS-1$
	public static final String REST = "/rest"; //$NON-NLS-1$

	private ObjectMapper mapper;

	@Override
	public void init() throws ServletException {
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
		}
	}

	protected void doRestGet(final HttpServletResponse resp, final String restCommand) throws IOException {
		if (restCommand != null) {
			final List<String> list = REST_COMMAND_SPLITTER.splitToList(restCommand);
			final String cmd = getRestCommand(list);
			switch (cmd) {
			case CMD_GET_MESSAGES:
				final String topic = getTopic(list);
				final LocalDate date = getDate(list);
				LOGGER.trace("Filter messages list with {} from {}", topic, date); //$NON-NLS-1$
				mapper.writeValue(resp.getOutputStream(), Messages.getLoggers(topic, date));
				resp.setContentType(APPLICATION_JSON);
				break;
			default:
				break;
			}
		}
		resp.setStatus(404);
	}

	private static LocalDate getDate(@Nonnull final List<String> list) {
		return list.size() < 3 ? LocalDate.now() : LocalDate.parse(list.get(2));
	}

	private static String getTopic(@Nonnull final List<String> list) {
		return list.size() < 2 ? "" : list.get(1);
	}

	private static String getRestCommand(@Nonnull final List<String> list) {
		return list.get(0);
	}

}
