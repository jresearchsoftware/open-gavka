package org.jresearch.gavka.gwt.core.client.module.message.widget.collapsible;

import org.jresearch.gavka.gwt.core.client.module.message.widget.UserDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;

public class CustomerCollapsible extends Composite {

	private static CustomerCollapsibleUiBinder uiBinder = GWT.create(CustomerCollapsibleUiBinder.class);
	private final UserDTO dto;

	interface CustomerCollapsibleUiBinder extends UiBinder<Widget, CustomerCollapsible> {
	}

	@UiField
	MaterialLabel lblName, lblEmail, lblPosition;

	@UiField
	MaterialImage imgUser;

	@UiField
	MaterialIcon iconStar;

	@UiField
	MaterialCollapsibleItem colapsItem;

	public CustomerCollapsible(final UserDTO dto) {
		initWidget(uiBinder.createAndBindUi(this));
		this.dto = dto;
		lblName.setText(dto.getName());
		lblEmail.setText(dto.getEmail());
		lblPosition.setText(dto.getPosition().getValue());
		imgUser.setUrl(dto.getPicture());
		if (dto.isStarred()) {
			iconStar.setIconType(IconType.STAR);
		}
	}

	public UserDTO getDto() {
		return dto;
	}

	public MaterialCollapsibleItem getColapsItem() {
		return colapsItem;
	}
}