package com.nuts.service;

import com.nuts.domain.JoinVO;

public interface JoinService {
	
	public void join(JoinVO join);
	
	public boolean emailAuth(String uuid);
	
}
