package org.jresearch.gavka.dao;

import java.util.Properties;

import javax.annotation.Nullable;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

@SuppressWarnings({ "serial" })
public class PropertiesJsonBinder extends AbstractJsonBinder<Properties> {

	public PropertiesJsonBinder() { super(Properties.class, PropertiesJsonBinder::from, PropertiesJsonBinder::to); }

	private static Object to(@Nullable final Properties p) { return p == null ? null : ConfigFactory.parseProperties(p).root().render(ConfigRenderOptions.concise()); }

	private static Properties from(@Nullable final Object json) {
		final Properties properties = new Properties();
		if (json != null) {
			final Config config = ConfigFactory.parseString(json.toString());
			config.entrySet().forEach(e -> properties.setProperty(e.getKey(), config.getString(e.getKey())));
		}
		return properties;
	}

}
