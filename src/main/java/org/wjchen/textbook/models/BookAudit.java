package org.wjchen.textbook.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "TEXTBOOK_AUDIT")
public class BookAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "sequence", sequenceName = "TEXTBOOK_AUDIT_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "TIME")
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date time = new Date();

	@Column(name = "URL")
	private String url;
	
	@Column(name = "RESULT")
	private String result;
	
	public BookAudit(String url, String result) {
		this.url = url;
		this.result = result;
	}
	
}
