package conf;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Class that wrap {@link ObjectMapper} to allows Spring create
 * defaultObjectMapper
 *
 * @author horto
 *
 */
public class YamlMapper {

	private final ObjectMapper mapper;

	public YamlMapper() {
		mapper = new ObjectMapper(new YAMLFactory())
				.registerModule(new JavaTimeModule())
				.registerModule(new Jdk8Module())
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	public String writeValueAsString(final Object value) throws JsonProcessingException {
		return mapper.writeValueAsString(value);
	}

	public <T> T readValue(final InputStream src, final Class<T> valueType) throws IOException, JsonParseException, JsonMappingException {
		return mapper.readValue(src, valueType);
	}

}
