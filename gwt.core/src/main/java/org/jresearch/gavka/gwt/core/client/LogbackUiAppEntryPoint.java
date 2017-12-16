package org.jresearch.gavka.gwt.core.client;

import org.fusesource.restygwt.client.Defaults;
import org.jresearch.commons.gwt.client.AbstractModule;
import org.jresearch.commons.gwt.client.AbstractModuleEntryPoint;
import org.jresearch.gavka.gwt.core.client.gin.LogbackUiGinjector;

import com.google.gwt.core.shared.GWT;

public class LogbackUiAppEntryPoint extends AbstractModuleEntryPoint {

	@Override
	protected AbstractModule getModule() {
		final LogbackUiGinjector gin = GWT.create(LogbackUiGinjector.class);
		return gin.getAppModule();
	}

	@Override
	public void onModuleLoad() {
		// Set the root URL for rest calls
		Defaults.setServiceRoot(com.google.gwt.core.client.GWT.getHostPageBaseURL());
		// Defaults.setExceptionMapper(new ExceptionMapper() {
		// @Override
		// public Throwable createFailedStatusException(final Method method,
		// final Response response) {
		// final int statusCode = response.getStatusCode();
		// switch (statusCode) {
		// case 600:
		// return new AnonymousUserRestException();
		// case 601:
		// return new NoUserRestException();
		// case 602:
		// return new UserRegistrationModelException();
		// case 603:
		// return new WrongConfigException();
		// default:
		// return new FailedResponseException(response);
		// }
		// // try {
		// // final JSONValue val =
		// // JSONParser.parseStrict(response.getText());
		// // JavaScriptObject o;
		// // if (val.isObject() != null) {
		// // o = val.isObject().getJavaScriptObject();
		// // } else if (val.isArray() != null) {
		// // o = val.isArray().getJavaScriptObject();
		// // } else {
		// // return new ResponseFormatException("Response was NOT a JSON
		// // object");
		// // }
		// // } catch (final JSONException e) {
		// // return new ResponseFormatException("Response was NOT a valid
		// // JSON document", e);
		// // } catch (final IllegalArgumentException e) {
		// // return new ResponseFormatException("Response was NOT a valid
		// // JSON document", e);
		// // }
		// // return new FailedResponseException(response);
		// }
		// });
		super.onModuleLoad();
	}

}
