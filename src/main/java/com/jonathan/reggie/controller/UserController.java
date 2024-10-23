package com.jonathan.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jonathan.reggie.common.R;
import com.jonathan.reggie.entity.User;
import com.jonathan.reggie.service.UserService;
import com.jonathan.reggie.utils.SMSUtils;
import com.jonathan.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    public UserService userService;

    /**
     * Send phone SMS verification code
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //Get mobile phone number
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //Generate a random 4-digit verification code
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //Call the SMS service API provided by Alibaba Cloud to complete sending SMS
            SMSUtils.sendMessage("Reggie Take Out","",phone,code);

            //The generated verification code needs to be saved to Session
            session.setAttribute(phone,code);

            return R.success("Mobile phone verification code SMS sent successfully");
        }

        return R.error("Message failed to send");
    }

    /**
     * Mobile user login
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //Get mobile phone number
        String phone = map.get("phone").toString();

        //Get verification code
        String code = map.get("code").toString();

        //Get the saved verification code from Session
        Object codeInSession = session.getAttribute(phone);

        //Compare the verification code (compare the verification
        // code submitted on the page and the verification code saved in the Session)
        if(codeInSession != null && codeInSession.equals(code)){
            //If the comparison is successful, it means the login is successful.

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //Determine whether the user corresponding to the current mobile phone number is a new user.
                // If it is a new user, the registration will be automatically completed.
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("Login failed!");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session){

        // Remove the session and it will be done
        session.removeAttribute("user");

        return R.success("login out successfully");
    }

}
