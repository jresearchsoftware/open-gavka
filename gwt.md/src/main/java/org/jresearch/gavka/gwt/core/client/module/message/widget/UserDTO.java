package org.jresearch.gavka.gwt.core.client.module.message.widget;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;

public class UserDTO implements Serializable {

	public enum Position {
		CEO("CEO"),
		CTO("CTO"),
		DEVELOPER("Developer"),
		MARKETING("Marketing");

		private final String value;

		Position(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private String picture;

	private String name;
	private String email;
	private String password;

	private String address;
	private String contactNo;

	private String company;
	private boolean starred;

	private Position position;

	public UserDTO() {
	}

	public UserDTO(final String picture, final Position position, final boolean starred, final String name, final String email, final String password, final String contactNo, final String address, final String company) {
		this.picture = picture;
		this.position = position;
		this.starred = starred;
		this.name = name;
		this.email = email;
		this.password = password;
		this.contactNo = contactNo;
		this.address = address;
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(final String contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(final boolean starred) {
		this.starred = starred;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}
}