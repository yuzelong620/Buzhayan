package com.kratos.game.herphone.log.service;

import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.game.herphone.log.domain.ErrorLog;
import org.springframework.stereotype.Service;

@Service
public class ErrorLogServiceImpl extends BaseCrudService<String, ErrorLog> implements ErrorLogService {

}
