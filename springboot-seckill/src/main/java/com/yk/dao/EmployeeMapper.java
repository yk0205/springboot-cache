package com.yk.dao;

import com.yk.pojo.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT * FROM employee WHERE id=#{id}")
    Employee getEmpById(Integer id);

    @Update("UPDATE employee SET lastName=#{lastName}，email=#{email},d_id=#{dId} WHERE id=#{id} ")
    void updateEmp(Employee emp);

    @Delete("DELETE FROM employee WHERE id=#{id}")
    void deleteEmpById(Integer id);
}
