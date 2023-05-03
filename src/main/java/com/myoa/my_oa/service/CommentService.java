package com.myoa.my_oa.service;

import com.myoa.my_oa.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myoa.my_oa.entity.dto.CommentDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tyy9
 * @since 2023-04-26
 */
public interface CommentService extends IService<Comment> {

    List<CommentDto> commentPage(List<Comment> commentList);
}
