package org.jresearch.gavka.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jresearch.gavka.domain.Connection;
import org.jresearch.gavka.domain.ConnectionList;
import org.jresearch.gavka.rest.api.GavkaConnectionService;
import org.jresearch.gavka.srv.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("nls")
@RestController
@RequestMapping(GavkaConnectionService.SRV_PATH)
public class ConnectionController implements GavkaConnectionService {

	private static final MediaType YAML_MEDIA_TYPE = MediaType.valueOf("application/vnd.yaml");

	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionController.class);

	@Autowired
	private ConnectionService connectionService;

	@Autowired
	@Qualifier("yamlMapper")
	private ObjectMapper yamlMapper;

	@Override
	@GetMapping(M_R_GET)
	public List<Connection> get() {
		return connectionService.connections();
	}

	@PostMapping(M_P_SAVE)
	@Override
	public Connection save(@RequestBody final Connection connection) {
		return connectionService.update(connection);
	}

	@PostMapping(M_P_REMOVE)
	@Override
	public boolean remove(@RequestBody final String connectionId) {
		return connectionService.remove(connectionId);
	}

	@SuppressWarnings("null")
	@PostMapping("/import")
	public void importConnections(@RequestParam final MultipartFile file) {
		try (InputStream stream = file.getInputStream()) {
			final ConnectionList list = yamlMapper.readValue(stream, ConnectionList.class);
			connectionService.importConnections(list);
		} catch (final Exception e) {
			LOGGER.warn("Can't import connections.", e);
		}
	}

	@GetMapping("/export")
	public ResponseEntity<String> exportConnections() throws IOException {
		final ConnectionList list = connectionService.exportConnections();
		final String body = yamlMapper.writeValueAsString(list);
		final ContentDisposition disposition = ContentDisposition.builder("attachment").filename(String.format("connection-%tY%<tm%<td%<tH%<tM.yaml", list.timestamp())).build();
		return ResponseEntity
				.ok()
				.contentType(YAML_MEDIA_TYPE)
				.header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
				.body(body);
	}

}
