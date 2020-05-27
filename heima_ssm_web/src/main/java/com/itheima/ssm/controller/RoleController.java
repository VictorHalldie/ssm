package com.itheima.ssm.controller;


import com.itheima.ssm.domain.Permission;
import com.itheima.ssm.domain.Role;
import com.itheima.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    //给角色添加权限方法
    @RequestMapping("/addPermissionToRole.do")
    public String addPermissionToRole(@RequestParam(name = "roleId" , required = true)String roleId, @RequestParam(name = "ids",required = true)String[] permissionIds)throws Exception{
        iRoleService.addPermissionToRole(roleId,permissionIds);

        return "redirect:findAll.do";
    }




    //根据roleId查询role，并查询出可以添加的权限
    @RequestMapping("/findRoleByIdAndAllPermission.do")
    public ModelAndView findRoleByIdAndAllPermission(@RequestParam(name = "id",required = true)String roleId) throws  Exception{
        ModelAndView mv =new ModelAndView();
        //根据roleId查询role
        Role role=iRoleService.findById(roleId);
        //根据roleId查询可以添加的权限
        List<Permission> permissionList=iRoleService.findOtherPermissions(roleId);
        mv.addObject("role",role);
        mv.addObject("permissionList",permissionList);
        mv.setViewName("role-permission-add");
        return mv;
    }


    @RequestMapping("/findById.do")
    public ModelAndView findById(String id)throws Exception{
        ModelAndView mv =new ModelAndView();
        Role role=iRoleService.findById(id);
        mv.addObject("role",role);
        mv.setViewName("role-show");
        return mv;
    }


    
    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception{
        ModelAndView mv =new ModelAndView();
        List<Role> roleList = iRoleService.findAll();
        mv.addObject("roleList",roleList);
        mv.setViewName("role-list");
        return mv;
    }

    @RequestMapping("/save.do")
    public String save(Role role) throws  Exception{
        iRoleService.save(role);
        return "redirect:findAll.do";
    }

}
