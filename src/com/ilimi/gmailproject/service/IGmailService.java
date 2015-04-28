package com.ilimi.gmailproject.service;

import java.util.List;

import com.ilimi.gmailproject.DAO.MsgDTO;

public interface IGmailService {

	public List<MsgDTO> getMessage(Thread id);

}
