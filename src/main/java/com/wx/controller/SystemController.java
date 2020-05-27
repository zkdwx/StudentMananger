package com.wx.controller;

import com.wx.controller.util.CpachaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/system")
public class SystemController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login() {
        return "system/login";
    }

    /**
     * 获取验证码
     *
     * @param request
     * @param response
     * @param vl
     * @param w
     * @param h
     */
    @RequestMapping("/get_cpacha")
    public void get_cpacha(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(value = "vl", defaultValue = "4", required = false) Integer vl,
                           @RequestParam(value = "w", defaultValue = "98", required = false) Integer w,
                           @RequestParam(value = "h", defaultValue = "33", required = false) Integer h) throws IOException {
        CpachaUtil cpachaUtil = new CpachaUtil(vl, w, h);
        String generatorVCode = cpachaUtil.generatorVCode();
        request.getSession().setAttribute("loginCpacha", generatorVCode);
        BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
        ImageIO.write(generatorRotateVCodeImage, "gif", response.getOutputStream());
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> login(@RequestParam(value = "username", required = true) String username,
                                    @RequestParam(value = "password", required = true) String password,
                                    @RequestParam(value = "vcode", required = true) String vcode){
        Map<String,String> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("type","error");
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("type","error");
            map.put("msg","密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(vcode)){
            map.put("type","error");
            map.put("msg","验证码不能为空");
            return map;
        }
        map.put("type","success");
        map.put("msg","登陆成功！");
        return map;

    }
}
