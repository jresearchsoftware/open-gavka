package org.jresearch.gavka.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.jooq.JSONB;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

@SuppressWarnings({ "serial" })
public class PropertiesJsonBinder extends AbstractJsonBinder<Map> {

	public PropertiesJsonBinder() {
		super(Map.class, PropertiesJsonBinder::from, PropertiesJsonBinder::to);
	}

	private static JSONB to(@Nullable final Map<String, String> p) {
		return p == null ? null : JSONB.valueOf(ConfigFactory.parseMap(p).root().render(ConfigRenderOptions.concise()));
	}

	private static Map<String, String> from(@Nullable final JSONB json) {
		final HashMap<String, String> properties = new HashMap<>();
		if (json != null) {
			final Config config = ConfigFactory.parseString(json.toString());
			config.entrySet().forEach(e -> properties.put(e.getKey(), config.getString(e.getKey())));
		}
		return properties;
	}

}
