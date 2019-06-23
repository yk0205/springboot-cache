package com.yk.controller;



import com.yk.pojo.Employee;
import com.yk.redis.RedisService;
import com.yk.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    @GetMapping({"/emp/{id}"})
    private Employee getAllEmployee(@PathVariable("id") Integer id){
       Employee employee = employeeService.getEmp(id);
       return employee;
    }

    @GetMapping({"/emp"})
    public Employee updateEmp(Employee employee) {
        Employee emp = employeeService.updateEmp(employee);
        return emp;
    }


    @GetMapping({"/deleteEmp"})
    public String deleteEmp(@PathVariable("id") Integer id){
        employeeService.deleteEmpById(id);
        return "sucess";
    }






}
