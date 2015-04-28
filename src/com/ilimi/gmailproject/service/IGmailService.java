package com.ilimi.gmailproject.service;

import java.util.List;

import com.ilimi.gmailproject.dao.MsgDTO;

public interface IGmailService {

	public List<MsgDTO> getMessage(Thread id);

}
