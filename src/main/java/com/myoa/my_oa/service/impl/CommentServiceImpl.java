package com.myoa.my_oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myoa.my_oa.entity.Comment;
import com.myoa.my_oa.entity.SysUser;
import com.myoa.my_oa.entity.dto.CommentDto;
import com.myoa.my_oa.mapper.CommentMapper;
import com.myoa.my_oa.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myoa.my_oa.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-26
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    SysUserService sysUserService;


    @Override
    public List<CommentDto> commentPage(List<Comment> records) {

        List<CommentDto> commentDtoList=new ArrayList<>();
        for(Comment c:records){
            //获取评论用户
            SysUser sysUser = sysUserService.getById(c.getUserId());
            //获取回复用户
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLambdaQueryWrapper.eq(Comment::getId,c.getReplyId());
            Comment replycomment = this.getOne(commentLambdaQueryWrapper);
            if(sysUser!=null ){
                String username = sysUser.getUsername();
                String avatarUrl = sysUser.getAvatarUrl();

                CommentDto commentDto = new CommentDto();
                //----------------------
                commentDto.setContent(c.getContent());
                commentDto.setGmtCreate(c.getGmtCreate());
                commentDto.setUsername(username);
                commentDto.setAvatarUrl(avatarUrl);
                commentDto.setUserId(c.getUserId());
                commentDto.setId(c.getId());
                if(replycomment!=null){
                    log.info("reply=>",replycomment);
                    commentDto.setReply_content(replycomment.getContent());
                    SysUser replyuser= sysUserService.getById(replycomment.getUserId());
                    if(replyuser!=null){
                        String reply_username = replyuser.getUsername();
                        commentDto.setReplyuser_username(reply_username);
                    }else{
                        commentDto.setReplyuser_username("账号已注销");
                    }
                }else{
                    commentDto.setReply_content("");
                    commentDto.setReplyuser_username("");
                }
                //----------------------
                commentDtoList.add(commentDto);
            }else{
                CommentDto commentDto = new CommentDto();
                //----------------------
                commentDto.setContent(c.getContent());
                commentDto.setGmtCreate(c.getGmtCreate());
                commentDto.setUsername("账号已注销");
                commentDto.setAvatarUrl("../assets/user.png");
                commentDto.setUserId(c.getUserId());
                commentDto.setId(c.getId());
                if(replycomment!=null){
                    log.info("reply=>",replycomment);
                    commentDto.setReply_content(replycomment.getContent());
                    SysUser replyuser= sysUserService.getById(replycomment.getUserId());
                    if(replyuser!=null){
                        String reply_username = replyuser.getUsername();
                        commentDto.setReplyuser_username(reply_username);
                    }
                    else{
                        commentDto.setReplyuser_username("账号已注销");
                    }
                }else{
                    commentDto.setReply_content("");
                    commentDto.setReplyuser_username("");
                }
                //----------------------
                commentDtoList.add(commentDto);
            }
        }
        return commentDtoList;
    }


}
