package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.Comment;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.CommentDto;
import com.myoa.my_oa.service.CommentService;
import com.myoa.my_oa.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.DELETE;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tyy9
 * @since 2023-04-26
 */
@Api(tags = "评论接口")
@CrossOrigin
@RestController
@RequestMapping("/my_oa/comment")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "课程评论分页查询")
        @PostMapping("/pageComment_Course/{page}/{limit}")
    public R pageComment_Course(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "comment",value = "课程对象")
            @RequestBody(required = false) Comment comment
    ){
        Page<Comment> commentPage = new Page<>(page,limit);
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.isNull(Comment::getTeacherId)
                .eq(Comment::getCourseId,comment.getCourseId())
                .orderByDesc(Comment::getGmtCreate);
        commentService.page(commentPage,commentLambdaQueryWrapper);
        List<Comment> records = commentPage.getRecords();
        long total = commentPage.getTotal();
        List<CommentDto> commentDtoList=new ArrayList<>();
        for(Comment c:records){
            SysUser sysUser = sysUserService.getById(c.getUserId());
            if(sysUser!=null    ){
                String username = sysUser.getUsername();
                String avatarUrl = sysUser.getAvatarUrl();
                CommentDto commentDto = new CommentDto();
                commentDto.setContent(c.getContent());
                commentDto.setGmtCreate(c.getGmtCreate());
                commentDto.setUsername(username);
                commentDto.setAvatarUrl(avatarUrl);
                commentDto.setUserId(c.getUserId());
                commentDto.setId(c.getId());
                commentDtoList.add(commentDto);
            }

        }
        return R.ok().data("comment",commentDtoList).data("total",total);
    }



    @ApiOperation(value = "评论添加接口")
    @PostMapping("/addComment")
    public R addComment(
            @ApiParam(name = "comment",value = "评论对象")
            @RequestBody Comment comment
    ){
        boolean save = commentService.save(comment);
        return save?R.ok():R.error().message("评论发布失败");
    }

    @ApiOperation(value = "根据课程id获取评论")
    @PostMapping("/getCommentByTeacherId/{id}")
    public R getCommentByTeacherId(
            @ApiParam(name = "id",value = "教师id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getTeacherId,id).orderByDesc(Comment::getGmtCreate);
        List<Comment> list = commentService.list(commentLambdaQueryWrapper);
        return R.ok().data("comment",list);
    }

    @ApiOperation(value = "根据课程id获取评论")
        @PostMapping("/getCommentByCourseId/{id}")
    public R getCommentByCourseId(
            @ApiParam(name = "id",value = "课程id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getCourseId,id).orderByDesc(Comment::getGmtCreate);
        List<Comment> list = commentService.list(commentLambdaQueryWrapper);
        List<CommentDto> commentDtoList=new ArrayList<>();
        for(Comment c:list){
            SysUser sysUser = sysUserService.getById(c.getUserId());
            if(sysUser!=null    ){
                String username = sysUser.getUsername();
                String avatarUrl = sysUser.getAvatarUrl();
                CommentDto commentDto = new CommentDto();
                commentDto.setContent(c.getContent());
                commentDto.setGmtCreate(c.getGmtCreate());
                commentDto.setUsername(username);
                commentDto.setAvatarUrl(avatarUrl);
                commentDto.setUserId(c.getUserId());
                commentDto.setId(c.getId());
                commentDtoList.add(commentDto);
            }

        }
        return R.ok().data("comment",commentDtoList);
    }

    @ApiOperation(value = "评论删除接口")
    @DeleteMapping("/{id}")
    public R deleteCommentById(
            @ApiParam(value = "id",name = "评论id")
            @PathVariable Integer id
    ){
        boolean b = commentService.removeById(id);
        return b?R.ok():R.error().message("删除评论失败");
    }


}

