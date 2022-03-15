package com.luohao.lhdemo.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luohao.lhdemo.entity.User;
import com.luohao.lhdemo.mapper.UserMapper;
import com.luohao.lhdemo.service.UserService;
import com.luohao.lhdemo.service.impl.UserServiceImpl;
import io.swagger.models.auth.In;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 罗昊
 * @since 2022-03-08
 */
@RestController
@MapperScan("com.luohao.lhdemo.mapper")
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userService2;

    @Autowired
    private UserMapper userMapper;

    /*不使用插件实现分页*/
    /*@GetMapping("/page1")
    public Map<String,Object> findPage(@RequestParam int pageNum,@RequestParam int pageSize){
          pageNum = (pageNum-1)*pageSize;
          int total=userService.count();
        List<User> data =userMapper.selectpage1(pageNum,pageSize);
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;

    }*/


    /*实现分页*/
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam(defaultValue = "") String address) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        return userService.page(page, queryWrapper);
    }


    /*查询全部记录*/
    @GetMapping
    public List<User> getAllUser(){
        List<User> list = userService2.list();
        return list;
    }


    /*新增或更新*/
    @PostMapping("/save")
    public boolean saveone(@RequestBody User user){
        return userService.saveOrUpdate(user);

    }

    @PostMapping("/insert")
    public int save(@RequestBody User user){
        return  userMapper.insert(user);
    }

    @PostMapping("/updata")
    public boolean updatabyId(@RequestBody User user){
        return userService.updateById(user);
    }

    /*根据id进行删除*/
    @PutMapping("remove")
    public boolean remove(@RequestParam Integer id){
        return userService.removeById(id);
    }

    /*批量删除*/
    @PostMapping("/removeList")
    public  boolean removeByIds(@RequestBody List<Integer> ids){
        return userService.removeByIds(ids);
    }

    /*根据id进行查询*/
    @GetMapping("/select")
    public User selectbyid(int id){
        return userService.getById(id);
    }

    /*实现导出*/
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        /*从数据库查询出所有的数据*/
        List <User> list = userService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        /*自定义标题别名*/
        writer.addHeaderAlias("username","用户名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickname","昵称");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("createTime","创建时间");
        writer.addHeaderAlias("avatarurl","头像");

        /*一次性写出List内的对象到excel，使用默认式样，强制输出标题*/
        writer.write(list,true);

        /*设置浏览器响应格式*/
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");//application/vnd.openxmlformats-officedocument.spreadsheetml.sheet 参数含义为设置浏览器响应格式为.xlsx格式
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();


    }

    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<User> users = CollUtil.newArrayList();
        for (List<Object> row : list) {
            User user = new User();
            user.setUsername(row.get(0).toString());
            user.setPassword(row.get(1).toString());
            user.setNickname(row.get(2).toString());
            user.setEmail(row.get(3).toString());
            user.setPhone(row.get(4).toString());
            user.setAddress(row.get(5).toString());
            user.setAvatarurl(row.get(6).toString());
            users.add(user);
        }

        userService.saveBatch(users);
        return true;
    }


}

