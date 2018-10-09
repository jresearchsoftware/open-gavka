package org.jresearch.gavka.gwt.core.client.module.message.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark Kevin on 1/7/2016.
 */
public class DataHelper {

	/**
	 * Get all Users for GContacts Pattern
	 *
	 * @return
	 */
	public static List<UserDTO> getAllUsers() {
		final List<UserDTO> list = new ArrayList<>();
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/stevedesigner/128.jpg", UserDTO.Position.DEVELOPER, true, "Luis Hoppe", "luis@mail.com", "123123123", "718-555-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/yassiryahya/128.jpg", UserDTO.Position.DEVELOPER, true, "Irwin Mueller", "irwin@mail.com", "123123123", "718-432-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/lebinoclard/128.jpg", UserDTO.Position.DEVELOPER, true, "Levin Card", "levin@mail.com", "123123123", "432-555-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/lmjabreu/128.jpg", UserDTO.Position.DEVELOPER, false, "Dr. Cassie Keeling", "cassie@mail.com", "123123123", "432-555-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/ariil/128.jpg", UserDTO.Position.DEVELOPER, false, "Dr. Madelynn Schamberger", "madelyn@mail.com", "123123123", "543-555-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/devankoshal/128.jpg", UserDTO.Position.MARKETING, false, "Dominique Schmidt", "dom@mail.com", "123123123", "718-657-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/karthipanraj/128.jpg", UserDTO.Position.CTO, false, "Rowland Heller", "rowland@mail.com", "123123123", "718-765-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/GavicoInd/128.jpg", UserDTO.Position.CEO, false, "Quincy Schimmel", "quincy@mail.com", "123123123", "46-555-876", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/roybarberuk/128.jpg", UserDTO.Position.MARKETING, false, "Tierra VonRueden", "tierra@mail.com", "123123123", "654-56-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/kimcool/128.jpg", UserDTO.Position.MARKETING, false, "Travis Larson", "travis@mail.com", "123123123", "465-456-7654", "Makati City, Philippines", "Gwt Material"));
		list.add(new UserDTO("https://s3.amazonaws.com/uifaces/faces/twitter/tonymillion/128.jpg", UserDTO.Position.MARKETING, false, "Clint Heller", "clint@mail.com", "123123123", "645-555-65", "Makati City, Philippines", "Gwt Material"));
		return list;
	}

}