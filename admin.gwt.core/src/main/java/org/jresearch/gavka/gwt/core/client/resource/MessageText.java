package org.jresearch.gavka.gwt.core.client.resource;

import javax.annotation.Nonnull;

import org.jresearch.gavka.domain.CheckStatus;

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
	@DefaultMessage("hostnames or IPs separated by comma")
	String bootstrapServersPlaceholder();

	@Nonnull
	@DefaultMessage("Schema registry URLs (comma separated list)")
	String schemaRegistryUrlField();

	@Nonnull
	@DefaultMessage("hostnames or IPs separated by comma")
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

	/** Message for failed test result */
	@Nonnull
	@DefaultMessage("Can''t check right now: {0}. Try latter.")
	String testResultFailed(String message);

	/** Message for empty test result */
	@Nonnull
	@DefaultMessage("Not tested yet")
	String testResultNotTested();

	/** Test result detail link */
	@Nonnull
	@DefaultMessage("Details...")
	String testResultDetailLink();

	/** Test result details, windows header */
	@Nonnull
	@DefaultMessage("Connection check details.")
	String testResultWindowsHeader();

	/** Test result details, section general check */
	@Nonnull
	@DefaultMessage("General")
	String testResultSectionGeneral();

	/** Test result details, property section header */
	@Nonnull
	@DefaultMessage("Properties check")
	String testResultPropSectionHeader();

	/** Test result details, property section header empty */
	@Nonnull
	@DefaultMessage("No custom properties defined or no detailed checks")
	String testResultPropSectionEmpty();

	/** Test result details, schema registry section header */
	@Nonnull
	@DefaultMessage("Schema registry servers status")
	String testResultSchemaRegSectionHeader();

	/** Test result details, property section header empty */
	@Nonnull
	@DefaultMessage("No schema registry servers defined")
	String testResultSchemaRegSectionEmpty();

	/** Test result details, bootstrap section header */
	@Nonnull
	@DefaultMessage("Bootstrap servers status")
	String testResultBootstrapSectionHeader();

	/** Test result details, status */
	@Nonnull
	@DefaultMessage("Unknown status: {0}")
	@AlternateMessage({
			"ERROR", "Something wrong",
			"OK_WITH_WARNING", "Connection is Ok, but there are some warrinigs",
			"OK", "Connection is Ok",
	})
	String testResultStatus(@Select CheckStatus status);

}
