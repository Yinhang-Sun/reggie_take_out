package com.jonathan.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.Employee;
import com.jonathan.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * employee login
     *
     * @param request
     * @param employee
     * @return
     */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1. Encrypt the password submitted on the page with md5
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. Query the database based on the username submitted on the page.
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3. If no query is found, the login failure result will be returned.
        if(emp == null) {
            return R.error("Login failed!");
        }

        //4. Password comparison, if inconsistent, return login failure result
        if(!emp.getPassword().equals(password)) {
            return R.error("Login failed!");
        }

        //5. Check the employee status. If it is disabled, return the employee disabled result.
        if(emp.getStatus() == 0) {
            return R.error("Account has been disabled");
        }

        //6. The login is successful, save the employee ID into the Session and return the login success result.
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * employee logout
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //Clear the id of the currently logged-in employee saved in the Session
        request.getSession().removeAttribute("employee");
        return R.success("Exit successfully");
    }

    /**
     * Add employees
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("Add new employee, employee information: {}",employee.toString());

        // Set the initial password to 123456, which requires md5 encryption.
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //Get the id of the currently logged-in user
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("Added employee successfully");

    }
}
