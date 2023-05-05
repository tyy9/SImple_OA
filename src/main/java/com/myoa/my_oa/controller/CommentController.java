package com.myoa.my_oa.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.common.R;
import com.myoa.my_oa.entity.Comment;
import com.myoa.my_oa.entity.Course;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.CommentDto;
import com.myoa.my_oa.entity.dto.CommentSearchDto;
import com.myoa.my_oa.entity.dto.UserCommentDto;
import com.myoa.my_oa.exception.CustomerException;
import com.myoa.my_oa.service.CommentService;
import com.myoa.my_oa.service.SysUserService;
import com.myoa.my_oa.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
@Slf4j
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
            @ApiParam(name = "comment",value = "评论对象")
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
        List<CommentDto> commentDtoList1 = commentService.commentPage(records);

        return R.ok().data("comment",commentDtoList1).data("total",total);
    }

    @ApiOperation(value = "教师评论分页查询")
    @PostMapping("/pageComment_Teacher/{page}/{limit}")
    public R pageComment_Teacher(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "comment",value = "评论对象")
            @RequestBody(required = false) Comment comment
    ){
        Page<Comment> commentPage = new Page<>(page,limit);
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.isNull(Comment::getCourseId)
                .eq(Comment::getTeacherId,comment.getTeacherId())
                .orderByDesc(Comment::getGmtCreate);
        commentService.page(commentPage,commentLambdaQueryWrapper);
        List<Comment> records = commentPage.getRecords();
        long total = commentPage.getTotal();
        List<CommentDto> commentDtoList = commentService.commentPage(records);

        return R.ok().data("comment",commentDtoList).data("total",total);
    }

    @ApiOperation(value = "用户评论分页查询")
    @PostMapping("/pageUser_Comment/{page}/{limit}")
    public R pageUser_Comment(
            @ApiParam(name = "page",value = "当前页数",required = true)
            @PathVariable int page,
            @ApiParam(name = "limit",value = "最大显示数",required = true)
            @PathVariable int limit,
            @ApiParam(name = "search",value = "搜索对象")
            @RequestBody(required = false) CommentSearchDto search
    ){
      //先查出所有用户
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.like(!StringUtils.isEmpty(search.getUsername()),SysUser::getUsername,search.getUsername());
        List<SysUser> list = sysUserService.list(sysUserLambdaQueryWrapper);
        if(list.size()>0){
            List<UserCommentDto> userCommentDtoList = new ArrayList<>();
            for(SysUser user:list){
                //查出用户的评论
                LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
                commentLambdaQueryWrapper.eq(Comment::getUserId,user.getId());
                int count = commentService.count(commentLambdaQueryWrapper);
                //只获取发表过评论的用户
                if(count>0){
                    UserCommentDto userCommentDto = new UserCommentDto();
                    BeanUtils.copyProperties(user,userCommentDto);
                    //获取commentDto集合
                    List<Comment> commentList = commentService.list(commentLambdaQueryWrapper);
                    ArrayList<Comment> comments = new ArrayList<>();
                    //模糊查询
                    if(search.getContent()!=null&&search.getContent()!=""){
                        for(Comment c:commentList){
                            if(c.getContent().contains(search.getContent())){
                                comments.add(c);
                            }
                        }
                        if(comments.size()>0){
                            List<CommentDto> commentDtoList = commentService.commentPage(comments);
                            userCommentDto.setChildren(commentDtoList);
                            userCommentDtoList.add(userCommentDto);
                            Page page1 = PageUtils.getPage(page, limit, userCommentDtoList);
                            return R.ok().data("data",page1);
                        }else{
                            throw new CustomerException(25,"未能找到该用户的评论信息");
                        }
                    }
                    if(commentList.size()>0){
                        List<CommentDto> commentDtoList = commentService.commentPage(commentList);
                        userCommentDto.setChildren(commentDtoList);
                        userCommentDtoList.add(userCommentDto);
                    }else{
                        throw new CustomerException(25,"未能找到该用户的评论信息");
                    }

                }
            }
            Page page1 = PageUtils.getPage(page, limit, userCommentDtoList);
            return R.ok().data("data",page1);
        }else{
            throw new CustomerException(25,"未能找到该用户的评论信息");
        }

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

    @ApiOperation(value = "根据评论id回显")
    @PostMapping("/findCommentById/{id}")
    public R findCommentById(
            @ApiParam(name = "id",value = "评论id")
            @PathVariable Integer id
    ){
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getId,id);
        Comment one = commentService.getOne(commentLambdaQueryWrapper);
        return R.ok().data("comment",one);
    }

    @ApiOperation(value = "批量删除评论信息")
    @DeleteMapping("/deletebatch")
    public R deletebatch(
            @ApiParam(name="ids",value = "评论id集合")
            @RequestBody List<Integer> ids
    ){
        boolean b = commentService.removeByIds(ids);
        return b?R.ok():R.error();
    }

}

