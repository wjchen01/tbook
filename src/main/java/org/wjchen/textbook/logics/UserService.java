package org.wjchen.textbook.logics;

import org.wjchen.textbook.models.SakaiUser;

public interface UserService {

	public SakaiUser findByUserId(String userId);
	public SakaiUser findByUserNm(String userNm);
	
}
