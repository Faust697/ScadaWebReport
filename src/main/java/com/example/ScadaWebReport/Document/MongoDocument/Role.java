package com.example.ScadaWebReport.Document.MongoDocument;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
	CANALS,
    SUBARTEZIAN,
    ADMIN,
    DATA_EDITOR;

	@Override
	public String getAuthority() {
		return name();
	}
}
