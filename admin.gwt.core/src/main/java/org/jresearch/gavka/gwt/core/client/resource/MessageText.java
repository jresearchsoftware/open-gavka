package org.jresearch.gavka.gwt.core.client.resource;

import javax.annotation.Nonnull;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface MessageText extends Messages {

	@Nonnull
	@DefaultMessage("Connection id")
	String idPlaceholder();

	@Nonnull
	@DefaultMessage("Label")
	String labelField();

	@Nonnull
	@DefaultMessage("Connection label")
	String labelPlaceholder();

	@Nonnull
	@DefaultMessage("Icon")
	String iconField();

	@Nonnull
	@DefaultMessage("Color")
	String colorField();

	@Nonnull
	@DefaultMessage("Bootstrap servers (comma separated list)")
	String bootstrapServersField();

	@Nonnull
	@DefaultMessage("hostnames or IP separated by comma")
	String bootstrapServersPlaceholder();

	@Nonnull
	@DefaultMessage("Schema registry URL")
	String schemaRegistryUrlField();

	@Nonnull
	@DefaultMessage("hostname or IP of a schema registry server")
	String schemaRegistryUrlPlaceholder();

	@Nonnull
	@DefaultMessage("Custom properties")
	String propertyTagsField();

	@Nonnull
	@DefaultMessage("Custom properties, use fields bellow to add")
	String propertyTagsPlaceholder();

	@Nonnull
	@DefaultMessage("Key")
	String propertyKeyField();

	@Nonnull
	@DefaultMessage("New property key")
	String propertyKeyPlaceholder();

	@Nonnull
	@DefaultMessage("Value")
	String propertyValueField();

	@Nonnull
	@DefaultMessage("New property value")
	String propertyValuePlaceholder();

	@Nonnull
	@DefaultMessage("Cancel")
	String buttonCancel();

	/** Test connection on connection edit dialog */
	@Nonnull
	@DefaultMessage("Test")
	String buttonTest();

	@Nonnull
	@DefaultMessage("Save")
	String buttonSave();

	@Nonnull
	@DefaultMessage("Color doesn''t exist")
	String invalidColor();

	@Nonnull
	@DefaultMessage("Icon doesn''t exist")
	String invalidIcon();

	/** Message while connection testing on the server */
	@Nonnull
	@DefaultMessage("Checking...")
	String loaderChecking();

}
