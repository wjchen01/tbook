package org.wjchen.textbook.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * UserPermissioneProfile can prevent a particular user from performing 
 * certain actions, including creation, revision and removal of offers.
 *
 */

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_USERPERMISSIONPROFILE")
public class UserPermissionProfile 
{
	@Id
	@Column(name = "USERID")
	protected String userId;
	
	@Column(name = "CANCREATEOFFER")
	protected boolean canCreateOffer = true;
	
	@Column(name = "CANREVISEOFFER")
	protected boolean canReviseOffer = true;
	
	@Column(name = "CANREMOVEOFFER")
	protected boolean canRemoveOffer = true;
	
	@Column(name = "CREATEMESSAGE")
	protected String createMessage;
	
	@Column(name = "REVISEMESSAGE")
	protected String reviseMessage;
	
	@Column(name ="REMOVEMESSAGE")
	protected String removeMessage;
	
}
