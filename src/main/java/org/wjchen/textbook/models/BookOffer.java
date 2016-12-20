/**
 * $URL: https://source.sakaiproject.org/contrib/textbook/tbook/branches/2.9.x/api/src/java/org/sakaiproject/tbook/model/BookOffer.java $
 * $Id: BookOffer.java 56114 2008-12-12 22:13:08Z jimeng@umich.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.wjchen.textbook.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_OFFERS")
public class BookOffer implements Serializable {

	private static final long serialVersionUID = 1L;

	@SequenceGenerator(name = "sequence", sequenceName = "TEXTBOOK_OFFER_ID_SEQ") 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence") 
	@Column(name = "ID")
	private Long id;

	@Column(name = "VERSION")
	private int version;
	
	@Column(name = "DATEOFFERED")
	private Date dateOffered;

	@Column(name = "EXPIRATIONDATE")
	private Date expirationDate;

	@Column(name = "SELLER")
	private String seller;

	@Column(name = "SELLEREMAIL")
	private String sellerEmail;

	@Column(name = "NOTES")
	private String notes;

	@Column(name = "CURRENCY")
	private Currency currency = Currency.getInstance("USD");
	
	@Column(name = "PRICE")
	private BigDecimal price = new BigDecimal("0.00");
	
	@Column(name = "FIRM")
	private boolean firm;
	
	@Column(name = "REMOVED")
	private boolean removed = false;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "BOOKID")
	private Textbook book;
	
	public boolean isExpired() 
	{
		Date now = new Date();
		
		return now.after(this.expirationDate);
	}

}
