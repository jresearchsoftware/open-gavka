package org.jresearch.gavka.web;

import java.util.List;

import org.jresearch.gavka.rest.api.GavkaConnectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;

@RestController
@RequestMapping(GavkaConnectionService.SRV_PATH)
public class ConnectionController implements GavkaConnectionService {

	@Override
	@PostMapping(M_R_GET)
	public List<String> get() {
		return ImmutableList.of();
	}

}
