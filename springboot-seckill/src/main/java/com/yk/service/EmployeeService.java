package com.yk.service;

import com.yk.dao.EmployeeMapper;
import com.yk.pojo.Employee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EmployeeService {

    private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private EmployeeMapper employeeMapper;


    /**
     * condition 指定符合条件的情况下才缓存
     * condition = "#a0>1" 第一个参数大于0的时候才缓存
     * unless 否定缓存 当unless制定条件为true，方法返回值就不用缓存
     * @param id
     * @return
     */
    @Cacheable(value = {"emp"} ,condition = "#a0>1", unless = "#a0==2")
    public Employee getEmp(Integer id) {
        log.info("查询" + id + "号员工");
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }


    @CachePut(value = "emp",key = "#result.id")
    public Employee updateEmp(Employee employee) {
        log.info("updateEmp" + employee );
        employeeMapper.updateEmp(employee);
        return employee;
    }


    @CacheEvict(value = "emp",key = "#id")
    public void deleteEmpById(Integer id) {
        employeeMapper.deleteEmpById(id);

    }
}
