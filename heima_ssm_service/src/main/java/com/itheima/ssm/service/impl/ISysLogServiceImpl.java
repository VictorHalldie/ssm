package com.itheima.ssm.service.impl;


import com.itheima.ssm.dao.ISysLogDao;
import com.itheima.ssm.domain.SysLog;
import com.itheima.ssm.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ISysLogServiceImpl implements ISysLogService {

    @Autowired
    private ISysLogDao iSysLogDao;

    @Override
    public void save(SysLog sysLog) throws Exception {
        iSysLogDao.save(sysLog);
    }

    @Override
    public List<SysLog> findAll() throws Exception {
        return iSysLogDao.findAll();
    }
}
