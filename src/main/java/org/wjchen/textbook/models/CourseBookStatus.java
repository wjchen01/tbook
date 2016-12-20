package org.wjchen.textbook.models;

public enum CourseBookStatus
{
	required ("aaa.status.required"),
	recommended ("aaa.status.recommended"),
	optional ("aaa.status.optional"),
	avoid ("aaa.status.avoid");

	private String messagekey;
	private CourseBookStatus(String msgkey)
	{
		this.messagekey = msgkey;
	}
	/**
	 * @return
	 */
	public String messagekey() 
	{
		return this.messagekey;
	}
}
